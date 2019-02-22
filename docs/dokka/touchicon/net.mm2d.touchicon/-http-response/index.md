[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [HttpResponse](./index.md)

# HttpResponse

`interface HttpResponse : `[`Closeable`](https://developer.android.com/reference/java/io/Closeable.html)

Interface of HTTP response

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Properties

| Name | Summary |
|---|---|
| [isSuccess](is-success.md) | `abstract val isSuccess: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>true means success response |

### Functions

| Name | Summary |
|---|---|
| [bodyBytes](body-bytes.md) | `abstract fun bodyBytes(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`?`<br>Read body as byteArray |
| [bodyString](body-string.md) | `abstract fun bodyString(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Read body as string |
| [header](header.md) | `abstract fun header(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Get the response header |
