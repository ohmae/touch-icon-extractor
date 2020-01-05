/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class JsoupAdapterFactoryTest {
    @Test
    fun create() {
        Truth.assertThat(JsoupAdapterFactory.create())
            .isInstanceOf(JsoupHtmlParserAdapter::class.java)
    }
}
