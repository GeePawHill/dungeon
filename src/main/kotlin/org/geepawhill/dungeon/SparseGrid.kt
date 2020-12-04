package org.geepawhill.dungeon

class SparseGrid<T>(val width: Int, val height: Int, val default: () -> T) {

    class IllegalCoordsException(coords: Coords) : RuntimeException("Illegal coordinates in SparseGrid $coords.")

    private val coordsToContents = mutableMapOf<Coords, T>()

    operator fun get(x: Int, y: Int): T = get(Coords(x, y))
    operator fun get(coords: Coords) = coordsToContents.getOrDefault(validate(coords), default())

    operator fun set(x: Int, y: Int, value: T) = set(Coords(x, y), value)
    operator fun set(coords: Coords, value: T) = coordsToContents.put(validate(coords), value)

    fun clear() = coordsToContents.clear()

    private fun validate(coords: Coords): Coords {
        if (coords.x < 0 || coords.y < 0 || coords.x >= width || coords.y >= height) throw IllegalCoordsException(coords)
        return coords
    }
}