/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

/**
 * Comparator for sorting based on quality.
 *
 * To get the icon with the highest quality.
 *
 * If you give priority to size
 * ```kotlin
 * val bestIcon: Icon? = TouchIconExtractor.fromPage(url)
 *     .maxWith(IconComparator.SIZE_REL)
 * ```
 *
 * If you give priority to type(relationship)
 * ```kotlin
 * val bestIcon: Icon? = TouchIconExtractor.fromPage(url)
 *     .maxWith(IconComparator.REL_SIZE)
 * ```
 */
object IconComparator {
    /**
     * Comparator based on size.
     */
    val SIZE = compareBy<Icon> { it.inferArea() }

    /**
     * Comparator based on quality inferred from relationship.
     */
    val REL = compareBy<Icon> { it.rel.priority }

    /**
     * Comparator for order by size -> rel
     *
     * Size is based on width x height.
     * The priority order of relationship is
     * APPLE_TOUCH_ICON_PRECOMPOSED > APPLE_TOUCH_ICON > ICON > SHORTCUT_ICON
     */
    val SIZE_REL = compareBy<Icon> { it.inferArea() }.thenBy { it.rel.priority }

    /**
     * Comparator for order by rel -> size
     *
     * The priority order of relationship is
     * APPLE_TOUCH_ICON_PRECOMPOSED > APPLE_TOUCH_ICON > ICON > SHORTCUT_ICON
     * Size is based on width x height.
     */
    val REL_SIZE = compareBy<Icon> { it.rel.priority }.thenBy { it.inferArea() }
}
