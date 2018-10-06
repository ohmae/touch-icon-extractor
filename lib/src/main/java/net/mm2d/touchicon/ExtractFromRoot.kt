/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.net.Uri

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ExtractFromRoot(private val extractor: TouchIconExtractor) {
    private fun makeBaseBuilder(siteUrl: String) = Uri.parse(siteUrl)
            .buildUpon()
            .path(null)
            .fragment(null)
            .clearQuery()

    fun list(siteUrl: String, withPrecomposed: Boolean, sizes: List<String>): List<RootIcon> {
        val base = makeBaseBuilder(siteUrl)
        return createTryDataList(withPrecomposed, sizes)
                .mapNotNull {
                    try {
                        tryFetch(base, it)
                    } catch (e: Exception) {
                        null
                    }
                }
    }

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

    private fun tryFetch(baseUri: Uri.Builder, tryData: TryData): RootIcon? {
        val url = baseUri.path(tryData.name).build().toString()
        val response = extractor.executeHead(url)
        try {
            if (!response.isSuccessful) return null
            val type = response.header("Content-Type") ?: ""
            val length = response.header("Content-Length")?.toIntOrNull() ?: -1
            return RootIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
        } finally {
            response.body()?.close()
        }
    }

    private data class TryData(
            val rel: Rel,
            val name: String,
            val sizes: String,
            val precomposed: Boolean
    )

    companion object {
        const val FAVICON_ICO = "favicon.ico"
        const val APPLE_TOUCH_ICON = "apple-touch-icon"
        const val PNG = "png"
        const val PRECOMPOSED = "precomposed"
    }
}
