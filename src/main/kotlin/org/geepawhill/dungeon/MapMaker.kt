package org.geepawhill.dungeon

import javafx.collections.ObservableList
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MapMaker(val map: Map) {

    val randoms = Randoms()
    val rooms = mutableListOf<Area>()

    init {
        for (col in 0 until map.width) {
            map.cell[col][0] = Cell.BORDER
            map.cell[col][map.height - 1] = Cell.BORDER
        }
        for (row in 0 until map.height) {
            map.cell[0][row] = Cell.BORDER
            map.cell[map.width - 1][row] = Cell.BORDER
        }
    }

    fun populateRegions(destination: ObservableList<Node>) {
        for (c in 0 until map.width) {
            for (r in 0 until map.height) {
                when (map.cell[c][r]) {
                    Cell.BORDER -> destination.add(makeBorder(c, r))
                    Cell.FLOOR -> destination.add(makeFloor(c, r))
                    else -> {
                    }
                }
            }
        }
    }

    fun place(area: Area) {
        rooms.add(area)
        for (c in area.west..area.east) {
            for (r in area.north..area.south) {
                map.cell[c][r] = Cell.FLOOR
            }
        }
    }

    fun place(west: Int, north: Int, east: Int, south: Int) {
        place(Area(west, north, east, south))
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

    fun toRectangle(x: Int, y: Int) = Rectangle2D(x * Map.TILESIZE, y * Map.TILESIZE, Map.TILESIZE, Map.TILESIZE)

    fun randomFills(density: Int) {
        var target = (density * map.width * map.height) / 100
        var attempts = 1000
        while (target > 0 && attempts > 0) {
            target -= attemptFill()
            attempts -= 1
        }
        val achieved = (target * 100) / (map.width * map.height)
        println("$achieved $attempts")
    }

    fun attemptFill(): Int {
        val west = randoms.interval(1, map.width)
        val north = randoms.interval(1, map.height)
        val east = randoms.interval(west + MIN_WIDTH, west + MAX_WIDTH)
        val south = randoms.interval(north + MIN_HEIGHT, north + MAX_HEIGHT)
        if (east > map.width - 2) return 0
        if (south > map.height - 2) return 0
        val attempt = Area(west, north, east, south)
        println(attempt)
        for (room in rooms) {
            if (room.margin(1).intersects(attempt.margin(1))) return 0
        }
        println("Placing")
        place(attempt)
        return (attempt.east - attempt.west) * (attempt.south - attempt.north)
    }

    companion object {
        const val MIN_WIDTH = 16
        const val MAX_WIDTH = 32
        const val MIN_HEIGHT = 16
        const val MAX_HEIGHT = 32
    }


}