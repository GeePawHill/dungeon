package org.geepawhill.dungeon

class Map(val width: Int, val height: Int) {
    val widthInPixels = width * TILESIZE
    val heightInPixels = height * TILESIZE

    val cell = SparseGrid<CellType>(width, height) { CellType.GRANITE }

    init {
        reset()
    }

    operator fun get(x: Int, y: Int) = cell[x, y]
    operator fun set(x: Int, y: Int, value: CellType) {
        cell[x, y] = value
    }

    operator fun get(coords: Coords) = cell[coords]
    operator fun set(coords: Coords, value: CellType) {
        cell[coords] = value
    }

    fun reset() {
        for (col in 0 until width) {
            cell[col, 0] = CellType.BORDER
            cell[col, height - 1] = CellType.BORDER
        }
        for (row in 0 until height) {
            cell[0, row] = CellType.BORDER
            cell[width - 1, row] = CellType.BORDER
        }
    }

    companion object {
        const val TILESIZE = 64.0
    }
}

