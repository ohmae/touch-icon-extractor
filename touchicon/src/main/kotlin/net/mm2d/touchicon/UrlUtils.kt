/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.net.URI

internal fun makeAbsoluteUrl(
    baseUrl: String,
    url: String,
): String =
    URI(baseUrl).let {
        if (url.startsWith("//")) {
            it.scheme + ":" + url
        } else {
            it.resolve(url).toString()
        }
    }
