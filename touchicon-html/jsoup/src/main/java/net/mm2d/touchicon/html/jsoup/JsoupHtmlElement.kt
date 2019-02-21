/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlElement
import org.jsoup.nodes.Element

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class JsoupHtmlElement(
    private val element: Element
) : HtmlElement {
    override fun attr(name: String): String {
        return element.attr(name)
    }
}
