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

class JsonArrayTest {
    @Test
    fun testHashCode() {
        val delegate: List<Any> = mockk(relaxed = true)
        val jsonArray = JsonArray(delegate)
        jsonArray.hashCode()
        verify { delegate.hashCode() }
    }

    @Suppress("ReplaceCallWithBinaryOperator", "UnusedEquals")
    @Test
    fun testEquals() {
        val delegate: List<Any> = mockk(relaxed = true)
        val jsonArray = JsonArray(delegate)
        jsonArray.equals(mockk())
        jsonArray.equals(mockk<JsonArray>())
        verify { delegate.equals(any()) }
    }

    @Test
    fun testToString() {
        val delegate: List<Any> = mockk(relaxed = true)
        val jsonArray = JsonArray(delegate)
        jsonArray.toString()
        verify { delegate.toString() }
    }
}
