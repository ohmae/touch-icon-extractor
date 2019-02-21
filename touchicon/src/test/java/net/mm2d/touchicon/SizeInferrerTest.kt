/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class SizeInferrerTest {
    @Test
    fun inferSizeFromUrl() {
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon-48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon_48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon2-48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-iconx2-48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.48x48.png"
            ),
            Point(48, 48)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.4Bx48.png"
            ),
            Point(-1, -1)
        )
        assertEquals(
            inferSizeFromUrl(
                "https://www.example.com/"
            ),
            Point(-1, -1)
        )
        assertEquals(
            inferSizeFromUrl(
                ""
            ),
            Point(-1, -1)
        )
    }

    @Test
    fun inferSizeFromSizes() {
        assertEquals(inferSizeFromSizes("48x48"), Point(48, 48))
        assertEquals(inferSizeFromSizes("1024x1024"), Point(1024, 1024))
        assertEquals(inferSizeFromSizes("A0x1B"), Point(-1, -1))
    }
}
