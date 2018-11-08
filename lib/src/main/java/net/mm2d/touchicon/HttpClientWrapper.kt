/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class HttpClientWrapper(private val client: OkHttpClient) {
    /**
     * Specify the value of User-Agent used for HTTP communication.
     *
     * It takes precedence over specification in [headers].
     */
    internal var userAgent: String = ""
    /**
     * Specify the HTTP communication header.
     *
     * User-Agent can also be specified, but [userAgent] takes precedence.
     */
    internal var headers: Map<String, String> = emptyMap()

    internal fun head(url: String): Response {
        val request = Request.Builder()
            .head()
            .url(url)
            .appendHeader()
            .build()
        return client.newCall(request).execute()
    }

    internal fun get(url: String): Response {
        val request = Request.Builder()
            .get()
            .url(url)
            .appendHeader()
            .build()
        return client.newCall(request).execute()
    }

    private fun Request.Builder.appendHeader(): Request.Builder {
        if (headers.isNotEmpty()) {
            headers(Headers.of(headers))
        }
        if (userAgent.isNotEmpty()) {
            header("User-Agent", userAgent)
        }
        return this
    }
}
