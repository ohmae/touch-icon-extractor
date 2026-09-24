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
import java.net.URI
import java.net.URLConnection

internal class SimpleHttpClientAdapter(
    private val cookieHandler: CookieHandler? = null,
) : HttpClientAdapter {
    override var userAgent: String = ""
    override var headers: Map<String, String> = emptyMap()

    @Throws(IOException::class)
    override fun head(
        url: String,
    ): HttpResponse = execute(url, "HEAD")

    @Throws(IOException::class)
    override fun get(
        url: String,
    ): HttpResponse = execute(url, "GET")

    private fun execute(
        url: String,
        method: String,
    ): HttpResponse {
        var currentUrl = url
        repeat(MAX_REDIRECTS + 1) { redirects ->
            val connection = createConnection(currentUrl)
            try {
                connection.requestMethod = method
                connection.instanceFollowRedirects = false
                connection.connect()
                connection.extractCookie(currentUrl)
                val response = SimpleHttpResponse(connection)
                val location = if (connection.responseCode in 300..399) response.header("Location") else null
                val nextUrl = location?.let { URI(currentUrl).resolve(it).toString() }
                if (redirects == MAX_REDIRECTS || nextUrl == null || !sameOrigin(currentUrl, nextUrl)) {
                    return response
                }
                response.close()
                currentUrl = nextUrl
            } catch (e: Exception) {
                connection.disconnect()
                throw e
            }
        }
        error("unreachable")
    }

    private fun sameOrigin(
        first: String,
        second: String,
    ): Boolean =
        runCatching {
            val a = URI(first)
            val b = URI(second)
            fun port(
                uri: URI,
            ): Int = uri.port.takeIf { it >= 0 } ?: if (uri.scheme.equals("https", true)) 443 else 80
            a.scheme.equals(b.scheme, true) &&
                (a.scheme.equals("http", true) || a.scheme.equals("https", true)) &&
                a.host != null && a.host.equals(b.host, true) &&
                a.userInfo == null && b.userInfo == null && port(a) == port(b)
        }.getOrDefault(false)

    private fun createConnection(
        url: String,
    ): HttpURLConnection =
        URI(url).toURL().openConnection().also {
            headers.forEach { entry ->
                it.setRequestProperty(entry.key, entry.value)
            }
            it.setRequestProperty(KEY_USER_AGENT, userAgent)
            it.setCookie(url)
            it.connectTimeout = TIMEOUT
            it.readTimeout = TIMEOUT
        } as HttpURLConnection

    private fun URLConnection.setCookie(
        url: String,
    ) {
        cookieHandler?.loadCookie(url)?.let {
            setRequestProperty(KEY_COOKIE, it)
        }
    }

    private fun HttpURLConnection.extractCookie(
        url: String,
    ) {
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
        private const val MAX_REDIRECTS = 5
    }
}
