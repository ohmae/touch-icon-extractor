/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import org.hamcrest.Matchers
import org.junit.Assert
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
        Assert.assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "/icon.png"),
                Matchers.`is`("https://www.example.com/icon.png"))
        Assert.assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "icon.png"),
                Matchers.`is`("https://www.example.com/foo/icon.png"))
        Assert.assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html?a=b&c=d", "icon.png"),
                Matchers.`is`("https://www.example.com/foo/icon.png"))
        Assert.assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html#index", "icon.png"),
                Matchers.`is`("https://www.example.com/foo/icon.png"))
        Assert.assertThat(makeAbsoluteUrl("https://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
                Matchers.`is`("https://www.example.net/icon.png"))
        Assert.assertThat(makeAbsoluteUrl("http://www.example.com/foo/bar.html", "//www.example.net/icon.png"),
                Matchers.`is`("http://www.example.net/icon.png"))
    }

    @Test
    fun normalizePath() {
        Assert.assertThat("/".normalizePath(), Matchers.`is`("/"))
        Assert.assertThat("./".normalizePath(), Matchers.`is`("/"))
        Assert.assertThat("a".normalizePath(), Matchers.`is`("/a"))
        Assert.assertThat("a/b".normalizePath(), Matchers.`is`("/a/b"))
        Assert.assertThat("/a/b".normalizePath(), Matchers.`is`("/a/b"))
        Assert.assertThat("/a/b/".normalizePath(), Matchers.`is`("/a/b/"))
        Assert.assertThat("/a/b/./".normalizePath(), Matchers.`is`("/a/b/"))
        Assert.assertThat("/a/b/./c".normalizePath(), Matchers.`is`("/a/b/c"))
        Assert.assertThat("/a/b/c/../".normalizePath(), Matchers.`is`("/a/b/"))
        Assert.assertThat("/a/b/c/../d".normalizePath(), Matchers.`is`("/a/b/d"))
        Assert.assertThat("/a/b/c/./../d".normalizePath(), Matchers.`is`("/a/b/d"))
        Assert.assertThat("/a/b/c/../../d".normalizePath(), Matchers.`is`("/a/d"))
        Assert.assertThat("/a/../b/c/../d".normalizePath(), Matchers.`is`("/b/d"))
    }
}
