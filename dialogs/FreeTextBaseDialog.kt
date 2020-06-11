
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.View
import com.hannesdorfmann.fragmentargs.annotation.Arg
import kotlinx.android.synthetic.main.layout_dialog_free_text.*
import kotlinx.android.synthetic.main.layout_dialog_simple_message.*

abstract class FreeTextBaseDialog : BaseMvpDialog<FreeTextDialogView, FreeTextDialogPresenter>(), FreeTextDialogView {

    @Arg
    var timestamp: Long = System.currentTimeMillis()

    @Arg
    lateinit var dialogTitle: String

    @Arg
    lateinit var dialogMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun createPresenter(): FreeTextDialogPresenter =
            getComponent(ActivityComponent::class.java).freeTextDialogComponent()

    override fun injectDependencies() {
        super.injectDependencies()
        getComponent(ActivityComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.prepareMessageDate(timestamp)
        message.text = dialogMessage
    }

    override fun setFormattedDate(dateLabel: String) {
        date.text = dateLabel
    }

    override fun getTitle(): CharSequence? = dialogTitle

    override fun onPositiveClick(): Boolean {
        return handleOnPositiveClick()
    }

    abstract fun handleOnPositiveClick(): Boolean

    override fun onNegativeClick(): Boolean {
        dismiss()
        return false
    }

    @LayoutRes
    override fun getContentRes(): Int = R.layout.layout_dialog_free_text
}
