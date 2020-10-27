/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import net.mm2d.touchicon.HttpResponse
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.min

internal class OkHttpResponse(
    private val response: Response
) : HttpResponse {
    override val isSuccess: Boolean
        get() = response.isSuccessful

    override fun header(name: String): String? = response.header(name)

    @Throws(IOException::class)
    override fun bodyString(limit: Int): String? = response.body()?.let { body ->
        if (limit <= 0) body.string() else body.fetchString(limit)
    }

    @Throws(IOException::class)
    override fun bodyBytes(limit: Int): ByteArray? = response.body()?.let { body ->
        if (limit <= 0) body.bytes() else body.fetchBytes(limit)
    }

    override fun close() {
        response.body()?.close()
    }

    private fun ResponseBody.fetchString(limit: Int): String = String(fetchBytes(limit))

    private fun ResponseBody.fetchBytes(limit: Int): ByteArray {
        val stream = byteStream()
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var remain = limit
        while (true) {
            val fetchSize = min(buffer.size, remain)
            val size = stream.read(buffer, 0, fetchSize)
            if (size < 0) break
            output.write(buffer, 0, size)
            remain -= size
            if (remain <= 0) break
        }
        return output.toByteArray()
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}
