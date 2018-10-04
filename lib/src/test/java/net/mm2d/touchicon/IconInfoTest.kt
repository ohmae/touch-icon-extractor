/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

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
class IconInfoTest {
    @Test
    fun inferSizeFromUrl() {
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon-48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon_48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon2-48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-iconx2-48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.48x48.png"),
                `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.4Bx48.png"),
                `is`(0 to 0))
    }

    @Test
    fun inferSizeFromSizes() {
        assertThat(IconInfo.inferSizeFromSizes("48x48"), `is`(48 to 48))
        assertThat(IconInfo.inferSizeFromSizes("1024x1024"), `is`(1024 to 1024))
        assertThat(IconInfo.inferSizeFromSizes("0x1B"), `is`(0 to 0))
    }
}