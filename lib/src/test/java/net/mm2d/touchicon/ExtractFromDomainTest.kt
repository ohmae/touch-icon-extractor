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
import org.mockito.Mockito

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class ExtractFromDomainTest {
    @Test
    fun createTryDataList() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(true, listOf("120x120", "80x80"))
        assertThat(list.size, `is`(7))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon-120x120-precomposed.png"))
        assertThat(list[i].precomposed, `is`(true))
        assertThat(list[i].sizes, `is`("120x120"))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-120x120.png"))
        assertThat(list[i].precomposed, `is`(false))
        assertThat(list[i].sizes, `is`("120x120"))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-80x80-precomposed.png"))
        assertThat(list[i].precomposed, `is`(true))
        assertThat(list[i].sizes, `is`("80x80"))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-80x80.png"))
        assertThat(list[i].precomposed, `is`(false))
        assertThat(list[i].sizes, `is`("80x80"))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-precomposed.png"))
        assertThat(list[i].precomposed, `is`(true))
        assertThat(list[i].sizes, `is`(""))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        assertThat(list[i].precomposed, `is`(false))
        assertThat(list[i].sizes, `is`(""))
        assertThat(list[i].rel, `is`(Relationship.APPLE_TOUCH_ICON))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
        assertThat(list[i].precomposed, `is`(false))
        assertThat(list[i].sizes, `is`(""))
        assertThat(list[i].rel, `is`(Relationship.ICON))
    }

    @Test
    fun createTryDataList1() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(true, listOf("80x80"))
        assertThat(list.size, `is`(5))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon-80x80-precomposed.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-80x80.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-precomposed.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
    }

    @Test
    fun createTryDataList2() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(false, listOf("120x120", "80x80"))
        assertThat(list.size, `is`(4))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon-120x120.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon-80x80.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
    }

    @Test
    fun createTryDataList3() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(true, emptyList())
        assertThat(list.size, `is`(3))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon-precomposed.png"))
        i++
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
    }

    @Test
    fun createTryDataList4() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(false, emptyList())
        assertThat(list.size, `is`(2))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
    }
}
