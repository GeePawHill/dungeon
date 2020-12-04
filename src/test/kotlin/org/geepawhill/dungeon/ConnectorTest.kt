package org.geepawhill.dungeon

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConnectorTest {

    val map = Map(20, 5)

    @Test
    fun `empty field, forward to border, back to border`() {
        val c = Connector(map, Coords(10, 2), Direction.EAST)
        assertThat(c.end.coords).isEqualTo(Coords(18, 2))
        assertThat(c.end.cause).isEqualTo(Coords(19, 2))
        assertThat(c.start.coords).isEqualTo(Coords(1, 2))
        assertThat(c.start.cause).isEqualTo(Coords(0, 2))
        assertThat(c.isLegal).isFalse()
    }

    @Test
    fun `commit`() {
        val c = Connector(map, Coords(10, 2), Direction.EAST)
        c.commit(Cell.GROUP_HALLWAY)
        for (x in 1..18) {
            assertThat(map[x, 2]).withFailMessage("Checking $x").isEqualTo(Cell.GROUP_HALLWAY)
        }
    }

    @Test
    fun `direct blocker`() {
        map[15, 2] = Cell.FLOOR
        val c = Connector(map, Coords(10, 2), Direction.EAST)
        assertThat(c.end.coords).isEqualTo(Coords(14, 2))
        assertThat(c.end.cause).isEqualTo(Coords(15, 2))
        assertThat(c.start.coords).isEqualTo(Coords(1, 2))
        assertThat(c.start.cause).isEqualTo(Coords(0, 2))
    }

    @Test
    fun `side blocker`() {
        map[15, 3] = Cell.FLOOR
        val c = Connector(map, Coords(10, 2), Direction.EAST)
        assertThat(c.end.coords).isEqualTo(Coords(15, 2))
        assertThat(c.end.cause).isEqualTo(Coords(15, 3))
        assertThat(c.start.coords).isEqualTo(Coords(1, 2))
        assertThat(c.start.cause).isEqualTo(Coords(0, 2))
    }

    @Test
    fun `side blocker backwards`() {
        map[8, 3] = Cell.FLOOR
        val c = Connector(map, Coords(10, 2), Direction.EAST)
        assertThat(c.end.coords).isEqualTo(Coords(18, 2))
        assertThat(c.end.cause).isEqualTo(Coords(19, 2))
        assertThat(c.start.coords).isEqualTo(Coords(8, 2))
        assertThat(c.start.cause).isEqualTo(Coords(8, 3))
    }
}