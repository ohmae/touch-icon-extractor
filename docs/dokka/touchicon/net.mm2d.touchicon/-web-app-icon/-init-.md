[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [WebAppIcon](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`WebAppIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``WebAppIcon(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, density: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`

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

