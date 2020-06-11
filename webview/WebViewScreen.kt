
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.layout_app_bar.*

class WebViewScreen : Screen<WebViewPm>(), DrawerSectionedFragment {

    override val sectionIndex: Int = UserExperiencePreferences.GOODS

    override val locationRes: Int = R.string.ym_location_webview

    override val screenLayout: Int = R.layout.fragment_web_view

    companion object {
        val TAG = WebViewScreen::class.java.name

        fun createScreen(url: String, title: String, isFromNavBar: Boolean): WebViewScreen {
            return WebViewScreen().apply {
                this.url = url
                this.title = title
                this.isFromBottomNavigationBar = isFromNavBar
            }
        }

        private const val KEY_TITLE = "title"
        private const val KEY_URL = "url"
        private const val KEY_IS_FROM_BOTTOM_NAVIGATION_BAR = "is_from_bottom_navigation_bar"
    }

    var url: String = ""
    var title: String = ""
    var isFromBottomNavigationBar: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState?.containsKey(KEY_TITLE) == true) {
            title = savedInstanceState.getString(KEY_TITLE) ?: ""
        }
        if (savedInstanceState?.containsKey(KEY_URL) == true) {
            url = savedInstanceState.getString(KEY_URL) ?: ""
        }
        if (savedInstanceState?.containsKey(KEY_IS_FROM_BOTTOM_NAVIGATION_BAR) == true) {
            isFromBottomNavigationBar = savedInstanceState.getBoolean(KEY_IS_FROM_BOTTOM_NAVIGATION_BAR)
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.clearCache(true)
        webView.clearHistory()
        CookieManager.getInstance().removeAllCookies(activity!!)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            setUpToolbar()
        }
    }

    override fun bindPresentationModel(pm: WebViewPm) {

        pm.webViewLoadingUrlAction.consumer.accept(url)

        pm.webViewLoadingUrlCommand.bindTo { url ->
            if (url == webView.url) {
                webView.reload()
            } else {
                webView.loadUrl(url)
            }
        }

        pm.showProgressCommand.bindTo {
            progress.visible = it
        }

        pm.errorDialogControl.bindTo { message ->
            activity?.let {
                AlertDialog.Builder(it)
                        .setMessage(message)
                        .setPositiveButton(resources.getString(R.string.partner_services_error_button))
                        { _, _ ->
                            if (!isFromBottomNavigationBar) {
                                finish()
                            }
                        }
                        .setCancelable(false)
                        .create()
                        .show()
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String, icon: Bitmap?) {
                super.onPageStarted(view, url, icon)
                pm.loadingProgressAction.consumer.accept(true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                pm.loadingProgressAction.consumer.accept(false)
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler?, error: SslError?) {
                if (appComponent().webHelper()
                                .shouldProceedWithNotValidCertifications()) {
                    handler?.proceed()
                } else {
                    super.onReceivedSslError(view, handler, error)
                }
            }
        }
    }

    override fun providePresentationModel(): WebViewPm =
            WebViewPm(appComponent().networkHelper())

    private fun setUpToolbar() {

        val toolbar = activity?.toolbar

        if (toolbar == null || activity == null) {
            return
        }

        if (activity is MainActivity) {
            toolbar.visibility = View.GONE
        } else {
            toolbar.title = title
            toolbar.menu.clear()
            toolbar.setNavigationOnClickListener{
                onBackPressed()
            }
            toolbar.setNavigationIcon(R.drawable.ic_close_blue_24dp)
        }
    }

    fun onBackPressed() {
        finish()
    }

    fun finish() {
        activity?.let {
            it.finish()
            webView.stopLoading()
        }
    }

    fun onKeyBackDown(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            if (!isFromBottomNavigationBar) {
                onBackPressed()
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString(KEY_TITLE, title)
        outState.putString(KEY_URL, url)
        outState.putBoolean(KEY_IS_FROM_BOTTOM_NAVIGATION_BAR, isFromBottomNavigationBar)

        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        setUpToolbar()
        presentationModel.getUrlCommand.consumer.accept(url)
    }
}
