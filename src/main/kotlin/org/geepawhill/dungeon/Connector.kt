package org.geepawhill.dungeon


class Connector(private val map: Map, seed: Coords, private val direction: Direction) {

    private val neighbor1 = direction.neighbors().first
    private val neighbor2 = direction.neighbors().second
    private val opposite = direction.opposite()

    private val cells = mutableListOf<Coords>()

    val end: Terminator = moveToTerminator(seed, direction, false)
    val start: Terminator = moveToTerminator(end.coords[opposite], opposite, true)
    val isLegal = map[end.cause] != CellType.BORDER && map[start.cause] != CellType.BORDER
    val area = Area.normalized(start.coords, end.coords)

    fun commit(type: CellType) {
        area.fill(map, type)
    }

    private fun moveToTerminator(seed: Coords, direction: Direction, track: Boolean): Terminator {
        var last = seed
        while (true) {
            if (map[last[neighbor1]] != CellType.GRANITE) return Terminator(last, last[neighbor1])
            if (map[last[neighbor2]] != CellType.GRANITE) return Terminator(last, last[neighbor2])
            if (map[last[direction]] != CellType.GRANITE) return Terminator(last, last[direction])
            if (track) cells.add(last)
            last = last[direction]
        }
    }
}

