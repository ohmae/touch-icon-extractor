/*
 * Copyright (c) 2026 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.http

/**
 * Optional response capability that exposes the URL after redirects.
 *
 * Implement this when an HTTP adapter follows redirects so relative icon URLs
 * can be resolved against the document that was actually downloaded.
 */
interface EffectiveUrlHttpResponse : HttpResponse {
    /** URL of the final HTTP response. */
    val effectiveUrl: String
}
