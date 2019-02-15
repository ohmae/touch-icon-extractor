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
class PageIconTest {
    @Test
    fun inferSize() {
        assertEquals(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon-40x40.png"
            ).inferSize(),
            Point(50, 50)
        )
        assertEquals(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon.png"
            ).inferSize(),
            Point(50, 50)
        )
        assertEquals(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "0x5C",
                url = "http://example.com/icon-40x40.png"
            ).inferSize(),
            Point(40, 40)
        )
        assertEquals(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "0x5C",
                url = "http://example.com/icon.png"
            ).inferSize(),
            Point(-1, -1)
        )
    }
}