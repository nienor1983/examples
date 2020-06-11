
import android.support.v4.app.FragmentActivity
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

@FragmentWithArgs
class OpenWebViewDialog : OpenFreeTextDialog() {

    override fun openLink() {
        val intent = WebViewActivity.createLaunchIntent(context!!, screenTitle, link)
        startActivity(intent)
    }

    companion object {

        private val FRAGMENT_TAG = OpenWebViewDialog::class.java.name

        @JvmStatic
        fun show(title: String, screenTitle: String, time: Long, message: String, url: String = "", activity: FragmentActivity) {
            val fm = activity.supportFragmentManager
            if (fm.findFragmentByTag(FRAGMENT_TAG) == null) {
                val posButtonText = activity.getString(R.string.ok)
                OpenWebViewDialogBuilder.newOpenWebViewDialog(message, title, url, posButtonText, screenTitle, time).show(fm, FRAGMENT_TAG)
            }
        }
    }
}
