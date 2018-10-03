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
enum class Rel(val value: String) {
    ICON("icon"),
    APPLE_TOUCH_ICON("apple-touch-icon"),
    SHORTCUT_ICON("shortcut icon"),
    ;

    companion object {
        fun of(value: String?): Rel? {
            return values().find { it.value.equals(value, true) }
        }
    }
}
