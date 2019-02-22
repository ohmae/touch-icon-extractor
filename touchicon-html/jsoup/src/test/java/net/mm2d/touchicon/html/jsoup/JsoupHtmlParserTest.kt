/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class JsoupHtmlParserTest {
    @Test
    fun extractLinkTags() {
        val links = JsoupHtmlParser().extractLinkTags(
            """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon"/>
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon" />
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            <link
                rel="apple-touch-icon"
                sizes="57x57"
                href="//www.example.com/apple-touch-icon-57x57.png"
                >
            """.trimIndent()
        )
        assertEquals(links.size, 5)
        assertEquals(links[0].attr("rel"), "icon")
        assertEquals(links[0].attr("href"), "/favicon.ico")
        assertEquals(links[0].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[1].attr("rel"), "shortcut icon")
        assertEquals(links[1].attr("href"), "/favicon.ico")
        assertEquals(links[1].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[2].attr("rel"), "apple-touch-icon")
        assertEquals(links[2].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[2].attr("sizes"), "57x57")
        assertEquals(links[3].attr("rel"), "apple-touch-icon-precomposed")
        assertEquals(links[3].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[3].attr("sizes"), "57x57")
        assertEquals(links[4].attr("rel"), "apple-touch-icon")
        assertEquals(links[4].attr("href"), "//www.example.com/apple-touch-icon-57x57.png")
        assertEquals(links[4].attr("sizes"), "57x57")
    }

    @Test
    fun extractLinkTags_invalid_html() {
        val links = JsoupHtmlParser().extractLinkTags(
            """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <<link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">>>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png"/>
            <!-- <link rel="icon"> -->
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png">
            """.trimIndent()
        )
        assertEquals(links.size, 5)
        assertEquals(links[0].attr("rel"), "icon")
        assertEquals(links[0].attr("href"), "/favicon.ico")
        assertEquals(links[0].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[1].attr("rel"), "shortcut icon")
        assertEquals(links[1].attr("href"), "/favicon.ico")
        assertEquals(links[1].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[2].attr("rel"), "apple-touch-icon")
        assertEquals(links[2].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[2].attr("sizes"), "57x57")
        assertEquals(links[3].attr("rel"), "apple-touch-icon-precomposed")
        assertEquals(links[3].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[3].attr("sizes"), "57x57")
        assertEquals(links[4].attr("rel"), "apple-touch-icon")
        assertEquals(links[4].attr("href"), "//www.example.com/apple-touch-icon-57x57.png")
        assertEquals(links[4].attr("sizes"), "57x57")
    }

    @Test
    fun extractLinkTags_non_quote_attribute() {
        val links = JsoupHtmlParser().extractLinkTags(
            """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <link rel=icon href=/favicon.ico type=image/vnd.microsoft.icon>
            <link rel=shortcut_icon href=/favicon.ico type=image/vnd.microsoft.icon>
            <link rel=apple-touch-icon sizes=57x57 href=/apple-touch-icon-57x57.png>
            <link rel=apple-touch-icon-precomposed sizes=57x57 href=/apple-touch-icon-57x57.png>
            <link rel=apple-touch-icon sizes=57x57 href=//www.example.com/apple-touch-icon-57x57.png>
            """.trimIndent()
        )
        assertEquals(links.size, 5)
        assertEquals(links[0].attr("rel"), "icon")
        assertEquals(links[0].attr("href"), "/favicon.ico")
        assertEquals(links[0].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[1].attr("rel"), "shortcut_icon")
        assertEquals(links[1].attr("href"), "/favicon.ico")
        assertEquals(links[1].attr("type"), "image/vnd.microsoft.icon")
        assertEquals(links[2].attr("rel"), "apple-touch-icon")
        assertEquals(links[2].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[2].attr("sizes"), "57x57")
        assertEquals(links[3].attr("rel"), "apple-touch-icon-precomposed")
        assertEquals(links[3].attr("href"), "/apple-touch-icon-57x57.png")
        assertEquals(links[3].attr("sizes"), "57x57")
        assertEquals(links[4].attr("rel"), "apple-touch-icon")
        assertEquals(links[4].attr("href"), "//www.example.com/apple-touch-icon-57x57.png")
        assertEquals(links[4].attr("sizes"), "57x57")
    }
}