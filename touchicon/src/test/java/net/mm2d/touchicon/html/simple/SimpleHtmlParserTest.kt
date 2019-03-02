/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.simple

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class SimpleHtmlParserTest {
    @Test
    fun extractLinkTags() {
        val links = SimpleHtmlParser().extractLinkTags(
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
        assertThat(links).hasSize(5)
        assertThat(links[0].attr("rel")).isEqualTo("icon")
        assertThat(links[0].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[0].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[1].attr("rel")).isEqualTo("shortcut icon")
        assertThat(links[1].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[1].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[2].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[2].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[2].attr("sizes")).isEqualTo("57x57")
        assertThat(links[3].attr("rel")).isEqualTo("apple-touch-icon-precomposed")
        assertThat(links[3].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[3].attr("sizes")).isEqualTo("57x57")
        assertThat(links[4].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[4].attr("href")).isEqualTo("//www.example.com/apple-touch-icon-57x57.png")
        assertThat(links[4].attr("sizes")).isEqualTo("57x57")
    }

    @Test
    fun extractLinkTags_invalid_html() {
        val links = SimpleHtmlParser().extractLinkTags(
            """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <<link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">>>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png"/>
            <!-- <link rel="icon"> -->
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png"" ""
            """.trimIndent()
        )
        assertThat(links).hasSize(5)
        assertThat(links[0].attr("rel")).isEqualTo("icon")
        assertThat(links[0].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[0].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[1].attr("rel")).isEqualTo("shortcut icon")
        assertThat(links[1].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[1].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[2].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[2].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[2].attr("sizes")).isEqualTo("57x57")
        assertThat(links[3].attr("rel")).isEqualTo("apple-touch-icon-precomposed")
        assertThat(links[3].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[3].attr("sizes")).isEqualTo("57x57")
        assertThat(links[4].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[4].attr("href")).isEqualTo("//www.example.com/apple-touch-icon-57x57.png")
        assertThat(links[4].attr("sizes")).isEqualTo("57x57")
    }

    @Test
    fun extractLinkTags_non_quote_attribute() {
        val links = SimpleHtmlParser().extractLinkTags(
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
        assertThat(links).hasSize(5)
        assertThat(links[0].attr("rel")).isEqualTo("icon")
        assertThat(links[0].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[0].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[1].attr("rel")).isEqualTo("shortcut_icon")
        assertThat(links[1].attr("href")).isEqualTo("/favicon.ico")
        assertThat(links[1].attr("type")).isEqualTo("image/vnd.microsoft.icon")
        assertThat(links[2].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[2].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[2].attr("sizes")).isEqualTo("57x57")
        assertThat(links[3].attr("rel")).isEqualTo("apple-touch-icon-precomposed")
        assertThat(links[3].attr("href")).isEqualTo("/apple-touch-icon-57x57.png")
        assertThat(links[3].attr("sizes")).isEqualTo("57x57")
        assertThat(links[4].attr("rel")).isEqualTo("apple-touch-icon")
        assertThat(links[4].attr("href")).isEqualTo("//www.example.com/apple-touch-icon-57x57.png")
        assertThat(links[4].attr("sizes")).isEqualTo("57x57")
    }
}

