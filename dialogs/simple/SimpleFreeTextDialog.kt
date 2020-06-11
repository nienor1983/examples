
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.layout_dialog_simple_message.*

@FragmentWithArgs
class SimpleFreeTextDialog : FreeTextBaseDialog() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message.text = dialogMessage
    }

    override fun handleOnPositiveClick(): Boolean {
        return getPresenter().onPositiveClick()
    }

    @LayoutRes
    override fun getContentRes(): Int = R.layout.layout_dialog_simple_message

    companion object {

        private val FRAGMENT_TAG = SimpleFreeTextDialog::class.java.name

        @JvmStatic
        fun show(title: String, message: String, time: Long, activity: FragmentActivity) {
            val fm = activity.supportFragmentManager
            if (fm.findFragmentByTag(FRAGMENT_TAG) == null) {
                val posButtonText = activity.getString(R.string.ok)
                SimpleFreeTextDialogBuilder.newSimpleFreeTextDialog(title, message, posButtonText, time).show(fm, FRAGMENT_TAG)
            }
        }
    }
}
