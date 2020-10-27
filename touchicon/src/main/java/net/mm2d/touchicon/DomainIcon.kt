/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Icon information associated with the Web site domain.
 *
 * e.g.
 * Assuming that the URL of the site is https://www.example.com/index.html
 *
 * When the file name is "apple-touch-icon-120x120-precomposed.png",
 * the following values are stored
 * ```
 * rel=ICON
 * url=https://www.example.com/favicon.ico
 * sizes=120x120
 * mimeType=image/x-icon   <- Content-Type value of the response from the server
 * precomposed=false
 * length=99999            <- Content-Length value of the response from the server
 * ```
 *
 * When the file name is "favicon.con",
 * the following values are stored
 * ```
 * rel=ICON
 * url=https://www.example.com/favicon.ico
 * sizes=                  <- empty
 * mimeType=image/x-icon   <- Content-Type value of the response from the server
 * precomposed=false
 * length=99999            <- Content-Length value of the response from the server
 * ```
 */
data class DomainIcon(
    /**
     * Relationship between icon and page.
     *
     * This is pseudo information and determined based on the file name.
     *
     * - "favicon.ico" -> [Relationship.ICON]
     * - "apple-touch-icon.png" -> [Relationship.APPLE_TOUCH_ICON]
     * - "apple-touch-icon-precomposed.png" -> [Relationship.APPLE_TOUCH_ICON_PRECOMPOSED]
     */
    override val rel: Relationship,
    /**
     * Icon url.
     */
    override val url: String,
    /**
     * Size information, assumed format is (width)x(height). e.g. "80x80".
     *
     * When size is specified by argument, its value is stored.
     */
    override val sizes: String,
    /**
     * Icon MIME type. e.g. "image/png"
     *
     * "Content-Type" value of HTTP header.
     */
    override val mimeType: String,
    /**
     * true if this is for a precomposed touch icon.
     */
    override val precomposed: Boolean,
    /**
     * Icon file length.
     *
     * "Content-Length" value of HTTP header.
     * Negative value means unknown.
     */
    override val length: Int
) : Icon {
    private val size: Size by lazy {
        inferSizeFromSizes(sizes)
    }

    /**
     * Infer display size of this icon from sizes value.
     *
     * @return Inferred size
     */
    override fun inferSize(): Size = size
}
