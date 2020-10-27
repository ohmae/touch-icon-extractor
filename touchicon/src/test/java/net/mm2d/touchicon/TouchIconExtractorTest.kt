/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import net.mm2d.touchicon.http.HttpClientAdapter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(Enclosed::class)
@Suppress("TestFunctionName", "ClassName")
class TouchIconExtractorTest {
    @RunWith(JUnit4::class)
    class `adapter test` {
        private lateinit var httpClientAdapter: HttpClientAdapter
        private lateinit var extractFromPage: ExtractFromPage
        private lateinit var extractFromDomain: ExtractFromDomain
        private lateinit var extractor: TouchIconExtractor

        @Before
        fun setUp() {
            httpClientAdapter = mockk(relaxed = true)
            extractFromPage = mockk(relaxed = true)
            extractFromDomain = mockk(relaxed = true)
            mockkObject(TouchIconExtractor.Companion)
            every { TouchIconExtractor.createExtractFromPage(any()) } returns extractFromPage
            every { TouchIconExtractor.createExtractFromDomain(any()) } returns extractFromDomain
            extractor = TouchIconExtractor(httpClientAdapter)
            unmockkObject(TouchIconExtractor.Companion)
        }

        @After
        fun tearDown() {
        }

        @Test
        fun setUserAgent() {
            val ua = "user-agent"
            extractor.userAgent = ua
            verify { httpClientAdapter.userAgent = ua }
        }


        @Test
        fun getUserAgent() {
            val ua = "user-agent"
            every { httpClientAdapter.userAgent } returns ua
            assertThat(extractor.userAgent).isEqualTo(ua)
        }

        @Test
        fun setHeaders() {
            val headers = mapOf("key" to "value")
            extractor.headers = headers
            verify { httpClientAdapter.headers = headers }
        }

        @Test
        fun getHeaders() {
            val headers = mapOf("key" to "value")
            every { httpClientAdapter.headers } returns headers
            assertThat(extractor.headers).isEqualTo(headers)
        }

        @Test
        fun setDownloadLimit() {
            val downloadLimit = 10
            extractor.downloadLimit = downloadLimit
            verify { extractFromPage.downloadLimit = downloadLimit }
        }

        @Test
        fun getDownloadLimit() {
            val downloadLimit = 10
            every { extractFromPage.downloadLimit } returns downloadLimit
            assertThat(extractor.downloadLimit).isEqualTo(downloadLimit)
        }

        @Test
        fun fromPage() {
            val siteUrl = "https://example.com/"
            extractor.fromPage(siteUrl)
            verify(exactly = 1) { extractFromPage.fromPage(siteUrl, false) }
            extractor.fromPage(siteUrl, true)
            verify(exactly = 1) { extractFromPage.fromPage(siteUrl, true) }
        }

        @Test
        fun fromManifest() {
            val siteUrl = "https://example.com/"
            extractor.fromManifest(siteUrl)
            verify(exactly = 1) { extractFromPage.fromManifest(siteUrl) }
        }

        @Test
        fun fromDomain() {
            val siteUrl = "https://example.com/"
            val sizes = listOf("120x120")
            extractor.fromDomain(siteUrl)
            verify(exactly = 1) { extractFromDomain.fromDomain(siteUrl, true, emptyList()) }
            extractor.fromDomain(siteUrl, false)
            verify(exactly = 1) { extractFromDomain.fromDomain(siteUrl, false, emptyList()) }
            extractor.fromDomain(siteUrl, sizes = listOf("120x120"))
            verify(exactly = 1) { extractFromDomain.fromDomain(siteUrl, true, sizes) }
            extractor.fromDomain(siteUrl, false, listOf("120x120"))
            verify(exactly = 1) { extractFromDomain.fromDomain(siteUrl, false, sizes) }
        }

        @Test
        fun fromDomainWithDownload() {
            val siteUrl = "https://example.com/"
            val sizes = listOf("120x120")
            extractor.fromDomainWithDownload(siteUrl)
            verify(exactly = 1) {
                extractFromDomain.fromDomainWithDownload(siteUrl, true, emptyList())
            }
            extractor.fromDomainWithDownload(siteUrl, false)
            verify(exactly = 1) {
                extractFromDomain.fromDomainWithDownload(siteUrl, false, emptyList())
            }
            extractor.fromDomainWithDownload(siteUrl, sizes = listOf("120x120"))
            verify(exactly = 1) {
                extractFromDomain.fromDomainWithDownload(siteUrl, true, sizes)
            }
            extractor.fromDomainWithDownload(siteUrl, false, listOf("120x120"))
            verify(exactly = 1) {
                extractFromDomain.fromDomainWithDownload(siteUrl, false, sizes)
            }
        }

        @Test
        fun listFromDomain() {
            val siteUrl = "https://example.com/"
            val sizes = listOf("120x120")
            extractor.listFromDomain(siteUrl)
            verify(exactly = 1) { extractFromDomain.listFromDomain(siteUrl, true, emptyList()) }
            extractor.listFromDomain(siteUrl, false)
            verify(exactly = 1) { extractFromDomain.listFromDomain(siteUrl, false, emptyList()) }
            extractor.listFromDomain(siteUrl, sizes = listOf("120x120"))
            verify(exactly = 1) { extractFromDomain.listFromDomain(siteUrl, true, sizes) }
            extractor.listFromDomain(siteUrl, false, listOf("120x120"))
            verify(exactly = 1) { extractFromDomain.listFromDomain(siteUrl, false, sizes) }
        }
    }

    @RunWith(JUnit4::class)
    class `download test` {
        @Test
        fun test() {
            val extractor = TouchIconExtractor()
            extractor.fromManifest("https://example.com/")
        }
    }
}
