/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import net.mm2d.touchicon.http.HttpClientAdapter
import net.mm2d.touchicon.http.HttpResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class ExtractFromPageTest {
    @Test
    fun extract_icon() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_icon_with_manifest() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent(),
            true
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_shortcut_icon() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            <link rel="shortcut icon">
            </head></html>
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.SHORTCUT_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_apple_touch_icon() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_apple_touch_icon_precomposed() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON_PRECOMPOSED)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_omitted_scheme() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="//www.example.com/apple-touch-icon-57x57.png">
            </head></html>
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_broken() {
        val extract = ExtractFromPage(mockk())
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
            </hea
            """.trimIndent(),
            false
        )[0]
        assertThat(result.rel).isEqualTo(Relationship.APPLE_TOUCH_ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/apple-touch-icon-57x57.png")
        assertThat(result.sizes).isEqualTo("57x57")
        assertThat(result.mimeType).isEqualTo("")
    }

    @Test
    fun extract_web_app_manifest() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.bodyString() } returns """
            {
                "short_name": "short_name",
                "name": "name",
                "icons": [{
                    "src": "launcher-icon-1x.png",
                    "type": "image/png",
                    "sizes": "48x48"
                }, {
                    "src": "launcher-icon-4x.png",
                    "type": "image/png",
                    "sizes": "192x192",
                    "density": "4.0"
                }],
                "start_url": "index.html?launcher=true"
            }""".trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="/manifest.json">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result[0].rel).isEqualTo(Relationship.MANIFEST)
        assertThat(result[0].mimeType).isEqualTo("image/png")
        assertThat(result[0].sizes).isEqualTo("48x48")
        assertThat(result[0].url).isEqualTo("https://www.example.com/launcher-icon-1x.png")
        assertThat(result[1].rel).isEqualTo(Relationship.MANIFEST)
        assertThat(result[1].mimeType).isEqualTo("image/png")
        assertThat(result[1].sizes).isEqualTo("192x192")
        assertThat(result[1].url).isEqualTo("https://www.example.com/launcher-icon-4x.png")
        assertThat((result[1] as WebAppIcon).density).isEqualTo("4.0")
    }

    @Test
    fun extract_web_app_manifest_json_error1() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.bodyString() } returns """
            {
                "short_name": "short_name",
                "name": "name",
                "icons": [{
                    "src": "launcher-icon-1x.png",
                    "type": "image/png",
                    "sizes": "48x48"
                }, {
                    "src": "launcher-icon-4x.png",
                    "type": "image/png",
                    "sizes": "192x192",
                    "density": "4.0"
                }],
                "start_url": "index.html?launcher=true"
            """.trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="/manifest.json">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_web_app_manifest_json_error2() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.bodyString() } returns """
            {
                "short_name": "short_name",
                "name": "name",
                "icons": ["dummy",
                {
                    "src": "launcher-icon-1x.png",
                    "type": "image/png",
                    "sizes": "48x48"
                }, {
                    "type": "image/png",
                    "sizes": "192x192",
                    "density": "4.0"
                }],
                "start_url": "index.html"
            }""".trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="/manifest.json">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result).hasSize(1)
    }

    @Test
    fun extract_web_app_manifest_href_is_empty() {
        val httpClient: HttpClientAdapter = mockk()
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_web_app_manifest_bodyString_is_null() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.bodyString() } returns null
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="/manifest.json">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_web_app_manifest_IOException() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } throws IOException()
        val extract = ExtractFromPage(httpClient)
        val result = extract.extractFromHtml(
            "https://www.example.com/",
            """
            <html><head>
            <link rel="stylesheet" href="style.css">
            <link rel="manifest" href="/manifest.json">
            </head></html>
            """.trimIndent(),
            true
        )
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_from_page_html() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString(any()) } returns """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.fromPage("https://www.example.com/", false)[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_from_page_download_limit() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString(any()) } returns """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        extract.downloadLimit = 1
        val result = extract.fromPage("https://www.example.com/", false)[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
        assertThat(result.precomposed).isFalse()
        assertThat(result.length).isEqualTo(-1)
    }

    @Test
    fun extract_from_page_html_fail_get_IOException() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } throws IOException()
        val extract = ExtractFromPage(httpClient)
        val result = extract.fromPage("https://www.example.com/", false)
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_from_page_html_fail_body_IOException() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString(any()) } throws IOException()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.fromPage("https://www.example.com/", false)
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_from_page_html_fail_body_is_empty() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString(any()) } returns ""
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.fromPage("https://www.example.com/", false)
        assertThat(result).isEmpty()
    }

    @Test
    fun extract_from_page_xhtml() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "application/xhtml+xml"
            every { it.bodyString(any()) } returns """
            <html><head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon">
            </head></html>
            """.trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val result = extract.fromPage("https://www.example.com/", false)[0]
        assertThat(result.rel).isEqualTo(Relationship.ICON)
        assertThat(result.url).isEqualTo("https://www.example.com/favicon.ico")
        assertThat(result.sizes).isEqualTo("")
        assertThat(result.mimeType).isEqualTo("image/vnd.microsoft.icon")
    }

    @Test
    fun extract_from_page_fail1() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns false
        }
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromPage("https://www.example.com/", false)).isEmpty()
    }

    @Test
    fun extract_from_page_fail2() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "image/png"
        }
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromPage("https://www.example.com/", false)).isEmpty()
    }

    @Test
    fun extract_from_page_fail3() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns null
        }
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromPage("https://www.example.com/", false)).isEmpty()
    }

    @Test
    fun extract_from_manifest() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString(any()) } returns """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
            <html lang="ja">
            <head>
            <link rel="icon" href="/favicon.ico" type="image/vnd.microsoft.icon"/>
            <link rel="manifest" href="/manifest.json">
            <link rel="shortcut icon" href="/favicon.ico" type="image/vnd.microsoft.icon" />
            """.trimIndent()
        }
        every {
            httpClient.get("https://www.example.com/manifest.json")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.bodyString(any()) } returns """
                {
                  "icons": [{
                    "src": "images/touch48.png",
                    "sizes": "48x48",
                    "type": "image/png"
                  }, {
                    "src": "images/touch72.png",
                    "sizes": "72",
                    "type": "image/png"
                  }]
                }
            """.trimIndent()
        }
        val extract = ExtractFromPage(httpClient)
        val results = extract.fromManifest("https://www.example.com/")
        assertThat(results).hasSize(2)
        assertThat(results[0].rel).isEqualTo(Relationship.MANIFEST)
        assertThat(results[0].precomposed).isTrue()
        assertThat(results[0].length).isEqualTo(-1)
        assertThat(results[0].inferSize()).isEqualTo(Size(48, 48))
        assertThat(results[1].inferSize()).isEqualTo(Size(-1, -1))
    }

    @Test
    fun extract_from_manifest_fail1() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns false
        }
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromManifest("https://www.example.com/")).isEmpty()
    }

    @Test
    fun extract_from_manifest_fail2() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } throws IOException()
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromManifest("https://www.example.com/")).isEmpty()
    }

    @Test
    fun extract_from_manifest_fail3() {
        val httpClient: HttpClientAdapter = mockk()
        every {
            httpClient.get("https://www.example.com/")
        } returns mockk<HttpResponse>(relaxed = true).also {
            every { it.isSuccess } returns true
            every { it.header("Content-Type") } returns "text/html"
            every { it.bodyString() } returns ""
        }
        val extract = ExtractFromPage(httpClient)
        assertThat(extract.fromManifest("https://www.example.com/")).isEmpty()
    }
}
