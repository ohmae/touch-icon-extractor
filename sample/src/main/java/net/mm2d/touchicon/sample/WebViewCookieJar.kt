/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object WebViewCookieJar : CookieJar {
    private val cookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()
        cookies.forEach {
            cookieManager.setCookie(urlString, it.toString())
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookie = cookieManager.getCookie(url.toString())
        if (cookie.isNullOrEmpty()) {
            return emptyList()
        }
        return cookie.split(";")
                .filter { it.isNotBlank() }
                .mapNotNull { Cookie.parse(url, it) }
    }
}
