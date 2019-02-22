/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Represent HTML element.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface HtmlTag {
    /**
     * Return attribute value.
     *
     * @param name attribute name
     * @return attribute value, or empty string if not found
     */
    fun attr(name: String): String
}
