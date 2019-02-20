/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import net.mm2d.touchicon.HttpClient
import net.mm2d.touchicon.HttpResponse
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class OkHttpHttpClient(
    private val client: OkHttpClient
) : HttpClient {
    override var userAgent: String = ""
    override var headers: Map<String, String> = emptyMap()

    @Throws(IOException::class)
    override fun head(url: String): HttpResponse {
        return Request.Builder()
            .head()
            .url(url)
            .appendHeader()
            .build()
            .execute()
    }

    @Throws(IOException::class)
    override fun get(url: String): HttpResponse {
        return Request.Builder()
            .get()
            .url(url)
            .appendHeader()
            .build()
            .execute()
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

    private fun Request.execute(): HttpResponse {
        return OkHttpHttpResponse(client.newCall(this).execute())
    }
}
