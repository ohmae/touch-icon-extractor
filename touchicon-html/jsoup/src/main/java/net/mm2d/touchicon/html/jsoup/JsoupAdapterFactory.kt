/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import net.mm2d.touchicon.HtmlParserAdapter

/**
 * Supply HttpParser using Jsoup
 */
object JsoupAdapterFactory {
    /**
     * Create HttpParser instance
     *
     * @return instance
     */
    fun create(): HtmlParserAdapter = JsoupHtmlParserAdapter()
}
