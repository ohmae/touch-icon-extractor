[touchicon](../../index.md) / [net.mm2d.touchicon.http.simple](../index.md) / [CookieHandler](index.md) / [saveCookie](./save-cookie.md)

# saveCookie

`abstract fun saveCookie(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Called when cookies are received.

The value of "Set-Cookie" in the HTTP Response header is notified.
If there are multiple "Set-Cookie", it will be called each time.

### Parameters

`url` - URL

`value` - cookie value