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

    internal fun fromPage(siteUrl: String, withManifest: Boolean): List<Icon> {
        val html = runCatching { fetch(siteUrl) }.getOrNull()
        return if (html.isNullOrEmpty()) {
            emptyList()
        } else {
            extractFromHtml(siteUrl, html, withManifest)
        }
    }

    internal fun fromManifest(siteUrl: String): List<Icon> {
        val html = runCatching { fetch(siteUrl) }.getOrNull()
        return if (html.isNullOrEmpty()) {
            emptyList()
        } else {
            htmlParser.extractLinkTags(html)
                .filter { Relationship.of(it.attr("rel")) == Relationship.MANIFEST }
                .flatMap { extractFromManifest(siteUrl, it.attr("href")) ?: emptyList() }
        }
    }

    private fun fetch(url: String): String? = httpClient.get(url).use {
        if (it.hasHtml()) it.bodyString(downloadLimit) else null
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
        htmlParser.extractLinkTags(html).let { tags ->
            tags.mapNotNull { createPageIcon(siteUrl, it) } +
                tags.filter { Relationship.of(it.attr("rel")) == Relationship.MANIFEST }
                    .flatMap { extractFromManifest(siteUrl, it.attr("href")) }
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

    private fun extractFromManifest(siteUrl: String, href: String): List<Icon> {
        if (href.isEmpty()) return emptyList()
        val url = makeAbsoluteUrl(siteUrl, href)
        return runCatching {
            httpClient.get(url).use {
                it.bodyString()?.extractFromManifestJson(url)
            }
        }.getOrNull() ?: emptyList()
    }

    private fun String.extractFromManifestJson(baseUrl: String): List<Icon>? =
        runCatching {
            val icons = JSONObject(this).getJSONArray("icons")
            (0 until icons.length()).mapNotNull {
                runCatching { createWebAppIcon(baseUrl, icons.getJSONObject(it)) }.getOrNull()
            }
        }.getOrNull()

    private fun createWebAppIcon(baseUrl: String, icon: JSONObject): WebAppIcon =
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
