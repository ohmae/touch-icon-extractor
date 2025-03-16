/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html

/**
 * Html Parser.
 */
internal class HtmlParser {
    /**
     * Extract the link tag information.
     *
     * @return link tags
     */
    fun extractLinkTags(
        html: String,
    ): List<HtmlTag> = extractElementList(html).filter { it.name.equals("link", true) }

    // VisibleForTesting
    internal fun extractElementList(
        html: String,
    ): List<HtmlTag> {
        val result = mutableListOf<HtmlTag>()
        val a = html.toCharArray()
        var i = 0
        while (i < a.size) {
            if (a[i] != '<') {
                i++
                continue
            }
            if (match(a, i + 1, "!--")) { // skip comment
                i = skip(a, i + 4) { match(a, it, "-->") }
                continue
            }
            if (a.size > i + 1 && a[i + 1] == '/') { // skip close tag
                i = skip(a, i + 2) { a[it] == '>' }
                continue
            }
            val tagNameEnd = skip(a, ++i) { !a[it].isNameCharacter() }
            if (tagNameEnd == i || tagNameEnd >= a.size) {
                continue
            }
            val tagName = String(a, i, tagNameEnd - i)
            if (a[tagNameEnd].let { !it.isWhitespace() && it != '/' && it != '>' }) {
                continue
            }
            i = tagNameEnd
            val attrs = mutableListOf<Pair<String, String>>()
            while (i < a.size) {
                i = skip(a, i) { !a[it].isWhitespace() }
                if (i >= a.size || a[i] == '/' || a[i] == '>') {
                    break
                }
                val attrNameEnd = skip(a, i) { !a[it].isNameCharacter() }
                if (attrNameEnd == i || attrNameEnd >= a.size) break
                val attrName = String(a, i, attrNameEnd - i)
                i = attrNameEnd
                if (a[i] != '=') {
                    attrs.add(attrName to "")
                    continue
                }
                if (++i >= a.size) break
                val c = a[i]
                if (c.isWhitespace()) continue
                val quote = c == '"' || c == '\''
                val attrValueEnd = if (quote) {
                    skipQuoteValue(a, ++i, c)
                } else {
                    skip(a, i) { a[it].isWhitespace() || a[it] == '>' }
                }
                if (attrValueEnd == i || attrValueEnd >= a.size) {
                    break
                }
                val attrValue = String(a, i, attrValueEnd - i)
                attrs.add(attrName to attrValue)
                i = attrValueEnd
                if (quote) i++
            }
            result.add(HtmlTag(tagName, attrs))
            i++
        }
        return result
    }

    private fun Char.isNameCharacter(): Boolean = this.isLetterOrDigit() || ":-_.".contains(this)

    private fun match(
        a: CharArray,
        offset: Int,
        pattern: String,
    ): Boolean {
        val p = pattern.toCharArray()
        if (a.size < offset + p.size) return false
        return p.indices.none { a[offset + it] != p[it] }
    }

    private fun skip(
        a: CharArray,
        start: Int,
        breakCondition: (Int) -> Boolean,
    ): Int {
        for (i in start until a.size) {
            if (breakCondition(i)) return i
        }
        return a.size
    }

    private fun skipQuoteValue(
        a: CharArray,
        start: Int,
        quote: Char,
    ): Int {
        var escape = false
        for (i in start until a.size) {
            if (!escape && a[i] == quote) return i
            escape = !escape && a[i] == '\\'
        }
        return a.size
    }
}
