/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import net.mm2d.touchicon.http.HttpClientAdapter
import net.mm2d.touchicon.http.HttpResponse
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

internal class SimpleHttpClientAdapter(
    private val cookieHandler: CookieHandler? = null
) : HttpClientAdapter {
    override var userAgent: String = ""
    override var headers: Map<String, String> = emptyMap()

    @Throws(IOException::class)
    override fun head(url: String): HttpResponse = SimpleHttpResponse(createConnection(url).also {
        it.requestMethod = "HEAD"
        it.connect()
        it.extractCookie(url)
    })

    @Throws(IOException::class)
    override fun get(url: String): HttpResponse = SimpleHttpResponse(createConnection(url).also {
        it.requestMethod = "GET"
        it.connect()
        it.extractCookie(url)
    })

    private fun createConnection(url: String): HttpURLConnection = URL(url).openConnection().also {
        headers.forEach { entry ->
            it.setRequestProperty(entry.key, entry.value)
        }
        it.setRequestProperty(KEY_USER_AGENT, userAgent)
        it.setCookie(url)
        it.connectTimeout = TIMEOUT
        it.readTimeout = TIMEOUT
    } as HttpURLConnection

    private fun URLConnection.setCookie(url: String) {
        cookieHandler?.loadCookie(url)?.let {
            setRequestProperty(KEY_COOKIE, it)
        }
    }

    private fun HttpURLConnection.extractCookie(url: String) {
        val cookieHandler = cookieHandler ?: return
        headerFields[KEY_SET_COOKIE]?.forEach {
            cookieHandler.saveCookie(url, it)
        }
    }

    companion object {
        private const val TIMEOUT = 10000
        private const val KEY_USER_AGENT = "User-Agent"
        private const val KEY_SET_COOKIE = "Set-Cookie"
        private const val KEY_COOKIE = "Cookie"
    }
}
