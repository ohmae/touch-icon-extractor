/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OkHttpResponseTest {
    @Test
    fun `bodyString Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns "".toResponseBody()
        val okHttpResponse = OkHttpResponse(response)
        assertThat(okHttpResponse.bodyString()).isEmpty()
    }

    @Test
    fun `bodyBytes Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns "".toResponseBody()
        val okHttpResponse = OkHttpResponse(response)
        assertThat(okHttpResponse.bodyBytes()).isEmpty()
    }

    @Test
    fun `close Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns "".toResponseBody()
        val okHttpResponse = OkHttpResponse(response)
        okHttpResponse.close()
    }
}
