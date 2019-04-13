[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [TouchIconExtractor](index.md) / [fromPage](./from-page.md)

# fromPage

`@WorkerThread fun fromPage(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withManifest: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Icon`](../-icon/index.md)`>`

Analyzes the HTML of the designated URL and extract the icon information specified by the link tag.

**Do not call from main thread** to avoid NetworkOnMainThreadException

Download the HTML file to the [downloadLimit](download-limit.md) size
and analyze the `<link>` tag within that range.

### Parameters

`siteUrl` - URL of analysis target page

`withManifest` - true: extract Web App Manifest icon too, if it exists. false: only from link tag