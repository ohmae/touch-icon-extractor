/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.json

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class JsonObjectTest {
    @Test
    fun testHashCode() {
        val delegate: Map<String, Any> = mockk(relaxed = true)
        val jsonObject = JsonObject(delegate)
        jsonObject.hashCode()
        verify { delegate.hashCode() }
    }

    @Suppress("ReplaceCallWithBinaryOperator", "UnusedEquals")
    @Test
    fun testEquals() {
        val delegate: Map<String, Any> = mockk(relaxed = true)
        val jsonObject = JsonObject(delegate)
        jsonObject.equals(mockk())
        jsonObject.equals(mockk<JsonObject>())
        verify { delegate.equals(any()) }
    }

    @Test
    fun testToString() {
        val delegate: Map<String, Any> = mockk(relaxed = true)
        val jsonObject = JsonObject(delegate)
        jsonObject.toString()
        verify { delegate.toString() }
    }
}
