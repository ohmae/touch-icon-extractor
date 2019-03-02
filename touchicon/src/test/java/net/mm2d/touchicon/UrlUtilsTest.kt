/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class UrlUtilsTest {
    @Test
    fun makeAbsoluteUrl() {
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "/icon.png"))
            .isEqualTo("https://www.example.com/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "icon.png"))
            .isEqualTo("https://www.example.com/foo/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html?a=b&c=d", "icon.png"))
            .isEqualTo("https://www.example.com/foo/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html#index", "icon.png"))
            .isEqualTo("https://www.example.com/foo/icon.png")
        assertThat(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html", "//www.example.net/icon.png")
        ).isEqualTo("https://www.example.net/icon.png")
        assertThat(
            makeAbsoluteUrl("http://www.example.com/foo/bar.html", "//www.example.net/icon.png")
        ).isEqualTo("http://www.example.net/icon.png")
    }

    @Test
    fun normalizePath() {
        assertThat("/".normalizePath()).isEqualTo("/")
        assertThat("./".normalizePath()).isEqualTo("/")
        assertThat("a".normalizePath()).isEqualTo("/a")
        assertThat("a/b".normalizePath()).isEqualTo("/a/b")
        assertThat("/a/b".normalizePath()).isEqualTo("/a/b")
        assertThat("/a/b/".normalizePath()).isEqualTo("/a/b/")
        assertThat("/a/b/./".normalizePath()).isEqualTo("/a/b/")
        assertThat("/a/b/./c".normalizePath()).isEqualTo("/a/b/c")
        assertThat("/a/b/c/../".normalizePath()).isEqualTo("/a/b/")
        assertThat("/a/b/c/../d".normalizePath()).isEqualTo("/a/b/d")
        assertThat("/a/b/c/./../d".normalizePath()).isEqualTo("/a/b/d")
        assertThat("/a/b/c/../../d".normalizePath()).isEqualTo("/a/d")
        assertThat("/a/../b/c/../d".normalizePath()).isEqualTo("/b/d")
    }
}
