/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.simple

import net.mm2d.touchicon.HtmlTag

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal data class SimpleHtmlTag(
    val name: String,
    private val attrs: List<Pair<String, String>>
) : HtmlTag {
    override fun attr(name: String): String {
        return attrs.find { it.first.equals(name, true) }?.second ?: ""
    }
}