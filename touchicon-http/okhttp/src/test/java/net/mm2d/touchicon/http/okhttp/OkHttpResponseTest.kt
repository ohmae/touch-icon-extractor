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
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class OkHttpResponseTest {
    @Test
    fun `bodyString Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns null
        val okHttpResponse = OkHttpResponse(response)
        assertThat(okHttpResponse.bodyString()).isNull()
    }

    @Test
    fun `bodyBytes Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns null
        val okHttpResponse = OkHttpResponse(response)
        assertThat(okHttpResponse.bodyBytes()).isNull()
    }

    @Test
    fun `close Don't crash even if body is null`() {
        val response: Response = mockk()
        every { response.body } returns null
        val okHttpResponse = OkHttpResponse(response)
        okHttpResponse.close()
    }
}
