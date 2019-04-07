/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import net.mm2d.touchicon.HttpResponse
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class SimpleHttpResponse(
    private val connection: HttpURLConnection
) : HttpResponse {
    override val isSuccess: Boolean = connection.responseCode in 200 until 300
    private val inputStream: InputStream? = try {
        connection.inputStream
    } catch (e: java.lang.Exception) {
        null
    }

    override fun header(name: String): String? {
        return connection.getHeaderField(name)
    }

    override fun bodyString(limit: Int): String? {
        val stream = inputStream ?: return null
        return String(fetchBytes(stream, limit))
    }

    override fun bodyBytes(limit: Int): ByteArray? {
        val stream = inputStream ?: return null
        return fetchBytes(stream, limit)
    }

    override fun close() {
        try {
            inputStream?.close()
        } catch (e: Exception) {
        }
    }

    private fun fetchBytes(stream: InputStream, limit: Int): ByteArray {
        if (limit <= 0) {
            return stream.readBytes()
        }
        val outputSize = maxOf(DEFAULT_BUFFER_SIZE, stream.available())
        val result = ByteArrayOutputStream(minOf(outputSize, limit))
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var remain = limit
        while (true) {
            val requestSize = minOf(buffer.size, remain)
            val size = stream.read(buffer, 0, requestSize)
            if (size < 0) break
            result.write(buffer, 0, size)
            remain -= size
            if (remain <= 0) break
        }
        return result.toByteArray()
    }
}
