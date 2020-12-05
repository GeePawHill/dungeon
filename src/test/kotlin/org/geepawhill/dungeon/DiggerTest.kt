package org.geepawhill.dungeon

import org.assertj.core.api.Assertions.assertThat
import org.geepawhill.dungeon.Direction.EAST
import org.junit.jupiter.api.Test

class DiggerTest {

    val map = Map(20, 20)

    @Test
    fun `dig to border`() {
        // border will be at 19.
        val start = Coords(17, 10)
        val digger = Digger(map, start, EAST)
        assertThat(digger.cause).isEqualTo(Coords(19, 10))
        assertThat(digger.dug).containsExactly(Coords(18, 10))
    }

    @Test
    fun `dig to direct floor`() {
        val start = Coords(15, 10)
        map[17, 10] = CellType.FLOOR
        val digger = Digger(map, start, EAST)
        assertThat(digger.cause).isEqualTo(Coords(17, 10))
        assertThat(digger.dug).containsExactly(Coords(16, 10))
    }

    @Test
    fun `dig to indirect floor`() {
        val start = Coords(15, 10)
        map[17, 11] = CellType.FLOOR
        val digger = Digger(map, start, EAST)
        assertThat(digger.cause).isEqualTo(Coords(17, 11))
        assertThat(digger.dug).containsExactly(Coords(16, 10), Coords(17, 10))
    }

    @Test
    fun `dig to hallway`() {
        val start = Coords(15, 10)
        map[17, 10] = CellType.HALLWAY
        val digger = Digger(map, start, EAST)
        assertThat(digger.cause).isEqualTo(Coords(17, 10))
        assertThat(digger.dug).containsExactly(Coords(16, 10))
    }

    @Test
    fun `dig to indirect hallway`() {
        val start = Coords(15, 10)
        map[17, 11] = CellType.HALLWAY
        val digger = Digger(map, start, EAST)
        assertThat(digger.cause).isEqualTo(Coords(17, 11))
        assertThat(digger.dug).containsExactly(Coords(16, 10), Coords(17, 10))
    }
}