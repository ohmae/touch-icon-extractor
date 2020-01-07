/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object ParcelableTestUtil {
    fun <T : Parcelable> saveAndRestore(original: T): T {
        val parcel = Parcel.obtain()
        try {
            val key = "key"
            parcel.writeBundle(Bundle().also {
                it.putParcelable(key, original)
            })
            parcel.setDataPosition(0)
            return parcel.readBundle()!!.getParcelable(key)!!
        } finally {
            parcel.recycle()
        }
    }
}
