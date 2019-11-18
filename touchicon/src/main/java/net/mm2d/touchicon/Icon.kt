/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.os.Parcelable

/**
 * Icon information interface.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface Icon : Parcelable {
    /**
     * Relationship between icon and page.
     */
    val rel: Relationship
    /**
     * Icon url.
     */
    val url: String
    /**
     * Size information, assumed format is (width)x(height). e.g. "80x80".
     *
     * It exists only if a value is specified, often blank.
     */
    val sizes: String
    /**
     * Icon MIME type. e.g. "image/png"
     *
     * It exists only if a value is specified, often blank
     */
    val mimeType: String
    /**
     * true if this is for a precomposed touch icon.
     */
    val precomposed: Boolean
    /**
     * Icon file length.
     *
     * Negative value means unknown.
     */
    val length: Int

    /**
     * Infer display size of this icon.
     *
     * Infer based on [sizes] or [url].
     * If x or y is -1, it means fail to infer.
     *
     * @return Inferred size
     */
    fun inferSize(): Size

    /**
     * Infer area of this icon.
     *
     * @return area
     */
    fun inferArea(): Int = inferSize().let {
        if (it.isValid()) it.width * it.height else 0
    }
}
