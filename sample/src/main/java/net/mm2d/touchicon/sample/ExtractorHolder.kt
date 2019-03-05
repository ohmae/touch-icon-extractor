/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import net.mm2d.touchicon.TouchIconExtractor
import net.mm2d.touchicon.html.jsoup.JsoupHtmlParserFactory
import net.mm2d.touchicon.http.okhttp.OkHttpAdapterFactory

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object ExtractorHolder {
    val local = TouchIconExtractor()
    val library = TouchIconExtractor(
        httpAdapter = OkHttpAdapterFactory.create(OkHttpClientHolder.client),
        htmlParser = JsoupHtmlParserFactory.create()
    )
}