package com.shibuyaxpress.nuclearcodeinspector

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shibuyaxpress.nuclearcodeinspector.WebPreviewFragmentArgs.Companion.fromBundle
import kotlinx.android.synthetic.main.activity_menu.*


class WebPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = WebPreviewFragment()
    }

    private lateinit var viewModel: WebPreviewViewModel
    private lateinit var webview: WebView
    private val url by lazy {
        fromBundle(arguments!!).UrlSelected
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.web_preview_fragment, container, false)
        webview = root.findViewById(R.id.webview)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request != null) {
                    view?.loadUrl(request.url.toString())
                }
                return true
            }
        }
        webview.settings.javaScriptEnabled = false
        webview.loadUrl(url)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WebPreviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
