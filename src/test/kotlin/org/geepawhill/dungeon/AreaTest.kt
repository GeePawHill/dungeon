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

    @Test
    fun `contains a contained point`() {
        val area = Area(0, 0, 10, 10)
        assertThat(area.contains(Coords(5, 5))).isTrue()
    }

    @Test
    fun `line contains point`() {
        val area = Area(0, 0, 10, 0)
        assertThat(area.contains(Coords(5, 0))).isTrue()
    }

    @Test
    fun `contains a border point`() {
        val area = Area(0, 0, 10, 10)
        assertThat(area.contains(Coords(10, 5))).isTrue()
    }

    @Test
    fun `doesn't contain an outside point`() {
        val area = Area(0, 0, 10, 10)
        assertThat(area.contains(Coords(11, 5))).isFalse()
    }

    @Test
    fun `fill`() {
        val map = Map(10, 10)
        val area = Area(0, 0, 5, 5)
        area.fill(map, Cell.GROUP_HALLWAY)
        for (c in 0..5) {
            for (r in 0..5) {
                assertThat(map[c, r]).withFailMessage("Testing ($c,$r)").isEqualTo(Cell.GROUP_HALLWAY)
            }
        }
    }

    @Test
    fun `normalize`() {
        val area = Area.normalized(Coords(10, 9), Coords(0, 1))
        assertThat(area).isEqualTo(Area(0, 1, 10, 9))
    }
}