/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.support.annotation.VisibleForTesting
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromHtml(private val extractor: TouchIconExtractor) {
    var downloadLimit: Int = DEFAULT_LIMIT_SIZE

    internal fun invoke(siteUrl: String): List<LinkIcon> {
        val html = try {
            fetchHead(siteUrl)
        } catch (e: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return invoke(siteUrl, html)
    }

    @VisibleForTesting
    internal fun invoke(siteUrl: String, html: String): List<LinkIcon> {
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

    private fun fetchHead(url: String): String {
        val response = extractor.executeGet(url)
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

    companion object {
        const val BUFFER_SIZE = 1024
        const val DEFAULT_LIMIT_SIZE = 1024 * 64
    }
}
