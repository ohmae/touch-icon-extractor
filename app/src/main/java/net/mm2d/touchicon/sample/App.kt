/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.sample

import android.app.Application
import android.util.Log
import android.webkit.WebView
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true)
        RxJavaPlugins.setErrorHandler { logError(it) }
    }

    private fun logError(e: Throwable) {
        when (e) {
            is UndeliverableException ->
                Log.w(TAG, "UndeliverableException:", e.cause)
            is OnErrorNotImplementedException ->
                Log.w(TAG, "OnErrorNotImplementedException:", e.cause)
            else ->
                Log.w(TAG, e)
        }
    }

    companion object {
        private const val TAG = "App"
    }
}
