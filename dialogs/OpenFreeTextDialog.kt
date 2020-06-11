import com.hannesdorfmann.fragmentargs.annotation.Arg

abstract class OpenFreeTextDialog : FreeTextBaseDialog() {

    @Arg
    var link: String = ""

    @Arg
    lateinit var screenTitle: String

    override fun handleOnPositiveClick(): Boolean {
        return if (link.isEmpty()) {
            getPresenter().onPositiveClick()
        } else {
            openLink()
            false
        }
    }

    abstract fun openLink()
}
