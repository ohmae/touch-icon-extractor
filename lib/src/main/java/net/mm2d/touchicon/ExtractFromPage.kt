/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import androidx.annotation.VisibleForTesting
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromPage(
    private val http: HttpClient
) {
    var downloadLimit: Int = DEFAULT_LIMIT_SIZE

    internal fun invoke(siteUrl: String): List<PageIcon> {
        val html = try {
            fetch(siteUrl)
        } catch (e: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return invoke(siteUrl, html)
    }

    @VisibleForTesting
    internal fun invoke(siteUrl: String, html: String): List<PageIcon> {
        return Jsoup.parse(html).getElementsByTag("link")
            .mapNotNull { createPageIcon(siteUrl, it) }
            .toList()
    }

    private fun createPageIcon(siteUrl: String, linkElement: Element): PageIcon? {
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

    private fun fetch(url: String): String {
        http.get(url).use {
            if (!it.hasHtml()) return ""
            return it.bodyString(downloadLimit) ?: ""
        }
    }

    private fun HttpResponse.hasHtml(): Boolean {
        if (!isSuccess) return false
        val type = header("Content-Type") ?: return false
        return type.contains("text/html", true) ||
                type.contains("application/xhtml+xml", true)
    }

    companion object {
        private const val DEFAULT_LIMIT_SIZE = 1024 * 64
    }
}
