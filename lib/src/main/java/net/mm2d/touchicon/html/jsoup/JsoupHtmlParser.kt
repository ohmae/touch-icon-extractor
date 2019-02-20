/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlElement
import net.mm2d.touchicon.HtmlParser
import org.jsoup.Jsoup

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class JsoupHtmlParser : HtmlParser {
    override fun extractLinkElements(html: String): List<HtmlElement> {
        return Jsoup.parse(html).getElementsByTag("link")
            .map { JsoupHtmlElement(it) }
    }
}
