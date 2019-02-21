/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

import net.mm2d.touchicon.HttpAdapter

/**
 * Supply default HttpAdapter implementation.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object SimpleHttpAdapterFactory {
    fun create(): HttpAdapter {
        return SimpleHttpAdapter()
    }
}
