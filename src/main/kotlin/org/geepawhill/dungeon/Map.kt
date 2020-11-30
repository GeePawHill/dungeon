package org.geepawhill.dungeon

class Map(val width: Int, val height: Int) {
    val widthInPixels = width * TILESIZE
    val heightInPixels = height * TILESIZE

    val cell: Array<Array<Cell>> = Array(width)
    { _ ->
        Array(height) { _ -> Cell.GRANITE }
    }

    companion object {
        const val TILESIZE = 64.0
    }
}

