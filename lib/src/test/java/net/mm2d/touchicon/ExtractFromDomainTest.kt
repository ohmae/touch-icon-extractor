/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class ExtractFromDomainTest {
    @Test
    fun invoke_success_precomposed() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClient>(relaxed = true)
        every {
            httpClient.head("$baseUrl/apple-touch-icon-precomposed.png")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/png"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.invoke("$baseUrl/index.html", true, emptyList())
        assertEquals(icon?.mimeType, "image/png")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 1) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/apple-touch-icon-precomposed.png") }
    }

    @Test
    fun invoke_success_touch_icon() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClient>(relaxed = true)
        every {
            httpClient.head("$baseUrl/apple-touch-icon.png")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/png"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.invoke("$baseUrl/index.html", true, emptyList())
        assertEquals(icon?.mimeType, "image/png")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 2) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/apple-touch-icon.png") }
    }

    @Test
    fun invoke_success_favicon() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClient>(relaxed = true)
        every {
            httpClient.head("$baseUrl/favicon.ico")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/x-icon"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.invoke("$baseUrl/index.html", true, emptyList())
        assertEquals(icon?.mimeType, "image/x-icon")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 3) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/favicon.ico") }
    }

    @Test
    fun createTryDataList_full() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, listOf("120x120", "80x80"))
        assertEquals(list.size, 7)
        var i = 0
        assertEquals(list[i].name, "apple-touch-icon-120x120-precomposed.png")
        assertEquals(list[i].precomposed, true)
        assertEquals(list[i].sizes, "120x120")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertEquals(list[i].name, "apple-touch-icon-120x120.png")
        assertEquals(list[i].precomposed, false)
        assertEquals(list[i].sizes, "120x120")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON)
        i++
        assertEquals(list[i].name, "apple-touch-icon-80x80-precomposed.png")
        assertEquals(list[i].precomposed, true)
        assertEquals(list[i].sizes, "80x80")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertEquals(list[i].name, "apple-touch-icon-80x80.png")
        assertEquals(list[i].precomposed, false)
        assertEquals(list[i].sizes, "80x80")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON)
        i++
        assertEquals(list[i].name, "apple-touch-icon-precomposed.png")
        assertEquals(list[i].precomposed, true)
        assertEquals(list[i].sizes, "")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertEquals(list[i].name, "apple-touch-icon.png")
        assertEquals(list[i].precomposed, false)
        assertEquals(list[i].sizes, "")
        assertEquals(list[i].rel, Relationship.APPLE_TOUCH_ICON)
        i++
        assertEquals(list[i].name, "favicon.ico")
        assertEquals(list[i].precomposed, false)
        assertEquals(list[i].sizes, "")
        assertEquals(list[i].rel, Relationship.ICON)
    }

    @Test
    fun createTryDataList_size_1_with_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, listOf("80x80"))
        assertEquals(list.size, 5)
        var i = 0
        assertEquals(list[i].name, "apple-touch-icon-80x80-precomposed.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon-80x80.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon-precomposed.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon.png")
        i++
        assertEquals(list[i].name, "favicon.ico")
    }

    @Test
    fun createTryDataList_size_2_without_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(false, listOf("120x120", "80x80"))
        assertEquals(list.size, 4)
        var i = 0
        assertEquals(list[i].name, "apple-touch-icon-120x120.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon-80x80.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon.png")
        i++
        assertEquals(list[i].name, "favicon.ico")
    }

    @Test
    fun createTryDataList_size_empty_with_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, emptyList())
        assertEquals(list.size, 3)
        var i = 0
        assertEquals(list[i].name, "apple-touch-icon-precomposed.png")
        i++
        assertEquals(list[i].name, "apple-touch-icon.png")
        i++
        assertEquals(list[i].name, "favicon.ico")
    }

    @Test
    fun createTryDataList_size_empty_without_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(false, emptyList())
        assertEquals(list.size, 2)
        var i = 0
        assertEquals(list[i].name, "apple-touch-icon.png")
        i++
        assertEquals(list[i].name, "favicon.ico")
    }
}
