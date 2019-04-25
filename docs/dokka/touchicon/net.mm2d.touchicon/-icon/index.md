[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [Icon](./index.md)

# Icon

`interface Icon`

Icon information interface.

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Properties

| Name | Summary |
|---|---|
| [length](length.md) | `abstract val length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Icon file length. |
| [mimeType](mime-type.md) | `abstract val mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Icon MIME type. e.g. "image/png" |
| [precomposed](precomposed.md) | `abstract val precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>true if this is for a precomposed touch icon. |
| [rel](rel.md) | `abstract val rel: `[`Relationship`](../-relationship/index.md)<br>Relationship between icon and page. |
| [sizes](sizes.md) | `abstract val sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Size information, assumed format is (width)x(height). e.g. "80x80". |
| [url](url.md) | `abstract val url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Icon url. |

### Functions

| Name | Summary |
|---|---|
| [inferArea](infer-area.md) | `open fun inferArea(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Infer area of this icon. |
| [inferSize](infer-size.md) | `abstract fun inferSize(): `[`Size`](../-size/index.md)<br>Infer display size of this icon. |

### Inheritors

| Name | Summary |
|---|---|
| [DomainIcon](../-domain-icon/index.md) | `data class DomainIcon : `[`Icon`](./index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)<br>Icon information associated with the Web site domain. |
| [PageIcon](../-page-icon/index.md) | `data class PageIcon : `[`Icon`](./index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)<br>Icon information associated with the Web page. |
| [WebAppIcon](../-web-app-icon/index.md) | `data class WebAppIcon : `[`Icon`](./index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)<br>Icon information described in Web App Manifest. |
