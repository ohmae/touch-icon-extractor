/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.graphics.Point
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class PageIcon(
        override val rel: Relationship,
        override val url: String,
        override val sizes: String,
        override val mimeType: String,
        override val precomposed: Boolean = rel == Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
        override val length: Int = -1
) : Icon, Parcelable {
    constructor(parcel: Parcel) : this(
            Relationship.of(parcel.readString())!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )

    override fun inferSize(): Point {
        val size = inferSizeFromSizes(sizes)
        if (size.x != 0 && size.y != 0) {
            return size
        }
        return inferSizeFromUrl(url)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rel.value)
        parcel.writeString(url)
        parcel.writeString(sizes)
        parcel.writeString(mimeType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<PageIcon> {
        override fun createFromParcel(parcel: Parcel): PageIcon {
            return PageIcon(parcel)
        }

        override fun newArray(size: Int): Array<PageIcon?> {
            return arrayOfNulls(size)
        }
    }
}
