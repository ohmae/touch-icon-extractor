/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.jsoup

import com.google.common.truth.Truth.assertThat
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class JsoupHtmlTagTest {
    @Test
    fun `toString test`() {
        val attributes = Attributes().also { it.put("key", "value") }
        val element = Element(Tag.valueOf("test"), "", attributes)
        val tag = JsoupHtmlTag(element)
        assertThat(tag.toString()).isEqualTo("JsoupHtmlTag(name=test, attrs=[(key, value)])")
    }
}
