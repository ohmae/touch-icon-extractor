[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [TouchIconExtractor](index.md) / [listFromDomain](./list-from-domain.md)

# listFromDomain

`@WorkerThread fun listFromDomain(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DomainIcon`](../-domain-icon/index.md)`>`

Processing similar to [fromDomain](from-domain.md) is performed, but even if an icon is found,
processing is not stopped but a list of all existing icon information is returned.

**Do not call from main thread** to avoid NetworkOnMainThreadException

In order to put unnecessary load on the server,
please use only for debugging and verification.
And **strongly recommend to avoid use in production**.

### Parameters

`siteUrl` - URL of analysis target page

`withPrecomposed` - If specify true, include the "precomposed". default is true

`sizes` - Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.