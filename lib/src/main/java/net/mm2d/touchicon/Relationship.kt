/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * enum of relationship between icon and page.
 *
 * Express the value of rel of the link tag.
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
enum class Relationship(
        /**
         * Raw value of rel.
         */
        val value: String
) {
    /**
     * This expresses that the rel value of the link tag is "apple-touch-icon-precomposed".
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="80x80">
     * ```
     */
    APPLE_TOUCH_ICON_PRECOMPOSED("apple-touch-icon-precomposed"),
    /**
     * This expresses that the rel value of the link tag is "apple-touch-icon".
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="apple-touch-icon" href="/apple-touch-icon.png" sizes="57x57">
     * ```
     */
    APPLE_TOUCH_ICON("apple-touch-icon"),
    /**
     * This expresses that the rel value of the link tag is "icon".
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="icon" href="/favicon.ico" type="image/x-icon">
     * ```
     */
    ICON("icon"),
    /**
     * This expresses that the rel value of the link tag is "shortcut icon".
     *
     * This is used only in [PageIcon].
     * It is left for old IE, but it is a deprecated description as HTML.
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="shortcut icon" href="/favicon.ico">
     * ```
     */
    SHORTCUT_ICON("shortcut icon"),
    ;

    companion object {
        internal fun of(value: String?): Relationship? {
            return values().find { it.value.equals(value, true) }
        }
    }
}
