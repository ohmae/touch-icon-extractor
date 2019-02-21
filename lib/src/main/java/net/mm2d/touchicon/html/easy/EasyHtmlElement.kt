/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.easy

import net.mm2d.touchicon.HtmlElement

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class EasyHtmlElement(
    val name: String,
    private val attrs: List<Pair<String, String>>
) : HtmlElement {
    override fun attr(name: String): String {
        return attrs.find { it.first.equals(name, true) }?.second ?: ""
    }
}
