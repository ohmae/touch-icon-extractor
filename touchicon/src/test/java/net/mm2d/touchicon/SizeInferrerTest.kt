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
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SizeInferrerTest {
    @Test
    fun inferSizeFromUrl() {
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon-48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon_48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon2-48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-iconx2-48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.48x48.png"
            )
        ).isEqualTo(
            Size(48, 48)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/apple-touch-icon.4Bx48.png"
            )
        ).isEqualTo(
            Size(-1, -1)
        )
        assertThat(
            inferSizeFromUrl(
                "https://www.example.com/"
            )
        ).isEqualTo(
            Size(-1, -1)
        )
        assertThat(
            inferSizeFromUrl(
                ""
            )
        ).isEqualTo(
            Size(-1, -1)
        )
    }

    @Test
    fun inferSizeFromSizes() {
        assertThat(inferSizeFromSizes("48x48")).isEqualTo(Size(48, 48))
        assertThat(inferSizeFromSizes("1024x1024")).isEqualTo(Size(1024, 1024))
        assertThat(inferSizeFromSizes("A0x1B")).isEqualTo(Size(-1, -1))
        assertThat(inferSizeFromSizes("10-10")).isEqualTo(Size(-1, -1))
    }
}
