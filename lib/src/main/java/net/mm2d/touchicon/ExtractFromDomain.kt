/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.net.Uri
import okhttp3.Response

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromDomain(private val extractor: TouchIconExtractor) {
    private fun makeBaseBuilder(siteUrl: String) = Uri.parse(siteUrl)
            .buildUpon()
            .path(null)
            .fragment(null)
            .clearQuery()

    fun invoke(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): DomainIcon? {
        val base = makeBaseBuilder(siteUrl)
        createTryDataList(withPrecomposed, sizes).forEach {
            try {
                val icon = tryHead(base, it)
                if (icon != null) return icon
            } catch (e: Exception) {
            }
        }
        return null
    }

    fun invokeWithDownload(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): Pair<DomainIcon, ByteArray>? {
        val base = makeBaseBuilder(siteUrl)
        createTryDataList(withPrecomposed, sizes).forEach {
            try {
                val icon = tryGet(base, it)
                if (icon != null) return icon
            } catch (e: Exception) {
            }
        }
        return null
    }

    fun list(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): List<DomainIcon> {
        val base = makeBaseBuilder(siteUrl)
        return createTryDataList(withPrecomposed, sizes)
                .mapNotNull {
                    try {
                        tryHead(base, it)
                    } catch (e: Exception) {
                        null
                    }
                }
    }

    private fun tryHead(baseUri: Uri.Builder, tryData: TryData): DomainIcon? {
        val url = makeUrl(baseUri, tryData)
        val response = extractor.executeHead(url)
        try {
            return createDomainIcon(response, url, tryData)
        } finally {
            response.body()?.close()
        }
    }

    private fun tryGet(baseUri: Uri.Builder, tryData: TryData): Pair<DomainIcon, ByteArray>? {
        val url = makeUrl(baseUri, tryData)
        val response = extractor.executeGet(url)
        response.body()?.use {
            val icon = createDomainIcon(response, url, tryData) ?: return null
            return icon to it.bytes()
        }
        return null
    }

    private fun makeUrl(baseUri: Uri.Builder, tryData: TryData): String {
        return baseUri.path(tryData.name).build().toString()
    }

    private fun createDomainIcon(response: Response, url: String, tryData: TryData): DomainIcon? {
        if (!response.isSuccessful) return null
        val type = response.header("Content-Type") ?: return null
        if (!type.contains("image", true)) return null
        val length = response.header("Content-Length")?.toIntOrNull() ?: -1
        return DomainIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
    }

    private data class TryData(
            val rel: Relationship,
            val name: String,
            val sizes: String,
            val precomposed: Boolean
    )

    private fun createTryDataList(withPrecomposed: Boolean, sizes: List<String>): List<TryData> {
        val result: MutableList<TryData> = mutableListOf()
        sizes.forEach {
            if (withPrecomposed) {
                result.add(TryData(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED, "$APPLE_TOUCH_ICON-$it-$PRECOMPOSED.$PNG", it, true))
            }
            result.add(TryData(Relationship.APPLE_TOUCH_ICON, "$APPLE_TOUCH_ICON-$it.$PNG", it, false))
        }
        if (withPrecomposed) {
            result.add(TryData(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED, "$APPLE_TOUCH_ICON-$PRECOMPOSED.$PNG", "", true))
        }
        result.add(TryData(Relationship.APPLE_TOUCH_ICON, "$APPLE_TOUCH_ICON.$PNG", "", false))
        result.add(TryData(Relationship.ICON, FAVICON_ICO, "", false))
        return result
    }

    companion object {
        private const val FAVICON_ICO = "favicon.ico"
        private const val APPLE_TOUCH_ICON = "apple-touch-icon"
        private const val PNG = "png"
        private const val PRECOMPOSED = "precomposed"
    }
}
