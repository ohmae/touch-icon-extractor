package net.mm2d.build

object Projects {
    const val GROUP_ID: String = "net.mm2d.touchicon"
    const val DEVELOPER_ID: String = "ryo"
    const val DEVELOPER_NAME: String = "ryosuke"

    private const val VERSION_MAJOR: Int = 0
    private const val VERSION_MINOR: Int = 9
    private const val VERSION_PATCH: Int = 11
    const val VERSION_NAME: String = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

    object Url {
        const val SITE: String = "https://github.com/ohmae/touch-icon-extractor"
        const val GITHUB: String = "https://github.com/ohmae/touch-icon-extractor"
        const val SCM: String = "scm:git@github.com:ohmae/touch-icon-extractor.git"
    }
}
