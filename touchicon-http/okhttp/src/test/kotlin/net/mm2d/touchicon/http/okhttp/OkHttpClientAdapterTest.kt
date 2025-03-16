/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
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
class OkHttpClientAdapterTest {
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
        val client = OkHttpClientAdapter(OkHttpClient())
        client.userAgent = userAgent
        client.head(server.url("favicon.ico").toString())
        server.shutdown()
        assertThat(recordedRequest?.getHeader("User-Agent")).isEqualTo(userAgent)
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
        val client = OkHttpClientAdapter(OkHttpClient())
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
        val client = OkHttpClientAdapter(OkHttpClient())
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
        val client = OkHttpClientAdapter(OkHttpClient())
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
        val client = OkHttpClientAdapter(OkHttpClient())
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
        val client = OkHttpClientAdapter(OkHttpClient())
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyString(10)).isEqualTo("1234567890")
        }
        client.get(server.url("favicon.ico").toString()).use {
            assertThat(it.bodyString()).isEqualTo("12345678901234567890")
        }
        server.shutdown()
    }
}
