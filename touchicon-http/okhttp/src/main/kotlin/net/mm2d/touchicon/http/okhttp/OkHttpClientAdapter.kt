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
import java.net.URI

internal class OkHttpClientAdapter(
    client: OkHttpClient,
) : HttpClientAdapter {
    private val followRedirects = client.followRedirects
    private val redirectClient = client.newBuilder().followRedirects(false).followSslRedirects(false).build()
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

    private fun Request.execute(): HttpResponse {
        var request = this
        repeat(MAX_REDIRECTS + 1) { redirects ->
            val response = redirectClient.newCall(request).execute()
            val location = if (response.code in 300..399) response.header("Location") else null
            val nextUrl = location?.let { runCatching { request.url.toUri().resolve(it).toString() }.getOrNull() }
            if (!followRedirects || redirects == MAX_REDIRECTS || nextUrl == null ||
                !sameOrigin(request.url.toString(), nextUrl)
            ) {
                return OkHttpResponse(response)
            }
            response.close()
            request = request.newBuilder().url(nextUrl).build()
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

    companion object {
        private const val MAX_REDIRECTS = 5
    }
}
