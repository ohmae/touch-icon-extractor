/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip

import android.webkit.CookieManager
import net.mm2d.touchicon.http.simple.CookieHandler

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object WebViewCookieHandler : CookieHandler {
    private val cookieManager = CookieManager.getInstance()

    override fun saveCookie(url: String, value: String) {
        cookieManager.setCookie(url, value)
    }

    override fun loadCookie(url: String): String? = cookieManager.getCookie(url)
}
