/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.os.Bundle
import android.os.Parcel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class PageIconTest {
    @Test
    fun inferSize() {
        assertThat(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon-40x40.png"
            ).inferSize()
        ).isEqualTo(Size(50, 50))
        assertThat(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon.png"
            ).inferSize()
        ).isEqualTo(Size(50, 50))
        assertThat(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "0x5C",
                url = "http://example.com/icon-40x40.png"
            ).inferSize()
        ).isEqualTo(Size(40, 40))
        assertThat(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "0x5C",
                url = "http://example.com/icon.png"
            ).inferSize()
        ).isEqualTo(Size(-1, -1))
    }

    @Test
    fun parcelable() {
        val icon = PageIcon(
            rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
            mimeType = "image/png",
            sizes = "50x50",
            url = "http://example.com/icon-40x40.png"
        )
        val key = "key"
        val bundle1 = Bundle()
        val parcel = Parcel.obtain()
        try {
            bundle1.putParcelable(key, icon)
            parcel.writeBundle(bundle1)
            parcel.setDataPosition(0)
            val bundle2 = parcel.readBundle()!!
            val restoredIcon: PageIcon = bundle2.getParcelable(key)!!
            assertThat(icon).isEqualTo(restoredIcon)
        } finally {
            parcel.recycle()
        }
    }
}
