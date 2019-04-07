[touchicon](../../index.md) / [net.mm2d.touchicon.http.simple](../index.md) / [CookieHandler](./index.md)

# CookieHandler

`interface CookieHandler`

Cookie Handler for SimpleHttpClientAdapter.

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Functions

| Name | Summary |
|---|---|
| [loadCookie](load-cookie.md) | `abstract fun loadCookie(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Called to read cookie. |
| [saveCookie](save-cookie.md) | `abstract fun saveCookie(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when cookies are received. |
