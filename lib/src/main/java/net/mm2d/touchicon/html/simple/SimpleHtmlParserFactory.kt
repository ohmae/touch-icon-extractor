/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.simple

import net.mm2d.touchicon.HtmlParser

/**
 * Supply default HttpParser implementation.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object SimpleHtmlParserFactory {
    fun create(): HtmlParser {
        return SimpleHtmlParser()
    }
}
