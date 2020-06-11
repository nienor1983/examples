
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent

class WebViewActivity : BaseActivity(), HasComponent<MainComponent> {

    companion object {

        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_URL = "extra_url"

        fun createLaunchIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_URL, url)
            }
        }
    }

    private lateinit var mainComponent: MainComponent

    override fun getComponent(): MainComponent = mainComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        mainComponent = DaggerMainComponent.builder()
                .appComponent((applicationContext as MobileApplication).component)
                .activityModule(ActivityModule(this))
                .build()
        mainComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val fragment = WebViewScreen.createScreen(intent.getStringExtra(EXTRA_URL),
                intent.getStringExtra(EXTRA_TITLE), false)
        supportFragmentManager.beginTransaction()
                .replace(R.id.primary_content_container, fragment,
                        WebViewScreen.TAG)
                .commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_BACK) {
            val fragment = supportFragmentManager.findFragmentByTag(WebViewScreen.TAG)
            if (fragment is WebViewScreen) {
                return fragment.onKeyBackDown()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun toolbar(): TypefaceToolbar? {
        return findViewById(R.id.toolbar)
    }
}
