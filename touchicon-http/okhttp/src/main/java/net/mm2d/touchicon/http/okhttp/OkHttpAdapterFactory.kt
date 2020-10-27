/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.okhttp

import net.mm2d.touchicon.HttpClientAdapter
import okhttp3.OkHttpClient

/**
 * Supply HttpClientAdapter using OkHttp
 */
object OkHttpAdapterFactory {
    /**
     * Create HttpClientAdapter instance
     *
     * @return instance
     */
    fun create(client: OkHttpClient): HttpClientAdapter = OkHttpClientAdapter(client)
}
