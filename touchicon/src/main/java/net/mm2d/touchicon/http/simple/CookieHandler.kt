/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http.simple

/**
 * Cookie Handler for SimpleHttpClientAdapter.
 */
interface CookieHandler {
    /**
     * Called when cookies are received.
     *
     * The value of "Set-Cookie" in the HTTP Response header is notified.
     * If there are multiple "Set-Cookie", it will be called each time.
     *
     * @param url URL
     * @param value cookie value
     */
    fun saveCookie(url: String, value: String)

    /**
     * Called to read cookie.
     *
     * The return value is set to "Cookie" property.
     *
     * @param url URL
     * @return Cookie
     */
    fun loadCookie(url: String): String?
}
