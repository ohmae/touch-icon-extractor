/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpWebView()
        fab.setOnClickListener {
            IconDialog.show(this, web_view.title, web_view.url)
        }
        val url = extractUrlToLoad(intent)
        if (url.isNotEmpty()) {
            web_view.loadUrl(url)
        } else {
            web_view.loadUrl(DEFAULT_URL)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        setIntent(intent)
        val url = extractUrlToLoad(intent)
        if (url.isNotEmpty()) {
            web_view.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
            return
        }
        super.onBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        web_view.settings.javaScriptEnabled = true
        web_view.settings.setSupportZoom(true)
        web_view.settings.builtInZoomControls = true
        web_view.settings.displayZoomControls = false
        web_view.settings.useWideViewPort = true
        web_view.settings.loadWithOverviewMode = true
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress_bar.progress = newProgress
            }
        }
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress_bar.progress = 0
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress_bar.visibility = View.INVISIBLE
            }
        }
        TouchIconExtractorHolder.extractor.userAgent = web_view.settings.userAgentString
    }

    companion object {
        private const val DEFAULT_URL = "https://m.yahoo.co.jp/"
        private const val YAHOO_SEARCH_URL = "https://search.yahoo.co.jp/search?ei=UTF-8"
        private const val YAHOO_SEARCH_QUERY_KEY = "p"

        private fun extractUrlToLoad(intent: Intent): String {
            return when (intent.action) {
                Intent.ACTION_VIEW ->
                    intent.data?.toString() ?: ""
                Intent.ACTION_SEARCH, Intent.ACTION_WEB_SEARCH ->
                    makeSearchUrl(intent.getStringExtra(SearchManager.QUERY) ?: "")
                else ->
                    ""
            }
        }

        private fun makeSearchUrl(query: String): String {
            return Uri.parse(YAHOO_SEARCH_URL)
                    .buildUpon()
                    .appendQueryParameter(YAHOO_SEARCH_QUERY_KEY, query)
                    .build()
                    .toString()
        }
    }
}
