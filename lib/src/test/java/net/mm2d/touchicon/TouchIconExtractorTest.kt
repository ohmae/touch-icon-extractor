/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import okhttp3.OkHttpClient
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class TouchIconExtractorTest {
    @Test
    fun extract_icon() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
                    </head></html>
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.ICON))
        assertThat(result.url, `is`("https://www.example.com/favicon.ico"))
        assertThat(result.sizes, `is`(""))
        assertThat(result.type, `is`("image/vnd.microsoft.icon"))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun extract_shortcut_icon() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
                    </head></html>
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.SHORTCUT_ICON))
        assertThat(result.url, `is`("https://www.example.com/favicon.ico"))
        assertThat(result.sizes, `is`(""))
        assertThat(result.type, `is`("image/vnd.microsoft.icon"))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun extract_apple_touch_icon() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
                    </head></html>
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.APPLE_TOUCH_ICON))
        assertThat(result.url, `is`("https://www.example.com/apple-touch-icon-57x57.png"))
        assertThat(result.sizes, `is`("57x57"))
        assertThat(result.type, `is`(""))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun extract_apple_touch_icon_precomposed() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
                    </head></html>
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.APPLE_TOUCH_ICON_PRECOMPOSED))
        assertThat(result.url, `is`("https://www.example.com/apple-touch-icon-57x57.png"))
        assertThat(result.sizes, `is`("57x57"))
        assertThat(result.type, `is`(""))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun extract_omitted_scheme() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png">
                    </head></html>
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.APPLE_TOUCH_ICON))
        assertThat(result.url, `is`("https://www.example.com/apple-touch-icon-57x57.png"))
        assertThat(result.sizes, `is`("57x57"))
        assertThat(result.type, `is`(""))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun extract_broken() {
        val extractor = TouchIconExtractor(OkHttpClient())
        val result = extractor.extract("https://www.example.com/",
                """
                    <html><head>
                    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
                    </hea
                """.trimIndent()
        )[0]
        assertThat(result.rel, `is`(Rel.APPLE_TOUCH_ICON))
        assertThat(result.url, `is`("https://www.example.com/apple-touch-icon-57x57.png"))
        assertThat(result.sizes, `is`("57x57"))
        assertThat(result.type, `is`(""))
        assertThat(result.siteUrl, `is`("https://www.example.com/"))
    }

    @Test
    fun makeAbsoluteUrl() {
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "/icon.png"),
                `is`("https://www.example.com/icon.png"))
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "icon.png"),
                `is`("https://www.example.com/foo/icon.png"))
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html?a=b&c=d", "icon.png"),
                `is`("https://www.example.com/foo/icon.png"))
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html#index", "icon.png"),
                `is`("https://www.example.com/foo/icon.png"))
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
                `is`("https://www.example.net/icon.png"))
        assertThat(makeAbsoluteUrl("http://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
                `is`("http://www.example.net/icon.png"))
    }

    @Test
    fun normalize() {
        assertThat("/".normalize(), `is`("/"))
        assertThat("./".normalize(), `is`("/"))
        assertThat("a".normalize(), `is`("/a"))
        assertThat("a/b".normalize(), `is`("/a/b"))
        assertThat("/a/b".normalize(), `is`("/a/b"))
        assertThat("/a/b/".normalize(), `is`("/a/b/"))
        assertThat("/a/b/./".normalize(), `is`("/a/b/"))
        assertThat("/a/b/./c".normalize(), `is`("/a/b/c"))
        assertThat("/a/b/c/../".normalize(), `is`("/a/b/"))
        assertThat("/a/b/c/../d".normalize(), `is`("/a/b/d"))
        assertThat("/a/b/c/./../d".normalize(), `is`("/a/b/d"))
        assertThat("/a/b/c/../../d".normalize(), `is`("/a/d"))
        assertThat("/a/../b/c/../d".normalize(), `is`("/b/d"))
    }
}
