package org.geepawhill.dungeon

import javafx.collections.ObservableList
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class Map(val width: Int, val height: Int) {
    val widthInPixels = width * TILESIZE
    val heightInPixels = height * TILESIZE

    val cell: Array<Array<Cell>> = Array(width)
    { _ ->
        Array(height) { _ -> Cell.GRANITE }
    }

    init {
        for (col in 0 until width) {
            cell[col][0] = Cell.BORDER
            cell[col][height - 1] = Cell.BORDER
        }
        for (row in 0 until height) {
            cell[0][row] = Cell.BORDER
            cell[width - 1][row] = Cell.BORDER
        }
    }

    fun populateRegions(destination: ObservableList<Node>) {
        for (col in 0 until width) {
            for (row in 0 until height) {
                when (cell[col][row]) {
                    Cell.BORDER -> destination.add(makeBorder(col, row))
                    Cell.FLOOR -> destination.add(makeFloor(col, row))
                    else -> {
                    }
                }
            }
        }
    }

    fun place(column: Int, row: Int, width: Int, height: Int) {
        for (c in 0 until width) {
            for (r in 0 until height) {
                cell[column + c][row + r] = Cell.FLOOR
            }
        }
    }

    fun makeBorder(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.BLUE
            stroke = Color.YELLOW
        }
    }

    fun makeFloor(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.YELLOW
            stroke = Color.BLACK
        }
    }

    fun toRectangle(x: Int, y: Int) = Rectangle2D(x * TILESIZE, y * TILESIZE, TILESIZE, TILESIZE)
    fun randomFills(density: Double) {
        val area = width * height

    }

    companion object {
        const val TILESIZE = 64.0
    }

}