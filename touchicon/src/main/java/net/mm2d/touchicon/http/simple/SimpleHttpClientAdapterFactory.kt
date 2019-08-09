/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import net.mm2d.touchicon.HttpClientAdapter

/**
 * Supply default HttpClientAdapter implementation.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object SimpleHttpClientAdapterFactory {
    /**
     * Create HttpClientAdapter instance
     *
     * @return instance
     */
    fun create(cookieHandler: CookieHandler? = null): HttpClientAdapter =
        SimpleHttpClientAdapter(cookieHandler)
}
