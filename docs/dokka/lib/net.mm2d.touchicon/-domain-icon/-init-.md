[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [DomainIcon](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DomainIcon(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)``DomainIcon(rel: `[`Relationship`](../-relationship/index.md)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sizes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mimeType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, precomposed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`

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

