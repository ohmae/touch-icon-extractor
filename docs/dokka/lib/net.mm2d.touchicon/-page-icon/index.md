[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [PageIcon](./index.md)

# PageIcon

`data class PageIcon : `[`Icon`](../-icon/index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Icon information associated with the Web page.

e.g.
Assuming that the URL of the site is https://www.example.com/index.html

When the HTML tag is

``` html
<link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="120x120">
```

the following values are stored

```
rel=APPLE_TOUCH_ICON_PRECOMPOSED
url=https://www.example.com/apple-touch-icon-precomposed.png
sizes=120x120
mimeType=             <- empty
precomposed=true
length=-1             <- always -1
```

When the HTML tag is

``` html
<link rel="icon" href="/favicon.ico" type="image/x-icon">
```

the following values are stored

```
rel=ICON
url=https://www.example.com/favicon.ico
sizes=                <- empty
mimeType=image/x-icon
precomposed=false
length=-1             <- always -1
```

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Types

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r/index.md) | `companion object CREATOR : `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`PageIcon`](./index.md)`>` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PageIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``PageIcon(rel: `[`Relationship`](../-relationship/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = rel == Relationship.APPLE_TOUCH_ICON_PRECOMPOSED, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = -1)`<br>Icon information associated with the Web page. |

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

### Companion Object Functions

| Name | Summary |
|---|---|
| [createFromParcel](create-from-parcel.md) | `fun createFromParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`): `[`PageIcon`](./index.md) |
| [newArray](new-array.md) | `fun newArray(size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`PageIcon`](./index.md)`?>` |
