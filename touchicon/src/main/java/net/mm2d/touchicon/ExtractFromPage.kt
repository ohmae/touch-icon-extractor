/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import androidx.annotation.VisibleForTesting

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromPage(
    private val httpClient: HttpClientAdapter,
    private val htmlParser: HtmlParser
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
        return htmlParser.extractLinkTags(html)
            .mapNotNull { createPageIcon(siteUrl, it) }
            .toList()
    }

    private fun createPageIcon(siteUrl: String, linkTag: HtmlTag): PageIcon? {
        val rel = Relationship.of(linkTag.attr("rel")) ?: return null
        val href = linkTag.attr("href")
        if (href.isEmpty()) {
            return null
        }
        val url = makeAbsoluteUrl(siteUrl, href)
        val sizes = linkTag.attr("sizes")
        val mimeType = linkTag.attr("type")
        return PageIcon(rel, url, sizes, mimeType)
    }

    private fun fetch(url: String): String {
        httpClient.get(url).use {
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
