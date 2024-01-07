/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.json

internal class JsonObject(
    private val delegate: Map<String, Any?> = emptyMap(),
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): T = delegate[name] as T

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(name: String, default: T, clazz: Class<*>): T =
        delegate[name].let { if (clazz.isInstance(it)) it as T else default }

    inline fun <reified T> getOrDefault(name: String, default: T): T =
        getOrDefault(name, default, T::class.java)

    override fun hashCode(): Int = delegate.hashCode()
    override fun equals(other: Any?): Boolean = delegate == other
    override fun toString(): String = delegate.toString()
}
