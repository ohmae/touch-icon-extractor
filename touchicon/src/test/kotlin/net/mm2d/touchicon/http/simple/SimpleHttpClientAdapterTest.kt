/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayInputStream

@RunWith(JUnit4::class)
class SimpleHttpClientAdapterTest {
    @Test
    fun userAgent() {
        var recordedRequest: RecordedRequest? = null
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200)
            }
        }
        server.start()
        val userAgent = "user-agent"
        val client = SimpleHttpClientAdapter()
        client.userAgent = userAgent
        val response = client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertThat(recordedRequest?.getHeader("User-Agent")).isEqualTo(userAgent)
        assertThat(response.isSuccess).isTrue()
    }

    @Test
    fun headers() {
        var recordedRequest: RecordedRequest? = null
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200)
            }
        }
        server.start()
        val userAgent = "user-agent"
        val client = SimpleHttpClientAdapter()
        client.headers = mapOf("User-Agent" to "hogehoge", "Cache-Control" to "no-cache")
        client.userAgent = userAgent
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertThat(recordedRequest?.getHeader("User-Agent")).isEqualTo(userAgent)
        assertThat(recordedRequest?.getHeader("Cache-Control")).isEqualTo("no-cache")
    }

    @Test
    fun head() {
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                if (request.method != "HEAD") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        server.start()
        val client = SimpleHttpClientAdapter()
        client.head(server.url("favicon.ico").toString()).use {
            assertThat(it.isSuccess).isTrue()
            assertThat(it.header("Content-Type")).isEqualTo("image/png")
        }
        assertThat(client.get(server.url("favicon.ico").toString()).isSuccess).isFalse()
        server.shutdown()
    }

    @Test
    fun get() {
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                if (request.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "image/png")

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        server.start()
        val client = SimpleHttpClientAdapter()
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.isSuccess).isTrue()
            assertThat(it.header("Content-Type")).isEqualTo("image/png")
        }
        assertThat(client.head(server.url("favicon.ico").toString()).isSuccess).isFalse()
        server.shutdown()
    }

    @Test
    fun get_limit_bytes() {
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                if (request.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(Buffer().readFrom(ByteArrayInputStream(ByteArray(1024))))

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        server.start()
        val client = SimpleHttpClientAdapter()
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyBytes(100)).hasLength(100)
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyBytes(2048)).hasLength(1024)
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyBytes()).hasLength(1024)
        }
        server.shutdown()
    }

    @Test
    fun get_limit_string() {
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                if (request.method != "GET") {
                    return MockResponse().setResponseCode(404)
                }
                return when (request.path) {
                    "/favicon.ico" -> MockResponse()
                        .setResponseCode(200)
                        .setBody(Buffer().readFrom(ByteArrayInputStream("12345678901234567890".toByteArray())))

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        server.start()
        val client = SimpleHttpClientAdapter()
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyString(10)).isEqualTo("1234567890")
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyString()).isEqualTo("12345678901234567890")
        }
        server.shutdown()
    }

    @Test
    fun setCookie() {
        var recordedRequest: RecordedRequest? = null
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200)
            }
        }
        server.start()
        val cookie = "name=value"
        val cookieHandler: CookieHandler = mockk()
        every { cookieHandler.loadCookie(any()) } returns cookie
        val client = SimpleHttpClientAdapter(cookieHandler)
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertThat(recordedRequest?.getHeader("Cookie")).isEqualTo(cookie)
    }

    @Test
    fun getCookie() {
        val cookie1 = "name=value; Max-Age=1000"
        val cookie2 = "name=value; HttpOnly"
        val server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(
                request: RecordedRequest,
            ): MockResponse =
                MockResponse().setResponseCode(200)
                    .addHeader("Set-Cookie", cookie1)
                    .addHeader("Set-Cookie", cookie2)
        }
        server.start()
        val cookieHandler: CookieHandler = mockk()
        every { cookieHandler.loadCookie(any()) } returns null
        val slot = mutableListOf<String>()
        every { cookieHandler.saveCookie(any(), capture(slot)) } answers { nothing }
        val client = SimpleHttpClientAdapter(cookieHandler)
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertThat(slot).contains(cookie1)
        assertThat(slot).contains(cookie2)
    }
}
