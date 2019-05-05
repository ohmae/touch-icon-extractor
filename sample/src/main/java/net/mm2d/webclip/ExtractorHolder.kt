/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip

import net.mm2d.touchicon.TouchIconExtractor
import net.mm2d.touchicon.html.jsoup.JsoupAdapterFactory
import net.mm2d.touchicon.http.okhttp.OkHttpAdapterFactory
import net.mm2d.touchicon.http.simple.SimpleHttpClientAdapterFactory

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object ExtractorHolder {
    val local = TouchIconExtractor(
        httpClient = SimpleHttpClientAdapterFactory.create(WebViewCookieHandler)
    )
    val library = TouchIconExtractor(
        httpClient = OkHttpAdapterFactory.create(OkHttpClientHolder.client),
        htmlParser = JsoupAdapterFactory.create()
    )
}
