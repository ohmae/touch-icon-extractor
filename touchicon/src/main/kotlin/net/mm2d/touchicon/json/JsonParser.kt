/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.json

internal class JsonParser(
    private val input: String,
) {
    private var pos: Int = 0

    fun parse(): JsonObject =
        when (nextElementChar("unexpected end of stream")) {
            '{' -> nextObject()
            else -> throw JsonException("")
        }

    private fun nextValue(): Any? =
        when (nextElementChar("unexpected end of stream")) {
            '{' -> nextObject()

            '[' -> nextArray()

            '"' -> nextString()

            else -> {
                pos--
                nextLiteral()
            }
        }

    private fun nextObject(): JsonObject {
        if (nextElementChar("unterminated object") == '}') {
            return JsonObject()
        }
        pos--
        val map = mutableMapOf<String, Any?>()
        loop@ while (true) {
            if (nextElementChar("object format error") != '"') {
                throw JsonException("object value name error")
            }
            val name = nextString()
            if (nextElementChar("object format error") != ':') {
                throw JsonException("object value name error")
            }
            map[name] = nextValue()
            when (nextElementChar("unterminated object")) {
                '}' -> return JsonObject(map)
                ',' -> continue@loop
                else -> throw JsonException("unexpected character")
            }
        }
    }

    private fun nextArray(): JsonArray {
        if (nextElementChar("unterminated array") == ']') {
            return JsonArray()
        }
        pos--
        val list = mutableListOf<Any?>()
        loop@ while (true) {
            list.add(nextValue())
            when (nextElementChar("unterminated array")) {
                ']' -> return JsonArray(list)
                ',' -> continue@loop
            }
        }
    }

    private fun nextString(): String {
        val start = pos
        var escape = false
        while (pos < input.length) {
            val c = input[pos++]
            when {
                escape -> escape = false
                c == '\\' -> escape = true
                c == '"' -> return input.substring(start, pos - 1).unescape()
            }
        }
        throw JsonException("Unterminated string")
    }

    private fun String.unescape(): String {
        val string = this
        return buildString {
            var p = 0
            var escape = false
            while (p < string.length) {
                val c = string[p++]
                when {
                    escape -> {
                        escape = false
                        when (c) {
                            'u' -> {
                                if (p + 4 > string.length) {
                                    throw JsonException("unterminated unicode")
                                }
                                val code = string.substring(p, p + 4).toIntOrNull(16)
                                    ?: throw JsonException("escaped unicode error")
                                p += 4
                                append(code.toChar())
                            }

                            't' -> append('\t')

                            'n' -> append('\n')

                            'r' -> append('\r')

                            'b' -> append('\b')

                            else -> append(c)
                        }
                    }

                    c == '\\' -> escape = true

                    else -> append(c)
                }
            }
        }
    }

    private fun nextLiteral(): Any? {
        val literal = extractLiteral()
        when (literal) {
            "null" -> return null
            "true" -> return true
            "false" -> return false
        }
        return if (literal.contains('.')) {
            literal.toDoubleOrNull()
        } else {
            literal.toIntOrNull()
        } ?: throw JsonException("unexpected literal [$literal]")
    }

    private fun extractLiteral(): String {
        val start = pos
        while (pos < input.length) {
            val c = input[pos++]
            if (LITERAL_DELIMITER.contains(c)) {
                pos--
                return input.substring(start, pos)
            }
        }
        return input.substring(start)
    }

    private fun nextElementChar(
        errorMessage: String,
    ): Char {
        while (pos < input.length) {
            val c = input[pos++]
            if (!SPACE.contains(c)) return c
        }
        throw JsonException(errorMessage)
    }

    companion object {
        private val SPACE = " \t\r\n".toSet()
        private val LITERAL_DELIMITER = " \t\r\n}],".toSet()
    }
}
