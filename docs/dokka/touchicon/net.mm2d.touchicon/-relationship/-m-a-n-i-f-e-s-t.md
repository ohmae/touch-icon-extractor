[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [Relationship](index.md) / [MANIFEST](./-m-a-n-i-f-e-s-t.md)

# MANIFEST

`MANIFEST`

Used to represent the icon described in Web App Manifest.

This is used only in [WebAppIcon](../-web-app-icon/index.md).

This corresponds to the following description of html/json.

``` html
<link rel="manifest" href="manifest.json">
```

``` json
{
  "icons": [
    {
      "src": "icon.png",
      "type": "image/png",
      "sizes": "48x48"
    }
  ]
}
```

### Inherited Properties

| Name | Summary |
|---|---|
| [priority](priority.md) | `val priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Priority to expect quality. |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Raw value of rel. |
