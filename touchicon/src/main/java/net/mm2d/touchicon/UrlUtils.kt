/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.net.URL

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal fun makeAbsoluteUrl(baseUrl: String, url: String): String {
    val base = URL(if (baseUrl.startsWith("//")) "https:$baseUrl" else baseUrl)
    return if (url.startsWith("//")) base.protocol + ":" + url else URL(base, url).toString()
}
