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
internal class ExtractFromPage(private val extractor: TouchIconExtractor) {
    var downloadLimit: Int = DEFAULT_LIMIT_SIZE

    internal fun invoke(siteUrl: String): List<PageIcon> {
        val html = try {
            fetchHeaderPart(siteUrl)
        } catch (e: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return invoke(siteUrl, html)
    }

    @VisibleForTesting
    internal fun invoke(siteUrl: String, html: String): List<PageIcon> {
        return Jsoup.parse(html).getElementsByTag("link")
                .mapNotNull { createLinkIcon(siteUrl, it) }
                .toList()
    }

    private fun createLinkIcon(siteUrl: String, linkElement: Element): PageIcon? {
        val rel = Relationship.of(linkElement.attr("rel")) ?: return null
        val href = linkElement.attr("href")
        if (href.isEmpty()) {
            return null
        }
        val url = makeAbsoluteUrl(siteUrl, href)
        val sizes = linkElement.attr("sizes")
        val mimeType = linkElement.attr("type")
        return PageIcon(rel, url, sizes, mimeType)
    }

    private fun fetchHeaderPart(url: String): String {
        val response = extractor.executeGet(url)
        response.body()?.use {
            if (!response.hasHtml()) return ""
            if (downloadLimit <= 0) {
                return it.string()
            }
            return fetchHeaderPart(it.byteStream(), downloadLimit)
        }
        return ""
    }

    private fun Response.hasHtml(): Boolean {
        if (!isSuccessful) return false
        val type = header("Content-Type") ?: return false
        return type.contains("text/html", true) ||
                type.contains("application/xhtml+xml", true)
    }

    private fun fetchHeaderPart(stream: InputStream, limit: Int): String {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var remain = limit
        while (true) {
            val fetchSize = if (remain > BUFFER_SIZE) BUFFER_SIZE else remain
            val size = stream.read(buffer, 0, fetchSize)
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
        private const val BUFFER_SIZE = 1024
        private const val DEFAULT_LIMIT_SIZE = 1024 * 64
    }
}
