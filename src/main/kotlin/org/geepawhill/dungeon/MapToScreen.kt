package org.geepawhill.dungeon

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MapToScreen(val map: Map) {

    val cell = SimpleObjectProperty<Coords>()

    fun handleClick(x: Int, y: Int) {
        cell.value = Coords(x, y)
    }

    fun mapToScreen(destination: ObservableList<Node>) {
        for (c in 0 until map.width) {
            for (r in 0 until map.height) {
                when (map.cell[c, r]) {
                    CellType.BORDER -> destination.add(borderToScreen(c, r))
                    CellType.FLOOR -> destination.add(floorToScreen(c, r))
                    CellType.HALLWAY -> destination.add(hallwayToScreen(c, r))
                    CellType.GROUP_HALLWAY -> destination.add(groupHallwayToScreen(c, r))
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
            onMouseClicked = EventHandler { _ -> handleClick(col, row) }
        }
    }

    fun floorToScreen(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.YELLOW
            stroke = Color.BLACK
            onMouseClicked = EventHandler { _ -> handleClick(col, row) }
        }
    }

    fun hallwayToScreen(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.RED
            stroke = Color.BLACK
            onMouseClicked = EventHandler { _ -> handleClick(col, row) }
        }
    }

    fun groupHallwayToScreen(col: Int, row: Int): Rectangle {
        val coords = toRectangle(col, row)
        return Rectangle(coords.minX, coords.minY, coords.width, coords.height).apply {
            fill = Color.GRAY
            stroke = Color.BLACK
            onMouseClicked = EventHandler { _ -> handleClick(col, row) }
        }
    }

    fun toRectangle(x: Int, y: Int) = Rectangle2D(x * Map.TILESIZE, y * Map.TILESIZE, Map.TILESIZE, Map.TILESIZE)

}