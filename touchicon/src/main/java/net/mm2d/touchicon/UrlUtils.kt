/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.net.Uri
import android.webkit.URLUtil
import androidx.annotation.VisibleForTesting

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal fun makeAbsoluteUrl(baseUrl: String, url: String): String {
    return when {
        URLUtil.isNetworkUrl(url) -> url
        url.startsWith("//") -> (Uri.parse(baseUrl).scheme ?: "https") + ":" + url
        else -> Uri.parse(baseUrl)
            .buildUpon()
            .clearQuery()
            .fragment(null)
            .path(makePath(baseUrl, url).normalizePath())
            .build()
            .toString()
    }
}

private fun makePath(baseUrl: String, url: String): String {
    if (url.startsWith("/")) {
        return url
    }
    val basePath = Uri.parse(baseUrl).path ?: return url
    if (basePath.endsWith("/")) {
        return basePath + url
    }
    val pos = basePath.lastIndexOf('/')
    return if (pos > 0) {
        basePath.substring(0, pos + 1) + url
    } else url
}

@VisibleForTesting
internal fun String.normalizePath(): String {
    val sections = split('/')
        .filter { it.isNotEmpty() && it != "." }
        .toMutableList()
    val iterator = sections.listIterator()
    while (iterator.hasNext()) {
        if (iterator.next() != "..") continue
        iterator.remove()
        if (!iterator.hasPrevious()) throw IllegalArgumentException("path traversal error: $this")
        iterator.previous()
        iterator.remove()
    }
    if (sections.isEmpty()) return "/"
    val suffix = if (last() == '/') "/" else ""
    return sections.joinToString("/", "/", suffix)
}
