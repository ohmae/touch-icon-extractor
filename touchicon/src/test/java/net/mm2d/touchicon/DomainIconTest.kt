/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
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
class DomainIconTest {
    @Test
    fun inferSize() {
        assertThat(
            DomainIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon-40x40.png",
                precomposed = true,
                length = 0
            ).inferSize()
        ).isEqualTo(Size(50, 50))
    }

    @Test
    fun parcelable() {
        val icon = DomainIcon(
            rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
            mimeType = "image/png",
            sizes = "50x50",
            url = "http://example.com/icon-40x40.png",
            precomposed = true,
            length = 0
        )

        val restoredIcon = ParcelableTestUtil.saveAndRestore(icon)
        assertThat(restoredIcon).isEqualTo(icon)
        assertThat(icon.describeContents()).isEqualTo(0)
        assertThat(DomainIcon.newArray(1)).hasLength(1)
    }
}
