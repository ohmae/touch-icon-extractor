/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import net.mm2d.touchicon.TouchIconExtractor
import net.mm2d.touchicon.http.okhttp.OkHttpHttpClient
import net.mm2d.touchicon.html.jsoup.JsoupHtmlParser

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object TouchIconExtractorHolder {
    val extractor = TouchIconExtractor(
        OkHttpHttpClient(OkHttpClientHolder.client),
        JsoupHtmlParser()
    )
}
