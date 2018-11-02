/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import android.security.NetworkSecurityPolicy

import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@Implements(NetworkSecurityPolicy::class)
class NetworkSecurityPolicyShadow {
    @Implementation
    fun isCleartextTrafficPermitted(host: String): Boolean {
        return true
    }

    companion object {
        @JvmStatic
        @Implementation
        fun getInstance(): NetworkSecurityPolicy {
            try {
                val shadow = Class.forName("android.security.NetworkSecurityPolicy")
                return shadow.newInstance() as NetworkSecurityPolicy
            } catch (e: Exception) {
                throw AssertionError()
            }
        }
    }
}
