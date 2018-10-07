/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
enum class Relationship(val value: String) {
    APPLE_TOUCH_ICON_PRECOMPOSED("apple-touch-icon-precomposed"),
    APPLE_TOUCH_ICON("apple-touch-icon"),
    ICON("icon"),
    SHORTCUT_ICON("shortcut icon"),
    ;

    companion object {
        fun of(value: String?): Relationship? {
            return values().find { it.value.equals(value, true) }
        }
    }
}
