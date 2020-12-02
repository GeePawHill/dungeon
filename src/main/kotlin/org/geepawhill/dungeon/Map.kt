package org.geepawhill.dungeon

class Map(val width: Int, val height: Int) {
    val widthInPixels = width * TILESIZE
    val heightInPixels = height * TILESIZE

    val cell: Array<Array<Cell>> = Array(width)
    { _ ->
        Array(height) { _ -> Cell.GRANITE }
    }

    init {
        reset()
    }

    operator fun get(x: Int, y: Int) = cell[x][y]
    operator fun set(x: Int, y: Int, value: Cell) {
        cell[x][y] = value
    }

    operator fun get(coords: Coords) = cell[coords.x][coords.y]
    operator fun set(coords: Coords, value: Cell) {
        cell[coords.x][coords.y] = value
    }

    fun reset() {
        for (col in 0 until width) {
            cell[col][0] = Cell.BORDER
            cell[col][height - 1] = Cell.BORDER
        }
        for (row in 0 until height) {
            cell[0][row] = Cell.BORDER
            cell[width - 1][row] = Cell.BORDER
        }
    }

    companion object {
        const val TILESIZE = 64.0
    }
}

