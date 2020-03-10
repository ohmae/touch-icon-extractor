/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Icon information described in Web App Manifest.
 *
 * This is described by the following JSON.
 *
 * ```json
 * {
 *   "short_name": "AirHorner",
 *   "name": "Kinlan's AirHorner of Infamy",
 *   "icons": [
 *     {
 *       "src": "launcher-icon-1x.png",
 *       "type": "image/png",
 *       "sizes": "48x48"
 *     },
 *     {
 *       "src": "launcher-icon-2x.png",
 *       "type": "image/png",
 *       "sizes": "96x96"
 *     },
 *     {
 *       "src": "launcher-icon-4x.png",
 *       "type": "image/png",
 *       "sizes": "192x192"
 *     }
 *   ],
 *   "start_url": "index.html?launcher=true"
 * }
 * ```
 *
 * And it is described as follows in HTML.
 *
 * ```html
 * <link rel="manifest" href="/manifest.json">
 * ```
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class WebAppIcon(
    override val url: String,
    override val sizes: String,
    override val mimeType: String,
    /**
     * pixel densities.
     *
     * this is an optional member.
     * eg. "3.0"
     */
    val density: String
) : Icon {
    override val rel: Relationship = Relationship.MANIFEST
    override val precomposed: Boolean = true
    override val length: Int = -1

    private val size: Size by lazy {
        inferSizeInner()
    }

    /**
     * Infer display size of this icon from sizes value.
     *
     * if fail to infer from sizes, try to infer from url.
     *
     * @return Inferred size
     */
    override fun inferSize(): Size = size

    private fun inferSizeInner(): Size = inferSizeFromSizes(sizes).let {
        if (it.isValid()) it else inferSizeFromUrl(url)
    }
}
