/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip.settings

import net.mm2d.webclip.BuildConfig

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal object Maintainer {
    private const val SETTINGS_VERSION = 0

    fun maintain(storage: SettingsStorage) {
        if (storage.readInt(Key.SETTINGS_VERSION) != SETTINGS_VERSION) {
            storage.clear()
            storage.writeInt(Key.SETTINGS_VERSION, SETTINGS_VERSION)
            storage.writeInt(Key.APP_VERSION, BuildConfig.VERSION_CODE)
            return
        }
        if (storage.readInt(Key.APP_VERSION) != BuildConfig.VERSION_CODE) {
            storage.writeInt(Key.APP_VERSION, BuildConfig.VERSION_CODE)
        }
    }
}
