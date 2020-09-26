package build

object ProjectProperties {
    const val groupId: String = "net.mm2d"

    private const val versionMajor: Int = 0
    private const val versionMinor: Int = 8
    private const val versionPatch: Int = 2
    const val versionName: String = "$versionMajor.$versionMinor.$versionPatch"

    object Url {
        const val site: String = "https://github.com/ohmae/touch-icon-extractor"
        const val github: String = "https://github.com/ohmae/touch-icon-extractor"
        const val scm: String = "scm:git:https://github.com/ohmae/touch-icon-extractor.git"
    }
}
