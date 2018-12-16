/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.support.annotation.WorkerThread
import okhttp3.OkHttpClient

/**
 * Extract information of WebClip icon such as Apple Touch Icon or favicon related to the URL.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 *
 * @constructor
 * Initialize the instance.
 * It is necessary to specify an instance of OkHttpClient to be used for communication.
 *
 * @param client An instance of OkHttpClient to use for internal communication.
 */
class TouchIconExtractor(client: OkHttpClient) {
    private val http = HttpClientWrapper(client)
    private val fromPage = ExtractFromPage(http)
    private val fromDomain = ExtractFromDomain(http)
    /**
     * Specify the value of User-Agent used for HTTP communication.
     *
     * It takes precedence over specification in [headers].
     */
    var userAgent: String
        get() = http.userAgent
        set(value) {
            http.userAgent = value
        }
    /**
     * Specify the HTTP communication header.
     *
     * User-Agent can also be specified, but [userAgent] takes precedence.
     */
    var headers: Map<String, String>
        get() = http.headers
        set(value) {
            http.headers = value
        }
    /**
     * Specify the maximum download size when downloading HTML file.
     *
     * The default is 64 kB.
     * When the value is 0 or less, download is done without restriction.
     */
    var downloadLimit: Int
        get() = fromPage.downloadLimit
        set(value) {
            fromPage.downloadLimit = value
        }

    /**
     * Analyzes the HTML of the designated URL and extract the icon information specified by the link tag.
     *
     * **Do not call from main thread** to avoid NetworkOnMainThreadException
     *
     * Download the HTML file to the [downloadLimit] size
     * and analyze the `<link>` tag within that range.
     *
     * @param siteUrl
     * URL of analysis target page
     */
    @WorkerThread
    fun fromPage(siteUrl: String): List<PageIcon> {
        return fromPage.invoke(siteUrl)
    }

    /**
     * Get valid icon information,
     * by trying to access to some icon from the root of domain of specified URL.
     *
     * **Do not call from main thread** to avoid NetworkOnMainThreadException
     *
     * Judge the existence of icon using HTTP HEAD.
     * Since many of these sites do not have these icons, 404 errors are recorded on the server.
     * Because it is a bad manners, execution should be kept to a minimum.
     *
     * Checks the existence of the icon in order,
     * and returns the information when judging that the icon exists.
     * Processing is interrupted at the stage of finding the icon, and no confirmation is made on it after that
     *
     * The order of checking the existence of the icon is as follows
     *
     * In case of `withPrecomposed` is false and `sizes` is not specify.
     *
     * 1. apple-touch-icon.png
     * 2. favicon.ico
     *
     * In case of `withPrecomposed` is true and `sizes` is not specify.
     *
     * 1. apple-touch-icon-precomposed.png
     * 2. apple-touch-icon.png
     * 3. favicon.ico
     *
     * In case of `withPrecomposed` is true and `sizes` is `listOf("120x120", "72x72")`
     *
     * 1. apple-touch-icon-120x120-precomposed.png
     * 2. apple-touch-icon-120x120.png
     * 3. apple-touch-icon-72x72-precomposed.png
     * 4. apple-touch-icon-72x72.png
     * 5. apple-touch-icon-precomposed.png
     * 6. apple-touch-icon.png
     * 7. favicon.ico
     *
     * @param siteUrl
     * URL of analysis target page
     *
     * @param withPrecomposed
     * If specify true, include the "precomposed". default is true
     *
     * @param sizes
     * Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.
     */
    @WorkerThread
    fun fromDomain(
        siteUrl: String,
        withPrecomposed: Boolean = true,
        sizes: List<String> = emptyList()
    ): DomainIcon? {
        return fromDomain.invoke(siteUrl, withPrecomposed, sizes)
    }

    /**
     * It performs the same processing as [fromDomain] using HTTP GET, and downloads binary if icon exists.
     *
     * **Do not call from main thread** to avoid NetworkOnMainThreadException
     *
     * Since [fromDomain] get only the icon information,
     * the binary data of the icon needs to be downloaded again using HTTP GET.
     * In this method, HTTP GET is used to confirm the existence of the icon,
     * and if there is an icon, download is also done.
     * The result is returned with icon information and binary Pair.
     *
     * Do not call from main thread.
     *
     * @param siteUrl
     * URL of analysis target page
     *
     * @param withPrecomposed
     * If specify true, include the "precomposed". default is true
     *
     * @param sizes
     * Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.
     */
    @WorkerThread
    fun fromDomainWithDownload(
        siteUrl: String,
        withPrecomposed: Boolean = true,
        sizes: List<String> = emptyList()
    ): Pair<DomainIcon, ByteArray>? {
        return fromDomain.invokeWithDownload(siteUrl, withPrecomposed, sizes)
    }

    /**
     * Processing similar to [fromDomain] is performed, but even if an icon is found,
     * processing is not stopped but a list of all existing icon information is returned.
     *
     * **Do not call from main thread** to avoid NetworkOnMainThreadException
     *
     * In order to put unnecessary load on the server,
     * please use only for debugging and verification.
     * And **strongly recommend to avoid use in production**.
     *
     * @param siteUrl
     * URL of analysis target page
     *
     * @param withPrecomposed
     * If specify true, include the "precomposed". default is true
     *
     * @param sizes
     * Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.
     */
    @WorkerThread
    fun listFromDomain(
        siteUrl: String,
        withPrecomposed: Boolean = true,
        sizes: List<String> = emptyList()
    ): List<DomainIcon> {
        return fromDomain.list(siteUrl, withPrecomposed, sizes)
    }
}
