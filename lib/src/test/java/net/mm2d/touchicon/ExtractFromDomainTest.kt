/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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
        assertThat(icon?.mimeType, `is`("image/png"))
        assertThat(icon?.url, `is`(server.url("apple-touch-icon-precomposed.png").toString()))
        assertThat(count, `is`(1))
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
        assertThat(icon?.mimeType, `is`("image/png"))
        assertThat(icon?.url, `is`(server.url("apple-touch-icon.png").toString()))
        assertThat(count, `is`(2))
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
        assertThat(icon?.mimeType, `is`("image/x-icon"))
        assertThat(icon?.url, `is`(server.url("favicon.ico").toString()))
        assertThat(count, `is`(3))
    }

    @Test
    fun createTryDataList_full() {
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
    fun createTryDataList_size_1_with_precomposed() {
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
    fun createTryDataList_size_2_without_precomposed() {
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
    fun createTryDataList_size_empty_with_precomposed() {
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
    fun createTryDataList_size_empty_without_precomposed() {
        val extract = ExtractFromDomain(Mockito.mock(HttpClientWrapper::class.java))
        val list = extract.createTryDataList(false, emptyList())
        assertThat(list.size, `is`(2))
        var i = 0
        assertThat(list[i].name, `is`("apple-touch-icon.png"))
        i++
        assertThat(list[i].name, `is`("favicon.ico"))
    }
}
