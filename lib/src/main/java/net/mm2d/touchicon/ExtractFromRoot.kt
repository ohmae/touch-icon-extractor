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
internal class ExtractFromRoot(private val extractor: TouchIconExtractor) {
    private fun makeBaseBuilder(siteUrl: String) = Uri.parse(siteUrl)
            .buildUpon()
            .path(null)
            .fragment(null)
            .clearQuery()

    fun invoke(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): RootIcon? {
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

    fun invokeWithDownload(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): Pair<RootIcon, ByteArray>? {
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

    fun list(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): List<RootIcon> {
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

    private fun tryHead(baseUri: Uri.Builder, tryData: TryData): RootIcon? {
        val url = makeUrl(baseUri, tryData)
        val response = extractor.executeHead(url)
        try {
            return createRootIcon(response, url, tryData)
        } finally {
            response.body()?.close()
        }
    }

    private fun tryGet(baseUri: Uri.Builder, tryData: TryData): Pair<RootIcon, ByteArray>? {
        val url = makeUrl(baseUri, tryData)
        val response = extractor.executeGet(url)
        response.body()?.use {
            val icon = createRootIcon(response, url, tryData) ?: return null
            return icon to it.bytes()
        }
        return null
    }

    private fun makeUrl(baseUri: Uri.Builder, tryData: TryData): String {
        return baseUri.path(tryData.name).build().toString()
    }

    private fun createRootIcon(response: Response, url: String, tryData: TryData): RootIcon? {
        if (!response.isSuccessful) return null
        val type = response.header("Content-Type") ?: ""
        val length = response.header("Content-Length")?.toIntOrNull() ?: -1
        return RootIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
    }

    private data class TryData(
            val rel: Rel,
            val name: String,
            val sizes: String,
            val precomposed: Boolean
    )

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

    companion object {
        const val FAVICON_ICO = "favicon.ico"
        const val APPLE_TOUCH_ICON = "apple-touch-icon"
        const val PNG = "png"
        const val PRECOMPOSED = "precomposed"
    }
}
