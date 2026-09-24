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

internal fun isSameOrigin(
    first: String,
    second: String,
): Boolean =
    runCatching {
        val a = URI(first)
        val b = URI(second)
        fun port(
            uri: URI,
        ): Int = uri.port.takeIf { it >= 0 } ?: if (uri.scheme.equals("https", true)) 443 else 80
        a.scheme.equals(b.scheme, true) &&
            (a.scheme.equals("http", true) || a.scheme.equals("https", true)) &&
            a.host != null && a.host.equals(b.host, true) &&
            a.userInfo == null && b.userInfo == null && port(a) == port(b)
    }.getOrDefault(false)
