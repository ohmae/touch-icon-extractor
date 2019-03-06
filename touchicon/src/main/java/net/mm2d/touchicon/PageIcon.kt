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
 * Icon information associated with the Web page.
 *
 * e.g.
 * Assuming that the URL of the site is https://www.example.com/index.html
 *
 * When the HTML tag is
 * ```html
 * <link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="120x120">
 * ```
 * the following values are stored
 * ```
 * rel=APPLE_TOUCH_ICON_PRECOMPOSED
 * url=https://www.example.com/apple-touch-icon-precomposed.png
 * sizes=120x120
 * mimeType=             <- empty
 * precomposed=true
 * length=-1             <- always -1
 * ```
 *
 * When the HTML tag is
 * ```html
 * <link rel="icon" href="/favicon.ico" type="image/x-icon">
 * ```
 * the following values are stored
 * ```
 * rel=ICON
 * url=https://www.example.com/favicon.ico
 * sizes=                <- empty
 * mimeType=image/x-icon
 * precomposed=false
 * length=-1             <- always -1
 * ```
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
data class PageIcon(
    /**
     * Relationship between icon and page.
     *
     * rel attribute value of the link tag.
     */
    override val rel: Relationship,
    /**
     * Icon url.
     */
    override val url: String,
    /**
     * Size information, assumed format is (width)x(height). e.g. "80x80".
     *
     * Value of sizes attribute. often blank.
     */
    override val sizes: String,
    /**
     * Icon MIME type. e.g. "image/png"
     *
     * Value of type attribute. often blank.
     */
    override val mimeType: String,
    /**
     * true if this is for a precomposed touch icon.
     *
     * The same meaning that the value of [rel] is [Relationship.APPLE_TOUCH_ICON_PRECOMPOSED]
     */
    override val precomposed: Boolean = rel == Relationship.APPLE_TOUCH_ICON_PRECOMPOSED,
    /**
     * Icon file length.
     *
     * Negative value means unknown. and in this class always -1.
     */
    override val length: Int = -1
) : Icon, Parcelable {
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
        Relationship.of(parcel.readString())!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

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
