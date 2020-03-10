/*
 * Copyright (c) 2020 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.touchicon.json

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class JsonParserTest {
    @Test
    fun parse() {
        val result = JsonParser(
            """{
            |"bool1": true,
            |"bool2": false,
            |"null": null,
            |"int": 1,
            |"float1": 1.0,
            |"float2": 1.0e2,
            |"string": "string",
            |"array": [
            |  "a",
            |  "b",
            |  "c"
            |],
            |"object": {
            |  "a": "b"
            |},
            |"empty array": [],
            |"object array": [
            |{}
            |],
            |"escape": "\r\n\b\t\\\u0000\u0001"
            |}""".trimMargin()
        ).parse()
        assertThat(result.get<Boolean>("bool1")).isTrue()
        assertThat(result.get<Boolean>("bool2")).isFalse()
        assertThat(result.get<Any?>("null")).isNull()
        assertThat(result.get<Int>("int")).isEqualTo(1)
        assertThat(result.get<Double>("float1")).isEqualTo(1.0)
        assertThat(result.get<Double>("float2")).isEqualTo(1.0e2)
        assertThat(result.get<String>("string")).isEqualTo("string")
        assertThat(result.getOrDefault("string2", "default")).isEqualTo("default")
        assertThat(result.get<JsonArray>("array")).hasSize(3)
        assertThat(result.get<JsonArray>("array")).containsExactly("a", "b", "c")
        assertThat(result.get<JsonObject>("object").get<String>("a")).isEqualTo("b")
        assertThat(result.get<JsonArray>("empty array")).isEmpty()
        assertThat(
            result.get<JsonArray>("object array").first()
        ).isInstanceOf(JsonObject::class.java)
        assertThat(result.get<String>("escape")).isEqualTo("\r\n\b\t\\\u0000\u0001")
    }

    @Test(expected = JsonException::class)
    fun parse_invalid_literal() {
        JsonParser("""{"bool": true1}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_extra_comma1() {
        JsonParser("""{"bool": true,}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_extra_comma2() {
        JsonParser("""{"array": [],}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_extra_comma3() {
        JsonParser("""{"array": [1,]}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_unterminated_object() {
        JsonParser("""{"bool": true""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_unterminated_array() {
        JsonParser("""{"array": [""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_unquote_name() {
        JsonParser("""{array: []""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_no_colon() {
        JsonParser("""{"bool" true""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_no_comma1() {
        JsonParser("""{"bool": true "string": "string"}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_no_comma2() {
        JsonParser("""{"array": [1,2 3]}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_no_start() {
        JsonParser(""""bool" true}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_unterminated_string() {
        JsonParser("""{"bool""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_invalid_unicode1() {
        JsonParser("""{"string": "\u111"}""").parse()
    }

    @Test(expected = JsonException::class)
    fun parse_invalid_unicode2() {
        JsonParser("""{"string": "\uXXXX"}""").parse()
    }
}
