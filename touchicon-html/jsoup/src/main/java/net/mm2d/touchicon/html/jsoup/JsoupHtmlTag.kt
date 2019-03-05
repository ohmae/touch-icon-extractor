/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlTag
import org.jsoup.nodes.Element

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class JsoupHtmlTag(
    private val element: Element
) : HtmlTag {
    override fun attr(name: String): String {
        return element.attr(name)
    }

    override fun toString(): String {
        return "JsoupHtmlTag(name=${element.tagName()}, attrs=[" +
                element.attributes().joinToString { "(${it.key}, ${it.value})" } + "])"
    }
}
