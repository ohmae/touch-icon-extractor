/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlParser

/**
 * Supply HttpParser powered by Jsoup.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object JsoupHtmlParserFactory {
    fun create(): HtmlParser {
        return JsoupHtmlParser()
    }
}
