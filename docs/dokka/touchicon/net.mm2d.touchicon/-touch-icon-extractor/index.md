[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [TouchIconExtractor](./index.md)

# TouchIconExtractor

`class TouchIconExtractor`

Extract information of WebClip icon such as Apple Touch Icon or favicon related to the URL.

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TouchIconExtractor(httpAdapter: `[`HttpAdapter`](../-http-adapter/index.md)` = SimpleHttpAdapterFactory.create(), htmlParser: `[`HtmlParser`](../-html-parser/index.md)` = SimpleHtmlParserFactory.create())`<br>Initialize the instance. You can change HttpAdapter and HtmlParser. |

### Properties

| Name | Summary |
|---|---|
| [downloadLimit](download-limit.md) | `var downloadLimit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Specify the maximum download size when downloading HTML file. |
| [headers](headers.md) | `var headers: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Specify the HTTP communication header. |
| [userAgent](user-agent.md) | `var userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Specify the value of User-Agent used for HTTP communication. |

### Functions

| Name | Summary |
|---|---|
| [fromDomain](from-domain.md) | `fun fromDomain(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`DomainIcon`](../-domain-icon/index.md)`?`<br>Get valid icon information, by trying to access to some icon from the root of domain of specified URL. |
| [fromDomainWithDownload](from-domain-with-download.md) | `fun fromDomainWithDownload(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`DomainIcon`](../-domain-icon/index.md)`, `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`>?`<br>It performs the same processing as [fromDomain](from-domain.md) using HTTP GET, and downloads binary if icon exists. |
| [fromPage](from-page.md) | `fun fromPage(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PageIcon`](../-page-icon/index.md)`>`<br>Analyzes the HTML of the designated URL and extract the icon information specified by the link tag. |
| [listFromDomain](list-from-domain.md) | `fun listFromDomain(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DomainIcon`](../-domain-icon/index.md)`>`<br>Processing similar to [fromDomain](from-domain.md) is performed, but even if an icon is found, processing is not stopped but a list of all existing icon information is returned. |
