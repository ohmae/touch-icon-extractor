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
internal fun inferSizeFromUrl(url: String): Point {
    val fileName = url.substring(url.lastIndexOf('/'))
    val matcher = Pattern.compile("\\d+x\\d+").matcher(fileName)
    if (!matcher.find()) {
        return Point(-1, -1)
    }
    return inferSizeFromSizes(matcher.group())
}

internal fun inferSizeFromSizes(sizes: String): Point {
    val part = sizes.split('x')
    if (part.size == 2) {
        return Point(part[0].toIntOrNull() ?: -1, part[1].toIntOrNull() ?: -1)
    }
    return Point(-1, -1)
}
