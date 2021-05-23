package net.mm2d.touchicon

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@Suppress("TestFunctionName")
class RelationshipTest {
    @Test
    fun of() {
        assertThat(Relationship.of(null)).isNull()
        assertThat(Relationship.of("")).isNull()
        assertThat(Relationship.of("icon")).isEqualTo(Relationship.ICON)
    }
}
