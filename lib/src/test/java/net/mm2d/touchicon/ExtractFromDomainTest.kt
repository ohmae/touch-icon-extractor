/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(RobolectricTestRunner::class)
@Config(shadows = [NetworkSecurityPolicyShadow::class])
@Suppress("TestFunctionName")
class ExtractFromDomainTest {
    @Test
    fun invoke_success_precomposed() {
        var count = 0
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                count++
                if (request?.method != "HEAD") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/apple-touch-icon-precomposed.png" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val extract = ExtractFromDomain(HttpClientWrapper(OkHttpClient()))
        val icon = extract.invoke(server.url("index.html").toString(), true, emptyList())
        server.shutdown()
        assertEquals(icon?.mimeType, "image/png")
        assertEquals(icon?.url, server.url("apple-touch-icon-precomposed.png").toString())
        assertEquals(count, 1)
    }

    @Test
    fun invoke_success_touch_icon() {
        var count = 0
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                count++
                if (request?.method != "HEAD") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/apple-touch-icon.png" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val extract = ExtractFromDomain(HttpClientWrapper(OkHttpClient()))
        val icon = extract.invoke(server.url("index.html").toString(), true, emptyList())
        server.shutdown()
        assertEquals(icon?.mimeType, "image/png")
        assertEquals(icon?.url, server.url("apple-touch-icon.png").toString())
        assertEquals(count, 2)
    }

    @Test
    fun invoke_success_favicon() {
        var count = 0
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                count++
                if (request?.method != "HEAD") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/x-icon")
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val extract = ExtractFromDomain(HttpClientWrapper(OkHttpClient()))
        val icon = extract.invoke(server.url("index.html").toString(), true, emptyList())
        server.shutdown()
        assertEquals(icon?.mimeType, "image/x-icon")
        assertEquals(icon?.url, server.url("favicon.ico").toString())
        assertEquals(count, 3)
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
