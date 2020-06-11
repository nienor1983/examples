
import javax.inject.Inject


class FreeTextDialogPresenter
@Inject constructor(
        private val mGetFreeTextDate: GetFreeTextDate
) : BasePresenterImpl<FreeTextDialogView>() {


    fun onPositiveClick() = false


    fun onNegativeClick() = false


    fun prepareMessageDate(timestamp: Long) {
        ifViewAttached {
            it.setFormattedDate(mGetFreeTextDate.args(timestamp).execute())
        }
    }
}
