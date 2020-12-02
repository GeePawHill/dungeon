package org.geepawhill.dungeon


class Connector(private val map: Map, seed: Coords, private val direction: Direction) {

    private val neighbor1 = direction.neighbors().first
    private val neighbor2 = direction.neighbors().second
    private val opposite = direction.opposite()

    val end: Terminator = moveToTerminator(seed, direction)
    val start: Terminator = moveToTerminator(end.coords, opposite)

    private fun moveToTerminator(seed: Coords, direction: Direction): Terminator {
        var last = seed
        while (true) {
            val forwards = last[direction]
            if (map[forwards] != Cell.GRANITE) return Terminator(last, forwards)
            if (map[forwards[neighbor1]] != Cell.GRANITE) return Terminator(last, forwards[neighbor1])
            if (map[forwards[neighbor2]] != Cell.GRANITE) return Terminator(last, forwards[neighbor2])
            last = forwards
        }
    }
}

