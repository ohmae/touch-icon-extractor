/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class SizeInferrerTest {
    @Test
    fun inferSizeFromUrl() {
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon-48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon_48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon2-48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-iconx2-48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.48x48.png"
            )
        ).isEqualTo(
            Point(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.4Bx48.png"
            )
        ).isEqualTo(
            Point(-1, -1)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/"
            )
        ).isEqualTo(
            Point(-1, -1)
        )
        assertThat(
            inferSizeFromUrl(
                ""
            )
        ).isEqualTo(
            Point(-1, -1)
        )
    }

    @Test
    fun inferSizeFromSizes() {
        assertThat(inferSizeFromSizes("48x48")).isEqualTo(Point(48, 48))
        assertThat(inferSizeFromSizes("1024x1024")).isEqualTo(Point(1024, 1024))
        assertThat(inferSizeFromSizes("A0x1B")).isEqualTo(Point(-1, -1))
    }
}
