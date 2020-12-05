package org.geepawhill.dungeon

import java.lang.Integer.max
import java.lang.Integer.min

class Digger(val map: Map, val start: Coords, val direction: Direction) {

    val cause: Coords
    val dug = mutableListOf<Coords>()
    val neighbor1 = facesFor(direction).first
    val neighbor2 = facesFor(direction).second
    val hallway: Area
        get() {
            val from = dug.first()
            val to = dug.last()
            val west = min(from.x, to.x)
            val east = max(from.x, to.x)
            val north = min(from.y, to.y)
            val south = max(from.y, to.y)
            return Area(west, north, east, south)
        }

    init {
        var current = start[direction]
        while (true) {
            dug += current
            if (map[current[neighbor1]] != CellType.GRANITE) {
                cause = current[neighbor1]
                break
            }
            if (map[current[neighbor2]] != CellType.GRANITE) {
                cause = current[neighbor2]
                break
            }
            if (map[current[direction]] != CellType.GRANITE) {
                cause = current[direction]
                break
            }
            current = current[direction]
        }
    }

    fun facesFor(direction: Direction): Pair<Direction, Direction> {
        return when (direction) {
            Direction.NORTH,
            Direction.SOUTH -> Pair(Direction.EAST, Direction.WEST)
            Direction.EAST,
            Direction.WEST -> Pair(Direction.SOUTH, Direction.NORTH)
            else -> throw RuntimeException("facesFor called for non-orthogonal direction.")
        }
    }

    fun commit(cell: CellType) {
        for (coords in dug) map[coords] = cell
    }

    companion object {
        const val MAX_LENGTH = 20
    }
}