[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [TouchIconExtractor](index.md) / [fromDomain](./from-domain.md)

# fromDomain

`@WorkerThread fun fromDomain(siteUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, withPrecomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, sizes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyList()): `[`Icon`](../-icon/index.md)`?`

Get valid icon information,
by trying to access to some icon from the root of domain of specified URL.

**Do not call from main thread** to avoid NetworkOnMainThreadException

Judge the existence of icon using HTTP HEAD.
Since many of these sites do not have these icons, 404 errors are recorded on the server.
Because it is a bad manners, execution should be kept to a minimum.

Checks the existence of the icon in order,
and returns the information when judging that the icon exists.
Processing is interrupted at the stage of finding the icon, and no confirmation is made on it after that

The order of checking the existence of the icon is as follows

In case of `withPrecomposed` is false and `sizes` is not specify.

1. apple-touch-icon.png
2. favicon.ico

In case of `withPrecomposed` is true and `sizes` is not specify.

1. apple-touch-icon-precomposed.png
2. apple-touch-icon.png
3. favicon.ico

In case of `withPrecomposed` is true and `sizes` is `listOf("120x120", "72x72")`

1. apple-touch-icon-120x120-precomposed.png
2. apple-touch-icon-120x120.png
3. apple-touch-icon-72x72-precomposed.png
4. apple-touch-icon-72x72.png
5. apple-touch-icon-precomposed.png
6. apple-touch-icon.png
7. favicon.ico

### Parameters

`siteUrl` - URL of analysis target page

`withPrecomposed` - If specify true, include the "precomposed". default is true

`sizes` - Specify this when appending size (e.g. 80x80) to apple-touch-icon. Default is empty.