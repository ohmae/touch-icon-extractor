/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.html.simple

import net.mm2d.touchicon.HtmlElement
import net.mm2d.touchicon.HtmlParser

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class SimpleHtmlParser : HtmlParser {
    override fun extractLinkElements(html: String): List<HtmlElement> {
        return extractElementList(html).filter { it.name.equals("link", true) }
    }

    private fun extractElementList(html: String): List<SimpleHtmlElement> {
        val result = mutableListOf<SimpleHtmlElement>()
        val a = html.toCharArray()
        var i = 0
        while (i < a.size) {
            if (a[i] != '<') {
                i++
                continue
            }
            if (match(a, i + 1, "!--")) {
                i = skipComment(a, i + 4)
                continue
            }
            if (a.size > i + 1 && a[i + 1] == '/') {
                i = skipEndTag(a, i + 2)
                continue
            }
            i++
            val nameEnd = skipName(a, i)
            if (nameEnd == i || nameEnd >= a.size) {
                continue
            }
            val name = String(a, i, nameEnd - i)
            if (!a[nameEnd].isWhitespace() && a[nameEnd] != '/' && a[nameEnd] != '>') {
                continue
            }
            i = nameEnd
            val attrs = mutableListOf<Pair<String, String>>()
            while (i < a.size) {
                i = skipSpace(a, i)
                if (a[i] == '/' || a[i] == '>') {
                    break
                }
                val attrNameEnd = skipName(a, i)
                if (attrNameEnd == i || attrNameEnd >= a.size || a[attrNameEnd] != '=') {
                    break
                }
                val attrName = String(a, i, attrNameEnd - i)
                i = attrNameEnd + 1
                if (i >= a.size) break
                val c = a[i]
                if (c.isWhitespace()) break
                val quote = c == '"' || c == '\''
                val attrValueEnd = if (quote) {
                    i++
                    skipQuoteValue(a, i, c)
                } else {
                    skipNonQuoteValue(a, i)
                }
                if (attrValueEnd == i || attrValueEnd >= a.size) {
                    break
                }
                val attrValue = String(a, i, attrValueEnd - i)
                attrs.add(attrName to attrValue)
                i = attrValueEnd
                if (quote) i++
            }
            result.add(SimpleHtmlElement(name, attrs))
            i++
        }
        return result
    }

    private fun match(a: CharArray, offset: Int, pattern: String): Boolean {
        val p = pattern.toCharArray()
        if (a.size < offset + p.size) return false
        for (i in p.indices) {
            if (a[offset + i] != p[i]) return false
        }
        return true
    }

    private fun skipComment(a: CharArray, start: Int): Int {
        return skip(a, start) { match(a, it, "-->") }
    }

    private fun skipEndTag(a: CharArray, start: Int): Int {
        return skip(a, start) { a[it] == '>' }
    }

    private fun skipSpace(a: CharArray, start: Int): Int {
        return skip(a, start) { !a[it].isWhitespace() }
    }

    private fun skipName(a: CharArray, start: Int): Int {
        return skip(a, start) { !a[it].isLetterOrDigit() }
    }

    private fun skipNonQuoteValue(a: CharArray, start: Int): Int {
        return skip(a, start) { a[it].isWhitespace() || a[it] == '>' }
    }

    private inline fun skip(a: CharArray, start: Int, breakCondition: (Int) -> Boolean): Int {
        for (i in start until a.size) {
            if (breakCondition(i)) return i
        }
        return a.size
    }

    private fun skipQuoteValue(a: CharArray, start: Int, quote: Char): Int {
        var escape = false
        for (i in start until a.size) {
            if (!escape && a[i] == quote) return i
            escape = if (escape) false else a[i] == '\\'
        }
        return a.size
    }
}
