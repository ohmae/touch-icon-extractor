[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [PageIcon](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PageIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``PageIcon(rel: `[`Relationship`](../-relationship/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = rel == Relationship.APPLE_TOUCH_ICON_PRECOMPOSED, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = -1)`

Icon information associated with the Web page.

e.g.
Assuming that the URL of the site is https://www.example.com/index.html

When the HTML tag is

``` html
link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="120x120">
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

