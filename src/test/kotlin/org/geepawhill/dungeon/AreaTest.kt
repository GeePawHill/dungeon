package org.geepawhill.dungeon

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AreaTest {

    @Test
    fun `margin returns grown area`() {
        val area = Area(0, 0, 0, 0)
        assertThat(area.margin(1)).isEqualTo(Area(-1, -1, 1, 1))
    }

    @Test
    fun `no intersection`() {
        val area = Area(0, 0, 0, 0)
        val other = Area(1, 1, 1, 1)
        assertThat(area.intersects(other)).isFalse()
        assertThat(other.intersects(area)).isFalse()
    }

    @Test
    fun `intersects rectangle to se or nw`() {
        val area = Area(0, 0, 5, 5)
        val other = Area(4, 4, 6, 6)
        assertThat(area.intersects(other)).isTrue()
        assertThat(other.intersects(area)).isTrue()
    }

    @Test
    fun `intersects rectangle to ne or sw`() {
        val area = Area(0, 0, 5, 5)
        val other = Area(1, -1, 6, 1)
        assertThat(area.intersects(other)).isTrue()
        assertThat(other.intersects(area)).isTrue()
    }

    @Test
    fun `intersects rectangle inside or outside`() {
        val area = Area(0, 0, 5, 5)
        val other = Area(1, 1, 3, 3)
        assertThat(area.intersects(other)).isTrue()
        assertThat(other.intersects(area)).isTrue()
    }
}