/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point
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
class IconTest {
    @Test
    fun inferSizeFromUrl() {
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon-48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon_48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon2-48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-iconx2-48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.48x48.png"
            ),
            `is`(Point(48, 48))
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.4Bx48.png"
            ),
            `is`(Point(0, 0))
        )
    }

    @Test
    fun inferSizeFromSizes() {
        assertThat(inferSizeFromSizes("48x48"), `is`(Point(48, 48)))
        assertThat(inferSizeFromSizes("1024x1024"), `is`(Point(1024, 1024)))
        assertThat(inferSizeFromSizes("0x1B"), `is`(Point(0, 0)))
    }
}
