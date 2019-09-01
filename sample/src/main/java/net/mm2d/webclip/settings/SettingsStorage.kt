/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class SettingsStorage(context: Context) {
    private object PreferencesHolder {
        private var preferences: SharedPreferences? = null

        @Synchronized
        internal fun get(context: Context): SharedPreferences {
            preferences?.let {
                return it
            }
            return PreferenceManager.getDefaultSharedPreferences(context).also {
                preferences = it
            }
        }
    }

    private val preferences: SharedPreferences = PreferencesHolder.get(context)

    fun clear() {
        preferences.edit()
            .clear()
            .apply()
    }

    operator fun contains(key: Key): Boolean = preferences.contains(key.name)

    fun writeBoolean(key: Key, value: Boolean) {
        require(key.isBooleanKey) { key.name + " is not key for boolean" }
        preferences.edit()
            .putBoolean(key.name, value)
            .apply()
    }

    fun readBoolean(key: Key): Boolean {
        require(key.isBooleanKey) { key.name + " is not key for boolean" }
        return preferences.getBoolean(key.name, key.defaultBoolean)
    }

    fun writeInt(key: Key, value: Int) {
        require(key.isIntKey) { key.name + " is not key for int" }
        preferences.edit()
            .putInt(key.name, value)
            .apply()
    }

    fun readInt(key: Key): Int {
        require(key.isIntKey) { key.name + " is not key for int" }
        return preferences.getInt(key.name, key.defaultInt)
    }
}
