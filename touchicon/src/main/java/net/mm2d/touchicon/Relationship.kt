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
    val value: String,
    /**
     * Priority to expect quality.
     */
    val priority: Int,
    /**
     * Icon tag.
     */
    internal val isIcon: Boolean
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
    APPLE_TOUCH_ICON_PRECOMPOSED("apple-touch-icon-precomposed", 3, true),
    /**
     * This expresses that the rel value of the link tag is "apple-touch-icon".
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="apple-touch-icon" href="/apple-touch-icon.png" sizes="57x57">
     * ```
     */
    APPLE_TOUCH_ICON("apple-touch-icon", 2, true),
    /**
     * This expresses that the rel value of the link tag is "icon".
     *
     * This corresponds to the following description of html.
     *
     * ```html
     * <link rel="icon" href="/favicon.ico" type="image/x-icon">
     * ```
     */
    ICON("icon", 1, true),
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
    SHORTCUT_ICON("shortcut icon", 0, true),
    /**
     * Used to represent the icon described in Web App Manifest.
     *
     * This is used only in [WebAppIcon].
     *
     * This corresponds to the following description of html/json.
     *
     * ```html
     * <link rel="manifest" href="manifest.json">
     * ```
     *
     * ```json
     * {
     *   "icons": [
     *     {
     *       "src": "icon.png",
     *       "type": "image/png",
     *       "sizes": "48x48"
     *     }
     *   ]
     * }
     * ```
     */
    MANIFEST("manifest", 4, false),
    ;

    companion object {
        private val VALUE_MAP = values()
            .map { it.value to it }
            .toMap()

        internal fun of(value: String?): Relationship? =
            if (value.isNullOrEmpty()) null else VALUE_MAP[value.toLowerCase()]
    }
}
