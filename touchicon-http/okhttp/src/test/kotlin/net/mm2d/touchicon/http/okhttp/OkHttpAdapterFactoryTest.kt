/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OkHttpAdapterFactoryTest {
    @Test
    fun create() {
        assertThat(OkHttpAdapterFactory.create(OkHttpClient()))
            .isInstanceOf(OkHttpClientAdapter::class.java)
    }
}
