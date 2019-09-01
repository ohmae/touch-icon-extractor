/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip.settings

import android.content.Context

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class Settings private constructor(
    private val storage: SettingsStorage
) {
    fun shouldUseExtension(): Boolean = storage.readBoolean(Key.USE_EXTENSION)

    companion object {
        private lateinit var settings: Settings

        fun initialize(context: Context) {
            val storage = SettingsStorage(context)
            Maintainer.maintain(storage)
            settings = Settings(storage)
        }

        fun get(): Settings = settings
    }
}
