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
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class WebAppIconTest {
    @Test
    fun inferSize() {
        assertThat(
            WebAppIcon(
                mimeType = "image/png",
                sizes = "50x50",
                url = "http://example.com/icon-40x40.png",
                density = "3.0"
            ).inferSize()
        ).isEqualTo(Size(50, 50))
    }
}

