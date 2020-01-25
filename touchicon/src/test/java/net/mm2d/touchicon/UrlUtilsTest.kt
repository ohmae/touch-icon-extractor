/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
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
            makeAbsoluteUrl("//www.example.com/foo/bar.html", "//www.example.net/icon.png")
        ).isEqualTo("https://www.example.net/icon.png")
        assertThat(
            makeAbsoluteUrl("http://www.example.com/foo/bar.html", "//www.example.net/icon.png")
        ).isEqualTo("http://www.example.net/icon.png")
        assertThat(
            makeAbsoluteUrl("https://www.example.com/foo/bar.html", "http://www.example.net/icon.png")
        ).isEqualTo("http://www.example.net/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com", "icon.png"))
            .isEqualTo("https://www.example.com/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/", "icon.png"))
            .isEqualTo("https://www.example.com/foo/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/", "./icon.png"))
            .isEqualTo("https://www.example.com/foo/icon.png")
        assertThat(makeAbsoluteUrl("https://www.example.com/foo/", "../icon.png"))
            .isEqualTo("https://www.example.com/icon.png")
    }
}
