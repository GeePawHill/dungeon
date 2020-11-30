package org.geepawhill.dungeon

data class Area(val west: Int, val north: Int, val east: Int, val south: Int) {

    fun margin(amount: Int): Area {
        return Area(west - amount, north - amount, east + amount, south + amount)
    }

    fun intersects(other: Area): Boolean {
        return !(south < other.north || north > other.south || west > other.east || east < other.west)
        return false
    }

    override fun toString(): String = "($west,$north) - ($east,$south)"
}