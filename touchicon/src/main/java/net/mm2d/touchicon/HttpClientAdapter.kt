/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import java.io.IOException

/**
 * Interface of HTTP Client
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface HttpClientAdapter {
    /**
     * Specify the value of User-Agent used for HTTP communication.
     *
     * It takes precedence over specification in [headers].
     */
    var userAgent: String

    /**
     * Specify the HTTP communication header.
     *
     * User-Agent can also be specified, but [userAgent] takes precedence.
     */
    var headers: Map<String, String>

    /**
     * Execute HTTP HEAD method
     *
     * @param url access URL
     * @return Response
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun head(url: String): HttpResponse

    /**
     * Execute HTTP GET method
     *
     * @param url access URL
     * @return Response
     * @throws IOException if an I/O error occurs
     */
    @Throws(IOException::class)
    fun get(url: String): HttpResponse
}
