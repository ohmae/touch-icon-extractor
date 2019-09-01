/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.os.Parcel
import android.os.Parcelable

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
) : Icon, Parcelable {
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

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(sizes)
        parcel.writeString(mimeType)
        parcel.writeString(density)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WebAppIcon> {
        override fun createFromParcel(parcel: Parcel): WebAppIcon = WebAppIcon(parcel)
        override fun newArray(size: Int): Array<WebAppIcon?> = arrayOfNulls(size)
    }
}
