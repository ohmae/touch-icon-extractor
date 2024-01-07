/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Icon size.
 */
data class Size(
    /**
     * width.
     */
    val width: Int,
    /**
     * height.
     */
    val height: Int,
) {
    /**
     * Returns whether this has a valid value.
     *
     * @return true: this has valid value, false: otherwise
     */
    fun isValid(): Boolean = width > 0 && height > 0
}
