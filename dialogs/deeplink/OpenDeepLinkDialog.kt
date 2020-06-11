
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

@FragmentWithArgs
class OpenDeepLinkDialog : OpenFreeTextDialog() {

    private lateinit var deepLinkIntentFactory: DeepLinkIntentFactory

    private lateinit var appConfigPreferences: AppConfigPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepLinkIntentFactory = (context!!.applicationContext as MobileApplication).component.deepLinkIntentFactory()
        appConfigPreferences = (context!!.applicationContext as MobileApplication).component.appConfigPreferences()
        isCancelable = true
    }

    override fun openLink() {
        if (!appConfigPreferences.isTab(AppConfigPreferences.Companion.Tabs.TAB_FIRST)
                && !appConfigPreferences.isEnabledInMoreItems(DeepLinkTypes.FIRST) && link == DeepLinkTypes.FIRST) {
            return
        }
        if (!appConfigPreferences.isTab(AppConfigPreferences.Companion.Tabs.TAB_SECOND)
                && !appConfigPreferences.isEnabledInMoreItems(DeepLinkTypes.SECOND) && link == DeepLinkTypes.SECOND) {
            return
        }
        val intent = deepLinkIntentFactory.getInternalIntent(context!!, link)
        startActivity(intent)
    }

    companion object {

        private val FRAGMENT_TAG = OpenDeepLinkDialog::class.java.name

        @JvmStatic
        fun show(title: String, screenTitle: String, time: Long, message: String, url: String = "", activity: FragmentActivity) {
            val fm = activity.supportFragmentManager
            if (fm.findFragmentByTag(FRAGMENT_TAG) == null) {
                val positiveButtonText = activity.getString(R.string.button_ok_label)
                val negativeButtonText = activity.getString(R.string.button_close_label)
                val dialog = OpenDeepLinkDialogBuilder(message, title, url, positiveButtonText, screenTitle, time)
                .negativeButtonText(negativeButtonText).build()
                dialog.show(fm, FRAGMENT_TAG)
            }
        }
    }
}
