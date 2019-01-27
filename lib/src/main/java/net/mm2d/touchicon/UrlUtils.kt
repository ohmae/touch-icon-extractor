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
    val ignore = BooleanArray(sections.size)
    for (i in 0 until sections.size) {
        if (sections[i] != "..") continue
        ignore[i] = true
        var index = i - 1
        while (index >= 0) {
            if (!ignore[index]) {
                ignore[index] = true
                break
            }
            index--
        }
        if (index < 0) {
            return this
        }
    }
    val list = sections
        .asSequence()
        .withIndex()
        .filter { !ignore[it.index] }
        .toList()
    if (list.isEmpty()) return "/"
    val suffix = if (last() == '/') "/" else ""
    return list.joinToString("/", "/", suffix) { it.value }
}
