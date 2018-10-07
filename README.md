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

## Sample code

```kotlin
val extractor = TouchIconExtractor(OkHttpClient())  // initialize with OkHttpClient instance
extractor.userAgent = "user agent string"           // option: set User-Agent
extractor.headers = mapOf("Cookie" to "hoge=fuga")  // option: set additional HTTP header
extractor.downloadLimit = 10_000                    // option: set download limit (default 64kB). <= 0 means no limit 
...
Single.fromCallable { extractor.fromPage(url) }      // Do not call from the Main thread
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ 
            ...
        }, {})
```

# Dependent OSS

- [Kotlin](https://kotlinlang.org/)
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/)
  - support-annotations
- [Jsoup](https://jsoup.org/)
- [OkHttp3](https://square.github.io/okhttp/)

## sample app
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
