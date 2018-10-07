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
class TouchIconExtractor(private val client: OkHttpClient) {
    private val fromPage = ExtractFromPage(this)
    private val fromDomain = ExtractFromDomain(this)
    var userAgent: String = ""
    var headers: Map<String, String> = emptyMap()
    var downloadLimit: Int
        get() = fromPage.downloadLimit
        set(value) {
            fromPage.downloadLimit = value
        }

    internal fun executeHead(url: String): Response {
        val request = Request.Builder()
                .head()
                .url(url)
                .appendHeader()
                .build()
        return client.newCall(request).execute()
    }

    internal fun executeGet(url: String): Response {
        val request = Request.Builder()
                .get()
                .url(url)
                .appendHeader()
                .build()
        return client.newCall(request).execute()
    }

    private fun Request.Builder.appendHeader(): Request.Builder {
        if (userAgent.isNotEmpty()) {
            header("User-Agent", userAgent)
        }
        if (headers.isNotEmpty()) {
            headers(Headers.of(headers))
        }
        return this
    }

    fun fromPage(siteUrl: String): List<PageIcon> {
        return fromPage.invoke(siteUrl)
    }

    fun fromDomain(siteUrl: String, withPrecomposed: Boolean = true, sizes: List<String> = emptyList()): DomainIcon? {
        return fromDomain.invoke(siteUrl, withPrecomposed, sizes)
    }

    fun fromDomainWithDownload(siteUrl: String, withPrecomposed: Boolean = true, sizes: List<String> = emptyList()): Pair<DomainIcon, ByteArray>? {
        return fromDomain.invokeWithDownload(siteUrl, withPrecomposed, sizes)
    }

    fun listFromDomain(siteUrl: String, withPrecomposed: Boolean = true, sizes: List<String> = emptyList()): List<DomainIcon> {
        return fromDomain.list(siteUrl, withPrecomposed, sizes)
    }
}
