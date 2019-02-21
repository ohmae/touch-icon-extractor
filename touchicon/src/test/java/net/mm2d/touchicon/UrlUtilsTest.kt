/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class UrlUtilsTest {
    @Test
    fun makeAbsoluteUrl() {
        assertEquals(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html", "/icon.png"),
            "https://www.example.com/icon.png"
        )
        assertEquals(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html", "icon.png"),
            "https://www.example.com/foo/icon.png"
        )
        assertEquals(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html?a=b&c=d", "icon.png"),
            "https://www.example.com/foo/icon.png"
        )
        assertEquals(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html#index", "icon.png"),
            "https://www.example.com/foo/icon.png"
        )
        assertEquals(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
            "https://www.example.net/icon.png"
        )
        assertEquals(
            makeAbsoluteUrl("http://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
            "http://www.example.net/icon.png"
        )
    }

    @Test
    fun normalizePath() {
        assertEquals("/".normalizePath(), "/")
        assertEquals("./".normalizePath(), "/")
        assertEquals("a".normalizePath(), "/a")
        assertEquals("a/b".normalizePath(), "/a/b")
        assertEquals("/a/b".normalizePath(), "/a/b")
        assertEquals("/a/b/".normalizePath(), "/a/b/")
        assertEquals("/a/b/./".normalizePath(), "/a/b/")
        assertEquals("/a/b/./c".normalizePath(), "/a/b/c")
        assertEquals("/a/b/c/../".normalizePath(), "/a/b/")
        assertEquals("/a/b/c/../d".normalizePath(), "/a/b/d")
        assertEquals("/a/b/c/./../d".normalizePath(), "/a/b/d")
        assertEquals("/a/b/c/../../d".normalizePath(), "/a/d")
        assertEquals("/a/../b/c/../d".normalizePath(), "/b/d")
    }
}
