/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point
import java.util.regex.Pattern

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface Icon {
    val rel: Rel
    val url: String
    val sizes: String
    val mimeType: String
    val precomposed: Boolean
    val length: Int
    fun inferSize(): Point
}

internal fun inferSizeFromUrl(url: String): Point {
    val fileName = url.substring(url.lastIndexOf('/'))
    val matcher = Pattern.compile("\\d+x\\d+").matcher(fileName)
    if (!matcher.find()) {
        return Point(0, 0)
    }
    return inferSizeFromSizes(matcher.group())
}

internal fun inferSizeFromSizes(sizes: String): Point {
    val part = sizes.split('x')
    if (part.size == 2) {
        return Point(part[0].toIntOrNull() ?: 0, part[1].toIntOrNull() ?: 0)
    }
    return Point(0, 0)
}
