/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.url

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class UrlHttpClientTest {
    @Test
    fun userAgent() {
        var recordedRequest: RecordedRequest? = null
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200)
            }
        })
        server.start()
        val userAgent = "user-agent"
        val client = UrlHttpClient()
        client.userAgent = userAgent
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertEquals(recordedRequest?.getHeader("User-Agent"), userAgent)
    }

    @Test
    fun headers() {
        var recordedRequest: RecordedRequest? = null
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200)
            }
        })
        server.start()
        val userAgent = "user-agent"
        val client = UrlHttpClient()
        client.headers = mapOf("User-Agent" to "hogehoge", "Cache-Control" to "no-cache")
        client.userAgent = userAgent
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertEquals(recordedRequest?.getHeader("User-Agent"), userAgent)
        assertEquals(recordedRequest?.getHeader("Cache-Control"), "no-cache")
    }

    @Test
    fun head() {
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (request?.method != "HEAD") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val client = UrlHttpClient()
        client.head(server.url("favicon.ico").toString()).use {
            assertTrue(it.isSuccess)
            assertEquals(it.header("Content-Type"), "image/png")
        }
        assertFalse(client.get(server.url("favicon.ico").toString()).isSuccess)
        server.shutdown()
    }

    @Test
    fun get() {
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (request?.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val client = UrlHttpClient()
        client.get(server.url("favicon.ico").toString()).use {
            assertTrue(it.isSuccess)
            assertEquals(it.header("Content-Type"), "image/png")
        }
        assertFalse(client.head(server.url("favicon.ico").toString()).isSuccess)
        server.shutdown()
    }

    @Test
    fun get_limit_bytes() {
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (request?.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(Buffer().readFrom(ByteArrayInputStream(ByteArray(1024))))
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val client = UrlHttpClient()
        client.get(server.url("favicon.ico").toString()).use {
            assertEquals(it.bodyBytes(100)?.size, 100)
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertEquals(it.bodyBytes(2048)?.size, 1024)
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertEquals(it.bodyBytes()?.size, 1024)
        }
        server.shutdown()
    }

    @Test
    fun get_limit_string() {
        val server = MockWebServer()
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (request?.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(Buffer().readFrom(ByteArrayInputStream("12345678901234567890".toByteArray())))
                    else -> MockResponse().setResponseCode(404)
                }
            }
        })
        server.start()
        val client = UrlHttpClient()
        client.get(server.url("favicon.ico").toString()).use {
            assertEquals(it.bodyString(10), "1234567890")
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertEquals(it.bodyString(), "12345678901234567890")
        }
        server.shutdown()
    }
}
