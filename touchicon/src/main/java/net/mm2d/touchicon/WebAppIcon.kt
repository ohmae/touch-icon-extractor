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

    private var _size: Size? = null
    /**
     * Infer display size of this icon from sizes value.
     *
     * if fail to infer from sizes, try to infer from url.
     */
    override fun inferSize(): Size {
        _size?.let { return it }
        return inferSizeInner().also { _size = it }
    }

    private fun inferSizeInner(): Size {
        val size = inferSizeFromSizes(sizes)
        if (size.width > 0 && size.height > 0) {
            return size
        }
        return inferSizeFromUrl(url)
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

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebAppIcon> {
        override fun createFromParcel(parcel: Parcel): WebAppIcon {
            return WebAppIcon(parcel)
        }

        override fun newArray(size: Int): Array<WebAppIcon?> {
            return arrayOfNulls(size)
        }
    }
}
