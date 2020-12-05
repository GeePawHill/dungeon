package org.geepawhill.dungeon

import java.util.*

class Randoms {
    val source = Random(999)

    fun interval(from: Int, to: Int) = from + source.nextInt(to - from + 1)

    fun orthogonal(): Direction =
        when (source.nextInt(4)) {
            0 -> Direction.NORTH
            1 -> Direction.SOUTH
            2 -> Direction.EAST
            3 -> Direction.WEST
            else -> throw RuntimeException("Bounded random returned illegal value")
        }

    fun <T> choose(from: List<T>): T = from[source.nextInt(from.size)]
}