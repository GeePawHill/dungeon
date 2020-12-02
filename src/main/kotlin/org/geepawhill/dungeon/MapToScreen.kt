package org.geepawhill.dungeon

import javafx.collections.ObservableList
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MapToScreen(val map: Map) {

    fun mapToScreen(destination: ObservableList<Node>) {
        for (c in 0 until map.width) {
            for (r in 0 until map.height) {
                when (map.cell[c][r]) {
                    Cell.BORDER -> destination.add(borderToScreen(c, r))
                    Cell.FLOOR -> destination.add(borderToFloor(c, r))
                    Cell.HALLWAY -> destination.add(borderToHallway(c, r))
                    Cell.GROUP_HALLWAY -> destination.add(borderToGroupHallway(c, r))
                    else -> {
                    }
                }
            }
        }
    }

    fun borderToScreen(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.BLUE
            stroke = Color.YELLOW
        }
    }

    fun borderToFloor(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.YELLOW
            stroke = Color.BLACK
        }
    }

    fun borderToHallway(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.RED
            stroke = Color.BLACK
        }
    }

    fun borderToGroupHallway(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.GRAY
            stroke = Color.BLACK
        }
    }

    fun toRectangle(x: Int, y: Int) = Rectangle2D(x * Map.TILESIZE, y * Map.TILESIZE, Map.TILESIZE, Map.TILESIZE)

}