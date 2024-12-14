/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import net.mm2d.touchicon.http.HttpClientAdapter
import net.mm2d.touchicon.http.HttpResponse
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import java.io.IOException

internal class OkHttpClientAdapter(
    private val client: OkHttpClient,
) : HttpClientAdapter {
    override var userAgent: String = ""
    override var headers: Map<String, String> = emptyMap()

    @Throws(IOException::class)
    override fun head(
        url: String,
    ): HttpResponse =
        Builder().head()
            .url(url)
            .appendHeader()
            .build()
            .execute()

    @Throws(IOException::class)
    override fun get(
        url: String,
    ): HttpResponse =
        Builder().get()
            .url(url)
            .appendHeader()
            .build()
            .execute()

    private fun Builder.appendHeader(): Builder =
        apply {
            if (headers.isNotEmpty()) {
                headers(headers.toHeaders())
            }
            if (userAgent.isNotEmpty()) {
                header("User-Agent", userAgent)
            }
        }

    private fun Request.execute(): HttpResponse = OkHttpResponse(client.newCall(this).execute())
}
