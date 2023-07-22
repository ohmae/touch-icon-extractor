package build

object ProjectProperties {
    const val groupId: String = "net.mm2d.touchicon"
    const val name: String = "touchicon"
    const val description: String = "This is a library to extract WebClip icon information from the website. Available in JVM and Android as this is written in pure Kotlin."
    const val developerId: String = "ryo"
    const val developerName: String = "ryosuke"

    private const val versionMajor: Int = 0
    private const val versionMinor: Int = 9
    private const val versionPatch: Int = 7
    const val versionName: String = "$versionMajor.$versionMinor.$versionPatch"

    object Url {
        const val site: String = "https://github.com/ohmae/touch-icon-extractor"
        const val github: String = "https://github.com/ohmae/touch-icon-extractor"
        const val scm: String = "scm:git@github.com:ohmae/touch-icon-extractor.git"
    }
}
