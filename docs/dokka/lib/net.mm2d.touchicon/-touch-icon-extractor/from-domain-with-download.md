[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [TouchIconExtractor](index.md) / [fromDomainWithDownload](./from-domain-with-download.md)

# fromDomainWithDownload

`@WorkerThread fun fromDomainWithDownload(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`DomainIcon`](../-domain-icon/index.md)`, `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`>?`

It performs the same processing as [fromDomain](from-domain.md) using HTTP GET, and downloads binary if icon exists.

**Do not call from main thread** to avoid NetworkOnMainThreadException

Since [fromDomain](from-domain.md) get only the icon information,
the binary data of the icon needs to be downloaded again using HTTP GET.
In this method, HTTP GET is used to confirm the existence of the icon,
and if there is an icon, download is also done.
The result is returned with icon information and binary Pair.

Do not call from main thread.

### Parameters

`siteUrl` - URL of analysis target page

`withPrecomposed` - If specify true, include the "precomposed". default is true

`sizes` - Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.