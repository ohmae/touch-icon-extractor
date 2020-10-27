/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.simple

import net.mm2d.touchicon.HtmlParserAdapter

/**
 * Supply default HttpParser implementation.
 */
object SimpleHtmlParserAdapterFactory {
    /**
     * Create HttpParser instance
     *
     * @return instance
     */
    fun create(): HtmlParserAdapter = SimpleHtmlParserAdapter()
}
