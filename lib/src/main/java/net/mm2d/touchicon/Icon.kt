/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point

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
