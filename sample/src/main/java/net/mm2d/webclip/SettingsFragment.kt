/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.webclip

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.mm2d.webclip.settings.Key

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
        findPreference<Preference>(Key.VERSION_NUMBER.name)?.summary = BuildConfig.VERSION_NAME
        findPreference<Preference>(Key.SOURCE_CODE.name)?.setOnPreferenceClickListener {
            startActivity(Intent(activity, MainActivity::class.java).also {
                it.action = Intent.ACTION_VIEW
                it.addCategory(Intent.CATEGORY_BROWSABLE)
                it.data = Uri.parse("https://github.com/ohmae/touch-icon-extractor")
            })
            true
        }
        findPreference<Preference>(Key.LICENSE.name)?.setOnPreferenceClickListener {
            startActivity(Intent(activity, MainActivity::class.java).also {
                it.action = Intent.ACTION_VIEW
                it.addCategory(Intent.CATEGORY_BROWSABLE)
                it.data = Uri.parse("file:///android_asset/license.html")
            })
            true
        }
    }
}
