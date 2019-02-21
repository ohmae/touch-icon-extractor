/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import net.mm2d.touchicon.HttpAdapter
import net.mm2d.touchicon.HttpResponse
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class SimpleHttpAdapter : HttpAdapter {
    override var userAgent: String = ""
    override var headers: Map<String, String> = emptyMap()

    @Throws(IOException::class)
    override fun head(url: String): HttpResponse {
        val connection = createConnection(url)
        connection.requestMethod = "HEAD"
        connection.connect()
        return SimpleHttpResponse(connection)
    }

    @Throws(IOException::class)
    override fun get(url: String): HttpResponse {
        val connection = createConnection(url)
        connection.requestMethod = "GET"
        connection.connect()
        return SimpleHttpResponse(connection)
    }

    private fun createConnection(url: String): HttpURLConnection {
        return URL(url).openConnection().also {
            headers.forEach { entry ->
                it.setRequestProperty(entry.key, entry.value)
            }
            it.setRequestProperty("User-Agent", userAgent)
            it.connectTimeout = TIMEOUT
            it.readTimeout = TIMEOUT
        } as HttpURLConnection
    }

    companion object {
        private const val TIMEOUT = 10000
    }
}
