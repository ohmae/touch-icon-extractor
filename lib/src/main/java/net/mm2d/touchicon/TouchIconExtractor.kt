/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.net.Uri
import android.support.annotation.VisibleForTesting
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class TouchIconExtractor(private val client: OkHttpClient) {
    var userAgent: String = ""
    var headers: Map<String, String> = emptyMap()
    var downloadLimit: Int = DEFAULT_LIMIT_SIZE

    fun extract(siteUrl: String): List<LinkIcon> {
        val html = try {
            fetchHead(siteUrl)
        } catch (e: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return extract(siteUrl, html)
    }

    @VisibleForTesting
    internal fun extract(siteUrl: String, html: String): List<LinkIcon> {
        return Jsoup.parse(html).getElementsByTag("link")
                .mapNotNull { createLinkIcon(siteUrl, it) }
                .toList()
    }

    private fun createLinkIcon(siteUrl: String, linkElement: Element): LinkIcon? {
        val rel = Rel.of(linkElement.attr("rel")) ?: return null
        val href = linkElement.attr("href")
        if (href.isEmpty()) {
            return null
        }
        val url = makeAbsoluteUrl(siteUrl, href)
        val sizes = linkElement.attr("sizes")
        val mimeType = linkElement.attr("type")
        return LinkIcon(rel, url, sizes, mimeType)
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

    private fun fetchHead(url: String): String {
        val request = Request.Builder()
                .get()
                .url(url)
                .appendHeader()
                .build()
        val response = client.newCall(request).execute()
        response.body()?.use {
            if (!response.hasHtml()) return ""
            if (downloadLimit <= 0) {
                return it.string()
            }
            return fetchHead(it.byteStream(), downloadLimit)
        } ?: return ""
    }

    private fun Response.hasHtml(): Boolean {
        if (!isSuccessful) return false
        val type = header("Content-Type") ?: return false
        return type.contains("text/html", true) ||
                type.contains("application/xhtml+xml", true)
    }

    private fun fetchHead(stream: InputStream, limit: Int): String {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var remain = limit
        while (true) {
            val size = stream.read(buffer, 0, if (remain > BUFFER_SIZE) BUFFER_SIZE else remain)
            if (size < 0) {
                break
            }
            output.write(buffer, 0, size)
            remain -= size
            if (remain <= 0) {
                break
            }
        }
        return String(output.toByteArray())
    }

    data class TryData(
            val rel: Rel,
            val name: String,
            val sizes: String,
            val precomposed: Boolean
    )

    fun extractFromRoot(siteUrl: String, withPrecomposed: Boolean = true, sizes: List<String> = emptyList()): List<RootIcon> {
        val base = Uri.parse(siteUrl)
                .buildUpon()
                .path(null)
                .fragment(null)
                .clearQuery()
        return createTryDataList(withPrecomposed, sizes)
                .mapNotNull {
                    try {
                        tryFetch(base, it)
                    } catch (e: Exception) {
                        null
                    }
                }
    }

    private fun createTryDataList(withPrecomposed: Boolean, sizes: List<String>): List<TryData> {
        val result: MutableList<TryData> = mutableListOf()
        sizes.forEach {
            if (withPrecomposed) {
                result.add(TryData(Rel.APPLE_TOUCH_ICON_PRECOMPOSED, "$APPLE_TOUCH_ICON-$it-$PRECOMPOSED.$PNG", it, true))
            }
            result.add(TryData(Rel.APPLE_TOUCH_ICON, "$APPLE_TOUCH_ICON-$it.$PNG", it, false))
        }
        if (withPrecomposed) {
            result.add(TryData(Rel.APPLE_TOUCH_ICON_PRECOMPOSED, "$APPLE_TOUCH_ICON-$PRECOMPOSED.$PNG", "", true))
        }
        result.add(TryData(Rel.APPLE_TOUCH_ICON, "$APPLE_TOUCH_ICON.$PNG", "", false))
        result.add(TryData(Rel.ICON, FAVICON_ICO, "", false))
        return result
    }

    private fun tryFetch(baseUri: Uri.Builder, tryData: TryData): RootIcon? {
        val url = baseUri.path(tryData.name).build().toString()
        val request = Request.Builder()
                .head()
                .url(url)
                .appendHeader()
                .build()
        val response = client.newCall(request).execute()
        try {
            if (!response.isSuccessful) return null
            val type = response.header("Content-Type") ?: ""
            val length = response.header("Content-Length")?.toIntOrNull() ?: -1
            return RootIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
        } finally {
            response.body()?.close()
        }
    }

    companion object {
        const val BUFFER_SIZE = 1024
        const val DEFAULT_LIMIT_SIZE = 1024 * 64

        const val FAVICON_ICO = "favicon.ico"
        const val APPLE_TOUCH_ICON = "apple-touch-icon"
        const val PNG = "png"
        const val PRECOMPOSED = "precomposed"
    }
}
