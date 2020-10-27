/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.io.Closeable
import java.io.IOException

/**
 * Interface of HTTP response
 */
interface HttpResponse : Closeable {
    /**
     * true means success response
     */
    val isSuccess: Boolean

    /**
     * Get the response header
     *
     * @param name header name
     * @return header value, null if does not exist
     */
    fun header(name: String): String?

    /**
     * Read body as string
     *
     * @param limit download limit size, if <= 0 no limit
     * @return download result
     */
    @Throws(IOException::class)
    fun bodyString(limit: Int = 0): String?

    /**
     * Read body as byteArray
     *
     * @param limit download limit size, if <= 0 no limit
     * @return download result
     */
    @Throws(IOException::class)
    fun bodyBytes(limit: Int = 0): ByteArray?
}
