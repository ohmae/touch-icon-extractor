/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection

class SimpleHttpResponseTest {
    @Test
    fun isSuccess() {
        val connection: HttpURLConnection = mockk()
        every { connection.inputStream } returns null
        every { connection.responseCode } returns 100
        assertThat(SimpleHttpResponse(connection).isSuccess).isFalse()
        every { connection.responseCode } returns 200
        assertThat(SimpleHttpResponse(connection).isSuccess).isTrue()
        every { connection.responseCode } returns 300
        assertThat(SimpleHttpResponse(connection).isSuccess).isFalse()
    }

    @Test
    fun `bodyString null if stream is null`() {
        val connection: HttpURLConnection = mockk()
        every { connection.inputStream } returns null
        every { connection.responseCode } returns 200
        assertThat(SimpleHttpResponse(connection).bodyString(1000)).isNull()
    }

    @Test
    fun `bodyBytes null if stream is null`() {
        val connection: HttpURLConnection = mockk()
        every { connection.inputStream } returns null
        every { connection.responseCode } returns 200
        assertThat(SimpleHttpResponse(connection).bodyBytes(1000)).isNull()
    }

    @Test
    fun `close do not crash even if stream is null`() {
        val connection: HttpURLConnection = mockk()
        every { connection.inputStream } returns null
        every { connection.responseCode } returns 200
        SimpleHttpResponse(connection).close()
    }

    @Test
    fun `close do not crash even if stream throws exception`() {
        val connection: HttpURLConnection = mockk()
        val inputStream: InputStream = mockk()
        every { inputStream.close() } throws IOException()
        every { connection.inputStream } returns inputStream
        every { connection.responseCode } returns 200
        SimpleHttpResponse(connection).close()
    }
}
