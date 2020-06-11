

class WebViewPm(
        private val networkHelper: NetworkHelper
) : ScreenPresentationModel() {

    val getUrlCommand = Action<String>()
    val webViewLoadingUrlAction = Action<String>()
    val webViewLoadingUrlCommand = Command<String>()
    val showProgressCommand = Command<Boolean>()

    val loadingProgressAction = Action<Boolean>()
    private val reloadUrl = Action<String>()

    val errorDialogControl = Command<String>()

    override fun onCreate() {

        webViewLoadingUrlAction.observable
                .subscribe {
                    webViewLoadingUrlCommand.consumer.accept(it)
                }
                .untilDestroy()

        reloadUrl.observable
                .subscribe {
                    if (networkHelper.isOnline()) {
                        webViewLoadingUrlCommand.consumer.accept(it)
                    } else {
                        handleCommonError(ConnectionException(), true)
                    }
                }
                .untilDestroy()

        loadingProgressAction.observable
                .subscribe(showProgressCommand.consumer)
                .untilDestroy()
    }
}
