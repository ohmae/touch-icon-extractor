/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.json

internal class JsonArray(
    private val delegate: List<Any?> = emptyList(),
) : Iterable<Any?> by delegate {
    override fun hashCode(): Int = delegate.hashCode()
    override fun equals(other: Any?): Boolean = delegate == other
    override fun toString(): String = delegate.toString()
}
