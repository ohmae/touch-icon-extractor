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
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        if (limit <= 0) {
            while (true) {
                val size = stream.read(buffer, 0, BUFFER_SIZE)
                if (size < 0) break
                output.write(buffer, 0, size)
            }
        } else {
            var remain = limit
            while (true) {
                val fetchSize = if (remain > BUFFER_SIZE) BUFFER_SIZE else remain
                val size = stream.read(buffer, 0, fetchSize)
                if (size < 0) break
                output.write(buffer, 0, size)
                remain -= size
                if (remain <= 0) break
            }
        }
        return output.toByteArray()
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}
