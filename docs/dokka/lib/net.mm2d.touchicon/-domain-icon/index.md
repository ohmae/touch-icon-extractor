[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [DomainIcon](./index.md)

# DomainIcon

`data class DomainIcon : `[`Icon`](../-icon/index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Icon information associated with the Web site domain.

e.g.
Assuming that the URL of the site is https://www.example.com/index.html

When the file name is "apple-touch-icon-120x120-precomposed.png",
the following values are stored

```
rel=ICON
url=https://www.example.com/favicon.ico
sizes=120x120
mimeType=image/x-icon   <- Content-Type value of the response from the server
precomposed=false
length=99999            <- Content-Length value of the response from the server
```

When the file name is "favicon.con",
the following values are stored

```
rel=ICON
url=https://www.example.com/favicon.ico
sizes=                  <- empty
mimeType=image/x-icon   <- Content-Type value of the response from the server
precomposed=false
length=99999            <- Content-Length value of the response from the server
```

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Types

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r/index.md) | `companion object CREATOR : `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`DomainIcon`](./index.md)`>` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DomainIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``DomainIcon(rel: `[`Relationship`](../-relationship/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>Icon information associated with the Web site domain. |

### Properties

| Name | Summary |
|---|---|
| [length](length.md) | `val length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Icon file length. |
| [mimeType](mime-type.md) | `val mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Icon MIME type. e.g. "image/png" |
| [precomposed](precomposed.md) | `val precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>true if this is for a precomposed touch icon. |
| [rel](rel.md) | `val rel: `[`Relationship`](../-relationship/index.md)<br>Relationship between icon and page. |
| [sizes](sizes.md) | `val sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Size information, assumed format is (width)x(height). e.g. "80x80". |
| [url](url.md) | `val url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Icon url. |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [inferSize](infer-size.md) | `fun inferSize(): `[`Point`](https://developer.android.com/reference/android/graphics/Point.html)<br>Infer display size of this icon from sizes value. |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [inferArea](../-icon/infer-area.md) | `open fun inferArea(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Infer area of this icon. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [createFromParcel](create-from-parcel.md) | `fun createFromParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`): `[`DomainIcon`](./index.md) |
| [newArray](new-array.md) | `fun newArray(size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`DomainIcon`](./index.md)`?>` |
