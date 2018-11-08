[lib](../../index.md) / [net.mm2d.touchicon](../index.md) / [IconComparator](./index.md)

# IconComparator

`object IconComparator`

Comparator for sorting based on quality.

To get the icon with the highest quality.

If you give priority to size

``` kotlin
val bestIcon: Icon? = TouchIconExtractor.fromPage(url)
    .maxWith(IconComparator.SIZE_REL)
```

If you give priority to type(relationship)

``` kotlin
val bestIcon: Icon? = TouchIconExtractor.fromPage(url)
    .maxWith(IconComparator.REL_SIZE)
```

**Author**
[大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)

### Properties

| Name | Summary |
|---|---|
| [REL_SIZE](-r-e-l_-s-i-z-e.md) | `val REL_SIZE: `[`Comparator`](https://developer.android.com/reference/java/util/Comparator.html)`<`[`Icon`](../-icon/index.md)`>`<br>Comparator for order by rel -&gt; size |
| [SIZE_REL](-s-i-z-e_-r-e-l.md) | `val SIZE_REL: `[`Comparator`](https://developer.android.com/reference/java/util/Comparator.html)`<`[`Icon`](../-icon/index.md)`>`<br>Comparator for order by size -&gt; rel |
