[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [HttpAdapter](./index.md)

# HttpAdapter

`interface HttpAdapter`

Interface of HTTP Client

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Properties

| Name | Summary |
|---|---|
| [headers](headers.md) | `abstract var headers: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Specify the HTTP communication header. |
| [userAgent](user-agent.md) | `abstract var userAgent: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Specify the value of User-Agent used for HTTP communication. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract fun get(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`HttpResponse`](../-http-response/index.md)<br>Execute HTTP GET method |
| [head](head.md) | `abstract fun head(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`HttpResponse`](../-http-response/index.md)<br>Execute HTTP HEAD method |
