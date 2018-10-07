/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class DomainIcon(
        override val rel: Rel,
        override val url: String,
        override val sizes: String,
        override val mimeType: String,
        override val precomposed: Boolean,
        override val length: Int
) : Icon, Parcelable {
    constructor(parcel: Parcel) : this(
            Rel.of(parcel.readString())!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readByte() != 0.toByte(),
            parcel.readInt())

    override fun inferSize() = inferSizeFromSizes(sizes)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rel.value)
        parcel.writeString(url)
        parcel.writeString(sizes)
        parcel.writeString(mimeType)
        parcel.writeByte(if (precomposed) 1 else 0)
        parcel.writeInt(length)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<DomainIcon> {
        override fun createFromParcel(parcel: Parcel): DomainIcon {
            return DomainIcon(parcel)
        }

        override fun newArray(size: Int): Array<DomainIcon?> {
            return arrayOfNulls(size)
        }
    }
}
