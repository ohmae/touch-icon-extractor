/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html

/**
 * Represent HTML element.
 */
internal class HtmlTag(
    val name: String,
    private val attrs: List<Pair<String, String>>
) {
    /**
     * Return attribute value.
     *
     * @param name attribute name
     * @return attribute value, or empty string if not found
     */
    fun attr(name: String): String {
        val attr = attrs.find { it.first.equals(name, true) } ?: return ""
        return attr.second
    }
}
