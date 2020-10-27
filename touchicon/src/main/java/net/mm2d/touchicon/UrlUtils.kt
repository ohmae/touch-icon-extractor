/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.net.URL

internal fun makeAbsoluteUrl(baseUrl: String, url: String): String =
    URL(baseUrl).let {
        if (url.startsWith("//")) {
            it.protocol + ":" + url
        } else {
            URL(it, url).toString()
        }
    }
