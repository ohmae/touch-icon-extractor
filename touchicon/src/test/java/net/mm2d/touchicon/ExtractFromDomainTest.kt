/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Suppress("TestFunctionName")
class ExtractFromDomainTest {
    @Test
    fun invoke_success_precomposed() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClientAdapter>(relaxed = true)
        every {
            httpClient.head("$baseUrl/apple-touch-icon-precomposed.png")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/png"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.fromDomain("$baseUrl/index.html", true, emptyList())!!
        assertThat(icon.mimeType).isEqualTo("image/png")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 1) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/apple-touch-icon-precomposed.png") }
    }

    @Test
    fun invoke_success_touch_icon() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClientAdapter>(relaxed = true)
        every {
            httpClient.head("$baseUrl/apple-touch-icon.png")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/png"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.fromDomain("$baseUrl/index.html", true, emptyList())!!
        assertThat(icon.mimeType).isEqualTo("image/png")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 2) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/apple-touch-icon.png") }
    }

    @Test
    fun invoke_success_favicon() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClientAdapter>(relaxed = true)
        every {
            httpClient.head("$baseUrl/favicon.ico")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/x-icon"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.fromDomain("$baseUrl/index.html", true, emptyList())!!
        assertThat(icon.mimeType).isEqualTo("image/x-icon")
        verify(inverse = true) { httpClient.get(any()) }
        verify(exactly = 3) { httpClient.head(any()) }
        verify(exactly = 1) { httpClient.head("$baseUrl/favicon.ico") }
    }

    @Test
    fun invoke_success_favicon_with_download() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClientAdapter>(relaxed = true)
        every {
            httpClient.get("$baseUrl/favicon.ico")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/x-icon"
            every { bodyBytes() } returns byteArrayOf(0)
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.fromDomainWithDownload("$baseUrl/index.html", true, emptyList())!!
        assertThat(icon.first.mimeType).isEqualTo("image/x-icon")
    }

    @Test
    fun invoke_success_favicon_list() {
        val baseUrl = "https://www.example.com"
        val httpClient = mockk<HttpClientAdapter>(relaxed = true)
        every {
            httpClient.head("$baseUrl/favicon.ico")
        } returns mockk(relaxed = true) {
            every { isSuccess } returns true
            every { header("Content-Type") } returns "image/x-icon"
        }
        val extract = ExtractFromDomain(httpClient)
        val icon = extract.listFromDomain("$baseUrl/index.html", true, emptyList())
        assertThat(icon).isNotEmpty()
        assertThat(icon[0].mimeType).isEqualTo("image/x-icon")
    }

    @Test
    fun createTryDataList_full() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, listOf("120x120", "80x80"))
        assertThat(list).hasSize(7)
        var i = 0
        assertThat(list[i].name).isEqualTo("apple-touch-icon-120x120-precomposed.png")
        assertThat(list[i].precomposed).isTrue()
        assertThat(list[i].sizes).isEqualTo("120x120")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-120x120.png")
        assertThat(list[i].precomposed).isFalse()
        assertThat(list[i].sizes).isEqualTo("120x120")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-80x80-precomposed.png")
        assertThat(list[i].precomposed).isTrue()
        assertThat(list[i].sizes).isEqualTo("80x80")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-80x80.png")
        assertThat(list[i].precomposed).isFalse()
        assertThat(list[i].sizes).isEqualTo("80x80")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-precomposed.png")
        assertThat(list[i].precomposed).isTrue()
        assertThat(list[i].sizes).isEqualTo("")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon.png")
        assertThat(list[i].precomposed).isFalse()
        assertThat(list[i].sizes).isEqualTo("")
        assertThat(list[i].rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        i++
        assertThat(list[i].name).isEqualTo("favicon.ico")
        assertThat(list[i].precomposed).isFalse()
        assertThat(list[i].sizes).isEqualTo("")
        assertThat(list[i].rel).isEqualTo(Relationship.ICON)
    }

    @Test
    fun createTryDataList_size_1_with_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, listOf("80x80"))
        assertThat(list).hasSize(5)
        var i = 0
        assertThat(list[i].name).isEqualTo("apple-touch-icon-80x80-precomposed.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-80x80.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-precomposed.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon.png")
        i++
        assertThat(list[i].name).isEqualTo("favicon.ico")
    }

    @Test
    fun createTryDataList_size_2_without_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(false, listOf("120x120", "80x80"))
        assertThat(list).hasSize(4)
        var i = 0
        assertThat(list[i].name).isEqualTo("apple-touch-icon-120x120.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon-80x80.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon.png")
        i++
        assertThat(list[i].name).isEqualTo("favicon.ico")
    }

    @Test
    fun createTryDataList_size_empty_with_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(true, emptyList())
        assertThat(list).hasSize(3)
        var i = 0
        assertThat(list[i].name).isEqualTo("apple-touch-icon-precomposed.png")
        i++
        assertThat(list[i].name).isEqualTo("apple-touch-icon.png")
        i++
        assertThat(list[i].name).isEqualTo("favicon.ico")
    }

    @Test
    fun createTryDataList_size_empty_without_precomposed() {
        val extract = ExtractFromDomain(mockk())
        val list = extract.createTryDataList(false, emptyList())
        assertThat(list).hasSize(2)
        var i = 0
        assertThat(list[i].name).isEqualTo("apple-touch-icon.png")
        i++
        assertThat(list[i].name).isEqualTo("favicon.ico")
    }
}
