/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import io.mockk.mockk
import net.mm2d.touchicon.html.jsoup.JsoupHtmlParser
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class ExtractFromPageTest {
    @Test
    fun extract_icon() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.ICON)
        assertEquals(result.url, "https://www.example.com/favicon.ico")
        assertEquals(result.sizes, "")
        assertEquals(result.mimeType, "image/vnd.microsoft.icon")
    }

    @Test
    fun extract_shortcut_icon() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.SHORTCUT_ICON)
        assertEquals(result.url, "https://www.example.com/favicon.ico")
        assertEquals(result.sizes, "")
        assertEquals(result.mimeType, "image/vnd.microsoft.icon")
    }

    @Test
    fun extract_apple_touch_icon() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.APPLE_TOUCH_ICON)
        assertEquals(result.url, "https://www.example.com/apple-touch-icon-57x57.png")
        assertEquals(result.sizes, "57x57")
        assertEquals(result.mimeType, "")
    }

    @Test
    fun extract_apple_touch_icon_precomposed() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        assertEquals(result.url, "https://www.example.com/apple-touch-icon-57x57.png")
        assertEquals(result.sizes, "57x57")
        assertEquals(result.mimeType, "")
    }

    @Test
    fun extract_omitted_scheme() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.APPLE_TOUCH_ICON)
        assertEquals(result.url, "https://www.example.com/apple-touch-icon-57x57.png")
        assertEquals(result.sizes, "57x57")
        assertEquals(result.mimeType, "")
    }

    @Test
    fun extract_broken() {
        val extract = ExtractFromPage(mockk(), JsoupHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </hea
            """.trimIndent()
        )[0]
        assertEquals(result.rel, Relationship.APPLE_TOUCH_ICON)
        assertEquals(result.url, "https://www.example.com/apple-touch-icon-57x57.png")
        assertEquals(result.sizes, "57x57")
        assertEquals(result.mimeType, "")
    }
}
