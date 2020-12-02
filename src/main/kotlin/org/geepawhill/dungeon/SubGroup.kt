package org.geepawhill.dungeon

class SubGroup {
    val rooms = mutableListOf<Area>()
    val hallways = mutableListOf<Area>()

    val union: Area
        get() {
            val combined = rooms + hallways
            if (combined.isEmpty()) throw RuntimeException("Called union on empty group")
            var area = combined[0]
            for (r in 1 until combined.size) {
                area = area.union(combined[r])
            }
            return area
        }

    fun contains(coords: Coords) = union.contains(coords)
}