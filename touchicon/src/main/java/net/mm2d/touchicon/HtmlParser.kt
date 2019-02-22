/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Html Parser Interface.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface HtmlParser {
    /**
     * Extract the link tag information.
     *
     * @return link tags
     */
    fun extractLinkTags(html: String): List<HtmlTag>
}
