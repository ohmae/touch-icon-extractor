/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import net.mm2d.touchicon.html.simple.SimpleHtmlParser
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class ExtractFromPageTest {
    @Test
    fun extract_icon() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_shortcut_icon() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.SHORTCUT_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_apple_touch_icon() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_apple_touch_icon_precomposed() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_omitted_scheme() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_broken() {
        val extract = ExtractFromPage(mockk(), SimpleHtmlParser())
        val result = extract.invoke(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </hea
            """.trimIndent()
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }
}
