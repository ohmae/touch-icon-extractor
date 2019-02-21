/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import net.mm2d.touchicon.HttpAdapter
import okhttp3.OkHttpClient

/**
 * Supply HttpAdapter powered by OkHttp
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object OkHttpAdapterFactory {
    fun create(client: OkHttpClient): HttpAdapter {
        return OkHttpAdapter(client)
    }
}
