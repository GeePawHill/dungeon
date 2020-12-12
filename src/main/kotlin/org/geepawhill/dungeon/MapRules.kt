package org.geepawhill.dungeon

class MapRules(
    val seed: Long = 100L,
    val density: Int = 25,
    val groupAttempts: Int = 20,
    val preferNearest: Boolean = true,
    val copies: Int = 1
)