/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.mm2d.webclip.settings.Settings

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class MainActivity : AppCompatActivity() {
    private lateinit var settings: Settings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings.get()
        setContentView(R.layout.activity_main)
        setUpWebView()
        fab.setOnClickListener {
            IconDialog.show(this, web_view.title, web_view.url, settings.shouldUseExtension())
        }
        val url = extractUrlToLoad(intent)
        if (url.isNotEmpty()) {
            web_view.loadUrl(url)
        } else {
            web_view.loadUrl(DEFAULT_URL)
        }
        back_button.setOnClickListener { web_view.goBack() }
        forward_button.setOnClickListener { web_view.goForward() }
        reload_button.setOnClickListener { web_view.reload() }
        settings_button.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
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
        web_view.settings.also {
            it.javaScriptEnabled = true
            it.setSupportZoom(true)
            it.builtInZoomControls = true
            it.displayZoomControls = false
            it.useWideViewPort = true
            it.loadWithOverviewMode = true
            it.domStorageEnabled = true
        }
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress_bar.progress = newProgress
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                site_title.text = title
            }
        }
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress_bar.progress = 0
                progress_bar.visibility = View.VISIBLE
                site_url.text = url
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress_bar.visibility = View.INVISIBLE
                site_url.text = url
                site_title.text = view?.title
            }
        }
        ExtractorHolder.local.userAgent = web_view.settings.userAgentString
        ExtractorHolder.library.userAgent = web_view.settings.userAgentString
    }

    companion object {
        private const val DEFAULT_URL = "https://www.google.com/"
        private const val YAHOO_SEARCH_URL = "https://search.yahoo.co.jp/search?ei=UTF-8"
        private const val YAHOO_SEARCH_QUERY_KEY = "p"

        private fun extractUrlToLoad(intent: Intent): String = when (intent.action) {
            Intent.ACTION_VIEW ->
                intent.data?.toString() ?: ""
            Intent.ACTION_SEARCH, Intent.ACTION_WEB_SEARCH ->
                makeSearchUrl(
                    intent.getStringExtra(
                        SearchManager.QUERY
                    ) ?: ""
                )
            else ->
                ""
        }

        private fun makeSearchUrl(query: String): String = Uri.parse(YAHOO_SEARCH_URL)
            .buildUpon()
            .appendQueryParameter(YAHOO_SEARCH_QUERY_KEY, query)
            .build()
            .toString()
    }
}
