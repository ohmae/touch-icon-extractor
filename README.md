# ![](.idea/icon.png) touch-icon-extractor
[![license](https://img.shields.io/github/license/ohmae/touch-icon-extractor.svg)](./LICENSE)
[![GitHub release](https://img.shields.io/github/release/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/releases)
[![GitHub issues](https://img.shields.io/github/issues/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/issues)
[![GitHub closed issues](https://img.shields.io/github/issues-closed/ohmae/touch-icon-extractor.svg)](https://github.com/ohmae/touch-icon-extractor/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub Actions](https://github.com/ohmae/touch-icon-extractor/actions/workflows/ci.yml/badge.svg)](https://github.com/ohmae/touch-icon-extractor/actions)
[![codecov](https://codecov.io/gh/ohmae/touch-icon-extractor/branch/develop/graph/badge.svg)](https://codecov.io/gh/ohmae/touch-icon-extractor)
[![Maven Central](https://img.shields.io/maven-central/v/net.mm2d.touchicon/touchicon)](https://search.maven.org/artifact/net.mm2d.touchicon/touchicon)

This is a library to extract WebClip icon information from the website.
Available in **JVM** and **Android** as this is written in pure **Kotlin**.

## Sample App

This app is in [touch-icon-extractor-sample](https://github.com/ohmae/touch-icon-extractor-sample)
And also published in [Play store](https://play.google.com/store/apps/details?id=net.mm2d.webclip)

|![](readme/screenshot1.png)|![](readme/screenshot2.png)|![](readme/screenshot3.png)|
|-|-|-|

## Library structure

- `net.mm2d:touchicon:` core component. All feature is provided by this. Use `UrlConnection` for HTTP access and its own parser for HTML parse.
- `net.mm2d:touchicon-http-okhttp:` Adapter to use OkHttp for HTTP access.

`net.mm2d:touchicon-html-jsoup:` is **EOL**. *The last version is [0.8.3](https://github.com/ohmae/touch-icon-extractor/releases/tag/v0.8.3)*

## How to use

jCenter will close in May. In 0.9.1 moved to mavenCentral from jcenter.  
Please note that the **groupID has changed**

Download from mavenCentral.  
The latest version is:
![Maven Central](https://img.shields.io/maven-central/v/net.mm2d.touchicon/touchicon)

```gradle
dependencies {
    implementation("net.mm2d.touchicon:touchicon:$touchIconVersion")
    implementation("net.mm2d.touchicon:touchicon-http-okhttp:$touchIconVersion") // Optional: If use OkHttp for HTTP access
}
```

Versions below 0.9.1 were distributed with jCenter.
However, jCenter will close and old versions are not migrated to mavenCentral.
If you need an older version, please use the Github Pages repository.

```gradle
repositories {
    maven { url = URI("https://ohmae.github.com/maven") }
}
```

```gradle
dependencies {
    implementation("net.mm2d:touchicon:$touchIconVersion")
    implementation("net.mm2d:touchicon-http-okhttp:$touchIconVersion") // Optional: If use OkHttp for HTTP access
}
```

## API document

Documentation comment is written in KDoc.

- [touchicon](https://ohmae.github.io/touch-icon-extractor/touchicon/)
- [touchicon-http-okhttp](https://ohmae.github.io/touch-icon-extractor/http-okhttp/)

### Sample code

```kotlin
val extractor = TouchIconExtractor()                    // initialize
extractor.userAgent = "user agent string"               // option: set User-Agent
extractor.headers = mapOf("Cookie" to "hoge=fuga")      // option: set additional HTTP header
extractor.downloadLimit = 10_000                        // option: set download limit (default 64kB).
                                                        // <= 0 means no limit
//...
GlobalScope.launch(Dispatchers.Main) {
    val job = async(Dispatchers.IO) {
        extractor.fromPage(siteUrl, true)               // Do not call from the Main thread
    }
    //...
}
```

If in RxJava

```kotlin
//...
Single.fromCallable { extractor.fromPage(url, true) }   // Do not call from the Main thread
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            //...
        }, {})
```

By default, this use HttpUrlConnection for HTTP access.
If you want to use OkHttp, use touchicon-http-okhttp module.

```kotlin
val extractor = TouchIconExtractor(
    httpClient = OkHttpAdapterFactory.create(OkHttpClient())
)
```

#### HTTP Session

You may want to use communication in the same session as other communication.
You need to use the same cookie in WebView and HTTP session of this library.
For example, to use the same session as WebView in an Android application,

For the default HTTP client using HttpUrlConnection, implement CookieHandler.

```kotlin
object WebViewCookieHandler : CookieHandler {
    private val cookieManager = CookieManager.getInstance()

    override fun saveCookie(url: String, value: String) {
        cookieManager.setCookie(url, value)
    }

    override fun loadCookie(url: String): String? = cookieManager.getCookie(url)
}
```

```kotlin
TouchIconExtractor(
    httpClient = SimpleHttpClientAdapterFactory.create(WebViewCookieHandler)
)
```

For OkHttp, set CookieJar in OkHttpClient as you know.

```kotlin
object WebViewCookieJar : CookieJar {
    private val cookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlString = url.toString()
        cookies.forEach {
            cookieManager.setCookie(urlString, it.toString())
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> =
        cookieManager.getCookie(url.toString()).let { cookie ->
            if (cookie.isNullOrEmpty()) {
                emptyList()
            } else {
                cookie.split(";")
                    .filter { it.isNotBlank() }
                    .mapNotNull { Cookie.parse(url, it) }
            }
        }
}
```

```kotlin
TouchIconExtractor(
    httpClient = OkHttpAdapterFactory.create(
        OkHttpClient.Builder()
            .cookieJar(WebViewCookieJar)
            .build()
    )
)
```

## Operating principle

There are two kinds of methods for specifying the WebClip icon.
This library supports both.

### Icon associated with the wab page

Specify the following description in the HTML header.

```html
<link rel="icon" href="/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/favicon.ico">
<link rel="apple-touch-icon" href="/apple-touch-icon.png" sizes="57x57">
<link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png" sizes="80x80">
```

If you want this information, as following

```kotlin
extractor.fromPage(url)
```

This library attempts to download an HTML file from the specified URL.
Since only the header is required, if the download size is larger than a certain size, the download is stopped there.

Analyzing the downloaded HTML file,
Extract only link tags whose rel attribute is
"icon", "shortcut icon", "apple-touch-icon", "apple-touch-icon-precomposed".
Parse it, create an `PageIcon` instance, and return it as a result.

### Web App Manifest

Although not strictly a WebClip icon, this can also get an icon written in the Web App Manifest.

This is described by the following JSON.

```json
{
  "short_name": "name",
  "name": "Web App Icon",
  "icons": [
    {
      "src": "icon-1x.png",
      "type": "image/png",
      "sizes": "48x48"
    },
    {
      "src": "icon-2x.png",
      "type": "image/png",
      "sizes": "96x96"
    },
    {
      "src": "icon-4x.png",
      "type": "image/png",
      "sizes": "192x192"
    }
  ],
  "start_url": "index.html"
}
```

And it is described as follows in HTML.

```html
<link rel="manifest" href="/manifest.json">
```

This information is expressed as `WebAppIcon`.

If you want this information, as following

```kotlin
extractor.fromPage(url, true)
```

As you guessed, it gets at the same time as PageIcon.

### Icon associated with the Domain

Simply putting a file with a fixed name like "favicon.ico" in the root of the domain.
Whether an icon exists or not can not be known until you try HTTP communication.

This is an inefficient, but there are Web sites that are still deployed in this way.
You should try only if you can not get it by the method in the previous section.
_Please be aware that this method can be annoying to the website administrator._

If you want this information, as following

```kotlin
extractor.fromDomain(url)
```

It checks whether the file exists, and returns the information if it exists.

The order of checking the existence of the icon is as follows

1. apple-touch-icon-precomposed.png
2. apple-touch-icon.png
3. favicon.ico

If the file exists, the subsequent files will not be checked.

If you do not need precomposed, as following

```kotlin
extractor.fromDomain(url, false)
```

The order of checking the existence of the icon is as follows
1. apple-touch-icon.png
1. favicon.ico

Sometimes the size information is included in the name, such as "apple-touch-icon-120x120.png"

When

```kotlin
extractor.fromDomain(url, true, listOf("120x120", "72x72"))
```

The order of checking the existence of the icon is as follows
1. apple-touch-icon-120x120-precomposed.png
2. apple-touch-icon-120x120.png
3. apple-touch-icon-72x72-precomposed.png
4. apple-touch-icon-72x72.png
5. apple-touch-icon-precomposed.png
6. apple-touch-icon.png
7. favicon.ico

There are methods to gather all the information (`TouchIconExtractor#listFromDomain()`)
This is for debugging and verification, **strongly recommended not to use in production.**.

### Comparison of icons

Often you can get more than one icon.
Which is the most appropriate icon depends on the application, but this library provides several Comparator.

```kotlin
val icons = extractor.fromDomain(url, true, listOf("120x120", "72x72"))
val bestIcon1 = icons.maxWith(IconComparator.SIZE)     // Compare by size. (the largest icon is the best)
val bestIcon2 = icons.maxWith(IconComparator.REL_SIZE) // Compare by rel, if same, compare by size
```

## Dependent OSS

### touchicon

- [Kotlin](https://kotlinlang.org/)
  - kotlin-stdlib

### touchicon-http-okhttp

- [Kotlin](https://kotlinlang.org/)
  - kotlin-stdlib
- [OkHttp3](https://square.github.io/okhttp/)

## Author
大前 良介 (OHMAE Ryosuke)
http://www.mm2d.net/

## License
[MIT License](./LICENSE)
