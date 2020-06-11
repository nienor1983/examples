import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

@FragmentWithArgs
class OpenExternalLinkDialog : OpenFreeTextDialog() {

    override fun openLink() {
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            this.data = Uri.parse(link)
        }
        startActivity(viewIntent)
    }

    companion object {

        private val FRAGMENT_TAG = FreeTextBaseDialog::class.java.name

        @JvmStatic
        fun show(title: String, message: String, time: Long, url: String = "", activity: FragmentActivity) {
            val fm = activity.supportFragmentManager
            if (fm.findFragmentByTag(FRAGMENT_TAG) == null) {
                val posButtonText = activity.getString(R.string.ok)
                OpenExternalLinkDialogBuilder.newOpenExternalLinkDialog(message, title, url, posButtonText, "", time).show(fm, FRAGMENT_TAG)
            }
        }
    }
}
