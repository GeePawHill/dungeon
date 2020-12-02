package org.geepawhill.dungeon

import java.lang.Integer.max
import java.lang.Integer.min

data class Area(val west: Int, val north: Int, val east: Int, val south: Int) {

    fun margin(amount: Int): Area {
        return Area(west - amount, north - amount, east + amount, south + amount)
    }

    fun intersects(other: Area): Boolean {
        return !(south < other.north || north > other.south || west > other.east || east < other.west)
    }

    fun contains(coords: Coords): Boolean {
        return intersects(Area(coords.x, coords.y, coords.x, coords.y))
    }

    fun union(other: Area): Area = Area(min(west, other.west), min(north, other.north), max(east, other.east), max(south, other.south))

    override fun toString(): String = "($west,$north) - ($east,$south)"
}