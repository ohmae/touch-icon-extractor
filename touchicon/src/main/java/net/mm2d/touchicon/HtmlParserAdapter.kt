/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Html Parser Interface.
 */
interface HtmlParserAdapter {
    /**
     * Extract the link tag information.
     *
     * @return link tags
     */
    fun extractLinkTags(html: String): List<HtmlTag>
}
