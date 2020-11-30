package org.geepawhill.dungeon

import java.util.*

class Randoms {
    val source = Random(100)

    fun interval(from: Int, to: Int) = from + source.nextInt(to - from + 1)
}