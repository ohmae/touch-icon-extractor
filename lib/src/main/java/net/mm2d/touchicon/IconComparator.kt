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
 *
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object IconComparator {
    /**
     * Comparator for order by size -> rel
     *
     * Size is based on width x height.
     * The priority order of relationship is
     * APPLE_TOUCH_ICON_PRECOMPOSED > APPLE_TOUCH_ICON > ICON > SHORTCUT_ICON
     */
    val SIZE_REL = Comparator<Icon> { icon1, icon2 ->
        val result = evaluateSize(icon1) - evaluateSize(icon2)
        if (result != 0) result else evaluateRelationship(icon1) - evaluateRelationship(icon2)
    }

    /**
     * Comparator for order by rel -> size
     *
     * The priority order of relationship is
     * APPLE_TOUCH_ICON_PRECOMPOSED > APPLE_TOUCH_ICON > ICON > SHORTCUT_ICON
     * Size is based on width x height.
     */
    val REL_SIZE = Comparator<Icon> { icon1, icon2 ->
        val result = evaluateRelationship(icon1) - evaluateRelationship(icon2)
        if (result != 0) result else evaluateSize(icon1) - evaluateSize(icon2)
    }

    private fun evaluateSize(icon: Icon): Int {
        val size = icon.inferSize()
        return if (size.x > 0 && size.y > 0) size.x * size.y else 0
    }

    private fun evaluateRelationship(icon: Icon): Int = when (icon.rel) {
        Relationship.APPLE_TOUCH_ICON_PRECOMPOSED -> 3
        Relationship.APPLE_TOUCH_ICON -> 2
        Relationship.ICON -> 1
        Relationship.SHORTCUT_ICON -> 0
    }
}
