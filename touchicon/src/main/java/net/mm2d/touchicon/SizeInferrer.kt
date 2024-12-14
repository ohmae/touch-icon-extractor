/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.util.regex.Pattern

private val INVALID_VALUE = Size(-1, -1)

/**
 * Infer size from url.
 *
 * If x or y is -1, it means fail to infer.
 *
 * Extract size section from file name and parse it.
 * e.g. "apple-touch-icon-120x120-precomposed.png" -> (120,120)
 */
internal fun inferSizeFromUrl(
    url: String,
): Size {
    val fileName = url.substringAfterLast('/', "")
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
internal fun inferSizeFromSizes(
    sizes: String,
): Size {
    val part = sizes.split('x')
    return if (part.size == 2) {
        Size(part[0].toIntOrNull() ?: -1, part[1].toIntOrNull() ?: -1)
    } else {
        INVALID_VALUE
    }
}
