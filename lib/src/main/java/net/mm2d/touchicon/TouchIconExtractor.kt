/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.net.Uri
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class TouchIconExtractor(private val client: OkHttpClient) {
    private val fromHtml = ExtractFromHtml(this)
    var userAgent: String = ""
    var headers: Map<String, String> = emptyMap()
    var downloadLimit: Int
        get() = fromHtml.downloadLimit
        set(value) {
            fromHtml.downloadLimit = value
        }

    internal fun executeHead(url: String): Response {
        val request = Request.Builder()
                .head()
                .url(url)
                .appendHeader()
                .build()
        return client.newCall(request).execute()
    }

    internal fun executeGet(url: String): Response {
        val request = Request.Builder()
                .get()
                .url(url)
                .appendHeader()
                .build()
        return client.newCall(request).execute()
    }

    private fun Request.Builder.appendHeader(): Request.Builder {
        if (userAgent.isNotEmpty()) {
            header("User-Agent", userAgent)
        }
        if (headers.isNotEmpty()) {
            headers(Headers.of(headers))
        }
        return this
    }

    fun fromHtml(siteUrl: String): List<LinkIcon> {
        return fromHtml.invoke(siteUrl)
    }

    data class TryData(
            val rel: Rel,
            val name: String,
            val sizes: String,
            val precomposed: Boolean
    )

    fun extractFromRoot(siteUrl: String, withPrecomposed: Boolean = true, sizes: List<String> = emptyList()): List<RootIcon> {
        val base = Uri.parse(siteUrl)
                .buildUpon()
                .path(null)
                .fragment(null)
                .clearQuery()
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
        val request = Request.Builder()
                .head()
                .url(url)
                .appendHeader()
                .build()
        val response = executeHead(url)
        try {
            if (!response.isSuccessful) return null
            val type = response.header("Content-Type") ?: ""
            val length = response.header("Content-Length")?.toIntOrNull() ?: -1
            return RootIcon(tryData.rel, url, tryData.sizes, type, tryData.precomposed, length)
        } finally {
            response.body()?.close()
        }
    }

    companion object {
        const val FAVICON_ICO = "favicon.ico"
        const val APPLE_TOUCH_ICON = "apple-touch-icon"
        const val PNG = "png"
        const val PRECOMPOSED = "precomposed"
    }
}
