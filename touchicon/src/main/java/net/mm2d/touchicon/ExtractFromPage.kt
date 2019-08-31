/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import androidx.annotation.VisibleForTesting
import org.json.JSONObject

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromPage(
    private val httpClient: HttpClientAdapter,
    private val htmlParser: HtmlParserAdapter
) {
    var downloadLimit: Int = DEFAULT_LIMIT_SIZE

    internal fun invoke(siteUrl: String, withManifest: Boolean): List<Icon> {
        val html = try {
            fetch(siteUrl)
        } catch (ignored: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return extractFromHtml(siteUrl, html, withManifest)
    }

    private fun fetch(url: String): String = httpClient.get(url).use {
        if (!it.hasHtml()) "" else it.bodyString(downloadLimit) ?: ""
    }

    private fun HttpResponse.hasHtml(): Boolean {
        if (!isSuccess) return false
        return header("Content-Type")?.let {
            it.contains("text/html", true) || it.contains("application/xhtml+xml", true)
        } ?: false
    }

    @VisibleForTesting
    internal fun extractFromHtml(
        siteUrl: String,
        html: String,
        withManifest: Boolean
    ): List<Icon> = if (!withManifest) {
        htmlParser.extractLinkTags(html)
            .mapNotNull { createPageIcon(siteUrl, it) }
    } else {
        htmlParser.extractLinkTags(html)
            .flatMap { tag ->
                if (Relationship.of(tag.attr("rel")) == Relationship.MANIFEST) {
                    extractFromManifest(siteUrl, tag.attr("href"))
                } else {
                    createPageIcon(siteUrl, tag)?.let { listOf(it) }
                } ?: emptyList()
            }
    }

    private fun createPageIcon(siteUrl: String, linkTag: HtmlTag): PageIcon? {
        val rel = Relationship.of(linkTag.attr("rel")) ?: return null
        if (!rel.isIcon) return null
        val href = linkTag.attr("href")
        if (href.isEmpty()) return null
        val url = makeAbsoluteUrl(siteUrl, href)
        val sizes = linkTag.attr("sizes")
        val mimeType = linkTag.attr("type")
        return PageIcon(rel, url, sizes, mimeType)
    }

    private fun extractFromManifest(siteUrl: String, href: String): List<Icon>? {
        if (href.isEmpty()) return null
        val url = makeAbsoluteUrl(siteUrl, href)
        return try {
            httpClient.get(url).use {
                it.bodyString()?.extractFromManifestJson(url)
            }
        } catch (ignored: Exception) {
            null
        }
    }

    private fun String.extractFromManifestJson(baseUrl: String): List<Icon> = try {
        val icons = JSONObject(this).getJSONArray("icons")
        (0 until icons.length()).mapNotNull {
            try {
                createIcon(baseUrl, icons.getJSONObject(it))
            } catch (ignored: Exception) {
                null
            }
        }
    } catch (ignored: Exception) {
        emptyList()
    }

    private fun createIcon(baseUrl: String, icon: JSONObject): WebAppIcon =
        WebAppIcon(
            makeAbsoluteUrl(baseUrl, icon.getString("src")),
            icon.optString("sizes"),
            icon.optString("type"),
            icon.optString("density")
        )

    companion object {
        private const val DEFAULT_LIMIT_SIZE = 1024 * 64
    }
}
