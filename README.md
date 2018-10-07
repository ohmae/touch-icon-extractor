# touch-icon-extractor
[![license](https://img.shields.io/github/license/ohmae/touch-icon-extractor.svg)](./LICENSE)
[![GitHub release](https://img.shields.io/github/release/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/releases)
[![GitHub issues](https://img.shields.io/github/issues/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/issues)
[![GitHub closed issues](https://img.shields.io/github/issues-closed/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/issues?q=is%3Aissue+is%3Aclosed)
[![Maven Repository](https://img.shields.io/badge/maven-jcenter-brightgreen.svg)](https://bintray.com/ohmae/maven/net.mm2d.touchicon)
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/https/jcenter.bintray.com/net/mm2d/touchicon/maven-metadata.xml.svg)](https://bintray.com/ohmae/maven/net.mm2d.touchicon)

This is a library for android for extracting apple-touch-icon information from the website.

|![](readme/screenshot1.png)|![](readme/screenshot2.png)|![](readme/screenshot3.png)|
|-|-|-|

## How to use

You can download this library from jCenter.
```gradle
repositories {
    jcenter()
}
```

Add dependencies, as following.
```gradle
dependencies {
    implementation 'net.mm2d:touchicon:0.0.3'
}
```

### Sample code

```kotlin
val extractor = TouchIconExtractor(OkHttpClient())  // initialize with OkHttpClient instance
extractor.userAgent = "user agent string"           // option: set User-Agent
extractor.headers = mapOf("Cookie" to "hoge=fuga")  // option: set additional HTTP header
extractor.downloadLimit = 10_000                    // option: set download limit (default 64kB).
                                                    // <= 0 means no limit 
...
Single.fromCallable { extractor.fromPage(url) }     // Do not call from the Main thread
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ 
            ...
        }, {})
```

## Operating principle
   
There are two kinds of methods for specifying the WebClip icon.
This library is compatible with both.

### Icon associated with the HTML file

Describe the following description in the HTML header.

```html
<link rel="icon" href="/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/favicon.ico">
<link rel="apple-touch-icon" href="/apple-touch-icon.png" sizes="57x57">
<link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="80x80">
```

If you want this information, do the following

```kotlin
extractor.fromPage(url)
```

This library attempts to download an HTML file from the specified URL.
Since only the header is required, if the download size is larger than a certain size, the download is stopped there.

Analyzing the downloaded HTML file,
Extract only link tags whose rel attribute is
"icon", "shortcut icon", "apple-touch-icon", "apple-touch-icon-precomposed".
Parse it, create an `PageIcon` instance, and return it as a result.

### Icon associated with the Domain

Simply putting a file with a fixed name like "favicon.ico" in the root of the domain.
Whether an icon exists or not is not known unless HTTP communication is actually performed.
This is an inefficient method, but there are Web sites that are still deployed in this way.

By doing the following

```kotlin
extractor.fromDomain(url)
```

In order of
1. "apple-touch-icon-precomposed.png"
1. "apple-touch-icon.png"
1. "favicon.ico"
It checks whether or not the file exists, and returns the information if it exists.

If you do not need precomposed, do the following

```kotlin
extractor.fromDomain(url, false)
```
The trial order is 
1. "apple-touch-icon.png"
1. "favicon.ico"

Sometimes the size information is included in the name, such as "apple-touch-icon-120x120.png"

When describing as follows

```kotlin
extractor.fromDomain(url, true, listOf("120x120", "72x72"))
```

The trial order is 
1. "apple-touch-icon-120x120-precomposed.png"
1. "apple-touch-icon-120x120.png"
1. "apple-touch-icon-80x80-precomposed.png"
1. "apple-touch-icon-80x80.png"
1. "apple-touch-icon-precomposed.png"
1. "apple-touch-icon.png"
1. "favicon.ico"

There are methods to gather all the information (`TouchIconExtractor#listFromDomain()`)
This is for debugging and verification, **strongly not recommended for use in production environment**.

## Dependent OSS

- [Kotlin](https://kotlinlang.org/)
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/)
  - support-annotations
- [Jsoup](https://jsoup.org/)
- [OkHttp3](https://square.github.io/okhttp/)

### sample app
- [Kotlin](https://kotlinlang.org/)
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/)
  - support-v4
  - appcompat-v7
  - design
  - constraint
- [RxJava2](https://github.com/ReactiveX/RxJava)
  - [RxAndroid](https://github.com/ReactiveX/RxAndroid)
  - [RxKotlin](https://github.com/ReactiveX/RxKotlin)

## Author
大前 良介 (OHMAE Ryosuke)
http://www.mm2d.net/

## License
[MIT License](./LICENSE)
