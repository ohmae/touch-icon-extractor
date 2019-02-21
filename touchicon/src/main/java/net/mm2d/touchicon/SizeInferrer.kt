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

private val INVALID_VALUE = Point(-1, -1)

/**
 * Infer size from url.
 *
 * If x or y is -1, it means fail to infer.
 *
 * Extract size section from file name and parse it.
 * e.g. "apple-touch-icon-120x120-precomposed.png" -> (120,120)
 */
internal fun inferSizeFromUrl(url: String): Point {
    val startOfFileName = url.lastIndexOf('/')
    if (startOfFileName < 0 || startOfFileName >= url.length - 1) {
        return INVALID_VALUE
    }
    val fileName = url.substring(startOfFileName + 1)
    val matcher = Pattern.compile("\\d+x\\d+").matcher(fileName)
    if (!matcher.find()) {
        return INVALID_VALUE
    }
    return inferSizeFromSizes(matcher.group())
}

/**
 * Infer size from sizes attribute.
 *
 * If x or y is -1, it means fail to infer.
 *
 * Assumed format is (width)x(height). e.g. "80x80".
 */
internal fun inferSizeFromSizes(sizes: String): Point {
    val part = sizes.split('x')
    if (part.size == 2) {
        return Point(part[0].toIntOrNull() ?: -1, part[1].toIntOrNull() ?: -1)
    }
    return INVALID_VALUE
}
