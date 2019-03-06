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
class SimpleHtmlParserAdapterTest {
    private val expected = listOf(
        mapOf(
            "rel" to "icon",
            "href" to "/favicon.ico",
            "type" to "image/vnd.microsoft.icon"
        ),
        mapOf(
            "rel" to "shortcut icon",
            "href" to "/favicon.ico",
            "type" to "image/vnd.microsoft.icon"
        ),
        mapOf(
            "rel" to "apple-touch-icon",
            "href" to "/apple-touch-icon-57x57.png",
            "sizes" to "57x57"
        ),
        mapOf(
            "rel" to "apple-touch-icon-precomposed",
            "href" to "/apple-touch-icon-57x57.png",
            "sizes" to "57x57"
        ),
        mapOf(
            "rel" to "apple-touch-icon",
            "href" to "//www.example.com/apple-touch-icon-57x57.png",
            "sizes" to "57x57"
        )
    )

    @Test
    fun extractLinkTags() {
        val links = SimpleHtmlParserAdapterFactory.create().extractLinkTags(
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
        expected.forEachIndexed { index, map ->
            map.forEach { key, value ->
                assertThat(links[index].attr(key)).isEqualTo(value)
            }
        }
    }

    @Test
    fun extractLinkTags_invalid_html() {
        val links = SimpleHtmlParserAdapter().extractLinkTags(
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
        expected.forEachIndexed { index, map ->
            map.forEach { key, value ->
                assertThat(links[index].attr(key)).isEqualTo(value)
            }
        }
    }

    @Test
    fun extractLinkTags_non_quote_attribute() {
        val links = SimpleHtmlParserAdapter().extractLinkTags(
            """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <link rel=icon href=/favicon.ico type=image/vnd.microsoft.icon>
            <link rel=shortcut icon href=/favicon.ico type=image/vnd.microsoft.icon>
            <link rel=apple-touch-icon sizes=57x57 href=/apple-touch-icon-57x57.png>
            <link rel=apple-touch-icon-precomposed sizes=57x57 href=/apple-touch-icon-57x57.png>
            <link rel=apple-touch-icon sizes=57x57 href=//www.example.com/apple-touch-icon-57x57.png>
            """.trimIndent()
        )
        assertThat(links).hasSize(5)
        expected.forEachIndexed { index, map ->
            map.forEach { key, value ->
                if (index == 1 && key == "rel") {
                    assertThat(links[index].attr(key)).isEqualTo("shortcut")
                } else {
                    assertThat(links[index].attr(key)).isEqualTo(value)
                }
            }
        }
    }
}

