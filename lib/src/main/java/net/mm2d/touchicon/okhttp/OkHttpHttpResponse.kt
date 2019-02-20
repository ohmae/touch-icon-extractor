/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.okhttp

import net.mm2d.touchicon.HttpResponse
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class OkHttpHttpResponse(
    private val response: Response
) : HttpResponse {

    override val isSuccess: Boolean
        get() = response.isSuccessful

    override fun header(name: String): String? {
        return response.header(name)
    }

    @Throws(IOException::class)
    override fun bodyString(limit: Int): String? {
        val body = response.body() ?: return null
        return if (limit <= 0) body.string() else body.fetchString(limit)
    }

    @Throws(IOException::class)
    override fun bodyBytes(limit: Int): ByteArray? {
        val body = response.body() ?: return null
        return if (limit <= 0) body.bytes() else body.fetchBytes(limit)
    }

    override fun close() {
        response.body()?.close()
    }

    private fun ResponseBody.fetchString(limit: Int): String {
        return String(fetchBytes(limit))
    }

    private fun ResponseBody.fetchBytes(limit: Int): ByteArray {
        val stream = byteStream()
        val output = ByteArrayOutputStream()
        var remain = limit
        while (true) {
            val byte = stream.read()
            if (byte < 0) {
                break
            }
            output.write(byte)
            if (--remain <= 0) {
                break
            }
        }
        return output.toByteArray()
    }
}
