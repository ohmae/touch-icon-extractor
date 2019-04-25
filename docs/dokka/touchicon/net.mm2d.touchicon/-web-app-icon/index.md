[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [WebAppIcon](./index.md)

# WebAppIcon

`data class WebAppIcon : `[`Icon`](../-icon/index.md)`, `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

Icon information described in Web App Manifest.

This is described by the following JSON.

``` json
{
  "short_name": "AirHorner",
  "name": "Kinlan's AirHorner of Infamy",
  "icons": [
    {
      "src": "launcher-icon-1x.png",
      "type": "image/png",
      "sizes": "48x48"
    },
    {
      "src": "launcher-icon-2x.png",
      "type": "image/png",
      "sizes": "96x96"
    },
    {
      "src": "launcher-icon-4x.png",
      "type": "image/png",
      "sizes": "192x192"
    }
  ],
  "start_url": "index.html?launcher=true"
}
```

And it is described as follows in HTML.

``` html
<link rel="manifest" href="/manifest.json">
```

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Types

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r/index.md) | `companion object CREATOR : `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`WebAppIcon`](./index.md)`>` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WebAppIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``WebAppIcon(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, density: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>Icon information described in Web App Manifest. |

### Properties

| Name | Summary |
|---|---|
| [density](density.md) | `val density: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>pixel densities. |
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
| [inferSize](infer-size.md) | `fun inferSize(): `[`Size`](../-size/index.md)<br>Infer display size of this icon from sizes value. |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [inferArea](../-icon/infer-area.md) | `open fun inferArea(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Infer area of this icon. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [createFromParcel](create-from-parcel.md) | `fun createFromParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`): `[`WebAppIcon`](./index.md) |
| [newArray](new-array.md) | `fun newArray(size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`WebAppIcon`](./index.md)`?>` |
