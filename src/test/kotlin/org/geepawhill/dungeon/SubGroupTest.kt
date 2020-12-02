package org.geepawhill.dungeon

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubGroupTest {
    val group = SubGroup()

    @Test
    fun `contains in room`() {
        group.rooms.add(Area(0, 0, 10, 10))
        assertThat(group.contains(Coords(5, 5))).isTrue()
    }

    @Test
    fun `contains in hallway`() {
        group.hallways.add(Area(0, 0, 10, 0))
        assertThat(group.contains(Coords(5, 0))).isTrue()
    }

    @Test
    fun `union single room`() {
        group.rooms.add(Area(0, 0, 10, 10))
        assertThat(group.union).isEqualTo(Area(0, 0, 10, 10))
    }

    @Test
    fun `union east west`() {
        group.rooms.add(Area(0, 0, 10, 10))
        group.rooms.add(Area(20, 0, 30, 10))
        assertThat(group.union).isEqualTo(Area(0, 0, 30, 10))
    }

    @Test
    fun `union north south`() {
        group.rooms.add(Area(0, 0, 10, 10))
        group.rooms.add(Area(0, 20, 10, 30))
        assertThat(group.union).isEqualTo(Area(0, 0, 10, 30))
    }

    @Test
    fun `union diagonals`() {
        group.rooms.add(Area(0, 0, 10, 10))
        group.rooms.add(Area(20, 20, 30, 30))
        assertThat(group.union).isEqualTo(Area(0, 0, 30, 30))
    }

}