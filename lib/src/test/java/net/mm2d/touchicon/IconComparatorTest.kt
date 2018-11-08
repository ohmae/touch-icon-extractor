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
class IconComparatorTest {
    @Test
    fun SIZE_REL() {
        val list = listOf(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "1",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "2",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON,
                url = "3",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON,
                url = "4",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.ICON,
                url = "5",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.ICON,
                url = "6",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.SHORTCUT_ICON,
                url = "7",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.SHORTCUT_ICON,
                url = "8",
                sizes = "40x40",
                mimeType = "image/png"
            )
        )
        val sorted = list.sortedWith(IconComparator.SIZE_REL)
        assertThat(sorted[0].url, `is`("8"))
        assertThat(sorted[1].url, `is`("6"))
        assertThat(sorted[2].url, `is`("4"))
        assertThat(sorted[3].url, `is`("2"))
        assertThat(sorted[4].url, `is`("7"))
        assertThat(sorted[5].url, `is`("5"))
        assertThat(sorted[6].url, `is`("3"))
        assertThat(sorted[7].url, `is`("1"))
        assertThat(list.maxWith(IconComparator.SIZE_REL)?.url, `is`("1"))
    }

    @Test
    fun REL_SIZE() {
        val list = listOf(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "1",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "2",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON,
                url = "3",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON,
                url = "4",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.ICON,
                url = "5",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.ICON,
                url = "6",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.SHORTCUT_ICON,
                url = "7",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.SHORTCUT_ICON,
                url = "8",
                sizes = "40x40",
                mimeType = "image/png"
            )
        )
        val sorted = list.sortedWith(IconComparator.REL_SIZE)
        assertThat(sorted[0].url, `is`("8"))
        assertThat(sorted[1].url, `is`("7"))
        assertThat(sorted[2].url, `is`("6"))
        assertThat(sorted[3].url, `is`("5"))
        assertThat(sorted[4].url, `is`("4"))
        assertThat(sorted[5].url, `is`("3"))
        assertThat(sorted[6].url, `is`("2"))
        assertThat(sorted[7].url, `is`("1"))
        assertThat(list.maxWith(IconComparator.SIZE_REL)?.url, `is`("1"))
    }

    @Test
    fun SIZE() {
        val list = listOf(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "1",
                sizes = "80x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "2",
                sizes = "20x80",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "3",
                sizes = "80x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "4",
                sizes = "80x",
                mimeType = "image/png"
            )
        )
        val sorted = list.sortedWith(IconComparator.SIZE_REL)
        assertThat(sorted[0].url, `is`("4"))
        assertThat(sorted[1].url, `is`("2"))
        assertThat(sorted[2].url, `is`("3"))
        assertThat(sorted[3].url, `is`("1"))
    }

    @Test
    fun REL() {
        val list = listOf(
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
                url = "1",
                sizes = "40x40",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.APPLE_TOUCH_ICON,
                url = "2",
                sizes = "50x50",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.ICON,
                url = "3",
                sizes = "60x60",
                mimeType = "image/png"
            ),
            PageIcon(
                rel = Relationship.SHORTCUT_ICON,
                url = "4",
                sizes = "70x70",
                mimeType = "image/png"
            )
        )
        val sorted1 = list.sortedWith(IconComparator.SIZE_REL)
        assertThat(sorted1[0].url, `is`("1"))
        assertThat(sorted1[1].url, `is`("2"))
        assertThat(sorted1[2].url, `is`("3"))
        assertThat(sorted1[3].url, `is`("4"))
        val sorted2 = list.sortedWith(IconComparator.REL_SIZE)
        assertThat(sorted2[0].url, `is`("4"))
        assertThat(sorted2[1].url, `is`("3"))
        assertThat(sorted2[2].url, `is`("2"))
        assertThat(sorted2[3].url, `is`("1"))
    }
}