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
        } catch (e: Exception) {
            ""
        }
        if (html.isEmpty()) return emptyList()
        return extractFromHtml(siteUrl, html, withManifest)
    }

    @VisibleForTesting
    internal fun extractFromHtml(
        siteUrl: String,
        html: String,
        withManifest: Boolean
    ): List<Icon> {
        val linkTags = htmlParser.extractLinkTags(html)
        if (withManifest) {
            return linkTags.flatMap { tag ->
                if (tag.attr("rel").equals("manifest", true)) {
                    extractFromManifest(siteUrl, tag.attr("href"))
                } else {
                    createPageIcon(siteUrl, tag)?.let { listOf(it) } ?: emptyList()
                }
            }
        }
        return linkTags.mapNotNull { createPageIcon(siteUrl, it) }
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

    private fun extractFromManifest(siteUrl: String, href: String): List<Icon> {
        if (href.isEmpty()) return emptyList()
        val url = makeAbsoluteUrl(siteUrl, href)
        httpClient.get(url).use {
            return it.bodyString()?.extractFromManifestJson(url) ?: emptyList()
        }
    }

    private fun String.extractFromManifestJson(baseUrl: String): List<Icon> {
        val result: MutableList<Icon> = mutableListOf()
        try {
            val json = JSONObject(this)
            val icons = json.getJSONArray("icons")
            for (i in 0 until icons.length()) {
                try {
                    result.add(createIcon(baseUrl, icons.getJSONObject(i)))
                } catch (ignored: Exception) {
                }
            }
        } catch (ignored: Exception) {
        }
        return result
    }

    private fun createIcon(baseUrl: String, icon: JSONObject): WebAppIcon {
        return WebAppIcon(
            makeAbsoluteUrl(baseUrl, icon.getString("src")),
            icon.optString("sizes"),
            icon.optString("type"),
            icon.optString("density")
        )
    }

    companion object {
        private const val DEFAULT_LIMIT_SIZE = 1024 * 64
    }
}
