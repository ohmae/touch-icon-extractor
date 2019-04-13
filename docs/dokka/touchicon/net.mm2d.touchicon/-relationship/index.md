[touchicon](../../index.md) / [net.mm2d.touchicon](../index.md) / [Relationship](./index.md)

# Relationship

`enum class Relationship`

enum of relationship between icon and page.

Express the value of rel of the link tag.

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Enum Values

| Name | Summary |
|---|---|
| [APPLE_TOUCH_ICON_PRECOMPOSED](-a-p-p-l-e_-t-o-u-c-h_-i-c-o-n_-p-r-e-c-o-m-p-o-s-e-d.md) | This expresses that the rel value of the link tag is "apple-touch-icon-precomposed". |
| [APPLE_TOUCH_ICON](-a-p-p-l-e_-t-o-u-c-h_-i-c-o-n.md) | This expresses that the rel value of the link tag is "apple-touch-icon". |
| [ICON](-i-c-o-n.md) | This expresses that the rel value of the link tag is "icon". |
| [SHORTCUT_ICON](-s-h-o-r-t-c-u-t_-i-c-o-n.md) | This expresses that the rel value of the link tag is "shortcut icon". |
| [MANIFEST](-m-a-n-i-f-e-s-t.md) | Used to represent the icon described in Web App Manifest. |

### Properties

| Name | Summary |
|---|---|
| [priority](priority.md) | `val priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Priority to expect quality. |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Raw value of rel. |
