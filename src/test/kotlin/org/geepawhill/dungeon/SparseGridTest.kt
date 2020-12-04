package org.geepawhill.dungeon

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SparseGridTest {

    val grid = SparseGrid<Int>(100, 100) { -1 }

    @Test
    fun `validation`() {
        assertThrows<SparseGrid.IllegalCoordsException> {
            grid[-1, 0] = 5
        }
        assertThrows<SparseGrid.IllegalCoordsException> {
            grid[100, 0] = 5
        }
        assertThrows<SparseGrid.IllegalCoordsException> {
            grid[0, -1] = 5
        }
        assertThrows<SparseGrid.IllegalCoordsException> {
            grid[0, 100] = 5
        }
    }

    @Test
    fun `default when unassigned`() {
        assertThat(grid[0, 0]).isEqualTo(-1)
    }

    @Test
    fun `remembers assigned values`() {
        grid[0, 0] = 1
        assertThat(grid[0, 0]).isEqualTo(1)
    }
}