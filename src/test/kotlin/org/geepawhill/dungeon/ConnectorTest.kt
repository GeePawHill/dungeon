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
    }
}