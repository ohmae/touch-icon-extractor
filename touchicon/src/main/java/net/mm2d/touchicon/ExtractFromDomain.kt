/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.net.URL

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromDomain(
    private val httpClient: HttpClientAdapter
) {
    internal fun fromDomain(
        siteUrl: String,
        withPrecomposed: Boolean,
        sizes: List<String>
    ): DomainIcon? = createTryDataList(withPrecomposed, sizes)
        .asSequence()
        .mapNotNull { tryHead(siteUrl, it) }
        .firstOrNull()

    internal fun fromDomainWithDownload(
        siteUrl: String,
        withPrecomposed: Boolean,
        sizes: List<String>
    ): Pair<DomainIcon, ByteArray>? = createTryDataList(withPrecomposed, sizes)
        .asSequence()
        .mapNotNull { tryGet(siteUrl, it) }
        .firstOrNull()

    internal fun listFromDomain(
        siteUrl: String,
        withPrecomposed: Boolean,
        sizes: List<String>
    ): List<DomainIcon> = createTryDataList(withPrecomposed, sizes)
        .mapNotNull { tryHead(siteUrl, it) }

    private fun tryHead(baseUrl: String, tryData: TryData): DomainIcon? =
        runCatching {
            val url = makeUrl(baseUrl, tryData)
            httpClient.head(url).use { createDomainIcon(it, url, tryData) }
        }.getOrNull()

    private fun tryGet(baseUrl: String, tryData: TryData): Pair<DomainIcon, ByteArray>? =
        runCatching {
            val url = makeUrl(baseUrl, tryData)
            httpClient.get(url).use {
                val icon = createDomainIcon(it, url, tryData)
                val bytes = it.bodyBytes()
                if (icon != null && bytes != null) icon to bytes else null
            }
        }.getOrNull()

    private fun makeUrl(baseUrl: String, tryData: TryData): String =
        URL(URL(baseUrl), "/" + tryData.name).toString()

    private fun createDomainIcon(
        response: HttpResponse,
        url: String,
        tryData: TryData
    ): DomainIcon? {
        if (!response.isSuccess) return null
        val type = response.header("Content-Type") ?: return null
        if (!type.contains("image", true)) return null
        val length = response.header("Content-Length")?.toIntOrNull() ?: -1
        return DomainIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
    }

    // VisibleForTesting
    internal data class TryData(
        val rel: Relationship,
        val name: String,
        val sizes: String,
        val precomposed: Boolean
    )

    // VisibleForTesting
    internal fun createTryDataList(withPrecomposed: Boolean, sizes: List<String>): List<TryData> {
        val result: MutableList<TryData> = mutableListOf()
        sizes.forEach {
            if (withPrecomposed) {
                result += TryData(
                    Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                    "$APPLE_TOUCH_ICON-$it-$PRECOMPOSED.$PNG",
                    it,
                    true
                )
            }
            result += TryData(
                Relationship.APPLE_TOUCH_ICON,
                "$APPLE_TOUCH_ICON-$it.$PNG",
                it,
                false
            )
        }
        if (withPrecomposed) {
            result += TryData(
                Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                "$APPLE_TOUCH_ICON-$PRECOMPOSED.$PNG",
                "",
                true
            )
        }
        result += TryData(Relationship.APPLE_TOUCH_ICON, "$APPLE_TOUCH_ICON.$PNG", "", false)
        result += TryData(Relationship.ICON, FAVICON_ICO, "", false)
        return result
    }

    companion object {
        private const val FAVICON_ICO = "favicon.ico"
        private const val APPLE_TOUCH_ICON = "apple-touch-icon"
        private const val PNG = "png"
        private const val PRECOMPOSED = "precomposed"
    }
}
