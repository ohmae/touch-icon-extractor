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
import java.util.regex.Pattern

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class IconInfo(
        val rel: Rel,
        val url: String,
        val sizes: String,
        val type: String,
        val siteUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            Rel.of(parcel.readString())!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    )

    fun inferSize(): Point {
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
        parcel.writeString(type)
        parcel.writeString(siteUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<IconInfo> {
        override fun createFromParcel(parcel: Parcel): IconInfo {
            return IconInfo(parcel)
        }

        override fun newArray(size: Int): Array<IconInfo?> {
            return arrayOfNulls(size)
        }

        fun inferSizeFromUrl(url: String): Point {
            val fileName = url.substring(url.lastIndexOf('/'))
            val matcher = Pattern.compile("\\d+x\\d+").matcher(fileName)
            if (!matcher.find()) {
                return Point(0, 0)
            }
            return inferSizeFromSizes(matcher.group())
        }

        fun inferSizeFromSizes(sizes: String): Point {
            val part = sizes.split('x')
            if (part.size == 2) {
                return Point(part[0].toIntOrNull() ?: 0, part[1].toIntOrNull() ?: 0)
            }
            return Point(0, 0)
        }
    }
}
