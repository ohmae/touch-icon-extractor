/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlParserAdapter
import net.mm2d.touchicon.HtmlTag
import org.jsoup.Jsoup

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class JsoupHtmlParserAdapter : HtmlParserAdapter {
    override fun extractLinkTags(html: String): List<HtmlTag> =
        Jsoup.parse(html).getElementsByTag("link")
            .map { JsoupHtmlTag(it) }
}
