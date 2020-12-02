package org.geepawhill.dungeon

class MapMaker(val map: Map) {

    val randoms = Randoms()
    val rooms = mutableListOf<Area>()
    val groups = mutableListOf<SubGroup>()

    fun generate() {
        while (true) {
            map.reset()
            makePlacements()
            makeRandomRooms(30)
            makeGroups()
            if (connectGroups() == true) return
        }
    }

    fun place(area: Area) {
        rooms.add(area)
        for (c in area.west..area.east) {
            for (r in area.north..area.south) {
                map.cell[c][r] = Cell.FLOOR
            }
        }
    }

    fun place(west: Int, north: Int, east: Int, south: Int) {
        place(Area(west, north, east, south))
    }


    fun makeRandomRooms(density: Int) {
        var target = (density * map.width * map.height) / 100
        var attempts = 1000
        while (target > 0 && attempts > 0) {
            target -= attemptFill()
            attempts -= 1
        }
        val achieved = (target * 100) / (map.width * map.height)
        println("Attempts: $attempts")
    }

    fun attemptFill(): Int {
        val west = randoms.interval(1, map.width)
        val north = randoms.interval(1, map.height)
        val east = randoms.interval(west + MIN_WIDTH, west + MAX_WIDTH)
        val south = randoms.interval(north + MIN_HEIGHT, north + MAX_HEIGHT)
        if (east > map.width - 2) return 0
        if (south > map.height - 2) return 0
        val attempt = Area(west, north, east, south)
        println(attempt)
        for (room in rooms) {
            if (room.margin(1).intersects(attempt.margin(1))) return 0
        }
        place(attempt)
        return (attempt.east - attempt.west) * (attempt.south - attempt.north)
    }

    fun makeGroups() {
        val disconnecteds = rooms.toMutableList()
        while (disconnecteds.isNotEmpty()) {
            val from = randoms.choose(disconnecteds)
            val digger = findLegalHallway(from)
            if (map[digger.cause] == Cell.FLOOR) {
                val to = rooms.filter { it.contains(digger.cause) }[0]
                disconnecteds.remove(to)
                addRoomToRoom(from, to, digger.hallway)
            } else {
                addRoomToHallway(from, digger.cause, digger.hallway)
            }
            disconnecteds.remove(from)
            digger.commit(Cell.HALLWAY)
        }

        for (group in groups) {
            println("Group")
            println("Rooms")
            group.rooms.forEach { println("\t$it") }
            println("Hallways")
            group.hallways.forEach { println("\t$it") }
        }
    }

    fun makePlacements() {
        // placeholder for placement-first maps to use as callback
    }

    fun connectGroups(): Boolean {
        var attempts = 0
        while (groups.size > 1 && attempts < 7) {
            attempts += 1
            val from = randoms.choose(groups)
            println("From: ${from.union}")
            val direction = randoms.orthogonal()
            println("Direction: $direction")
            val fromBorder = projectBackwards(from.union, direction)
            println("FromBorder: $fromBorder")
            val toDigger = Digger(map, fromBorder, direction)
            if (map[toDigger.cause] == Cell.BORDER) continue
            println("Got one!")
            toDigger.commit(Cell.GROUP_HALLWAY)
            val to = groups.filter { it.contains(toDigger.cause) }[0]
            println("To: ${to.union}")
            mergeGroups(from, to, toDigger.hallway)
        }
        return true
    }

    fun mergeGroups(from: SubGroup, to: SubGroup, hallway: Area) {
        groups.remove(from)
        to.rooms.addAll(from.rooms)
        to.hallways.addAll(from.hallways)
        to.hallways.add(hallway)
    }

    fun projectBackwards(area: Area, direction: Direction): Coords {
        val fromBorder = chooseEdge(area, direction)
        if (map[fromBorder] != Cell.GRANITE) return fromBorder
        val fromDigger = Digger(map, fromBorder, oppositeOf(direction))
        return fromDigger.dug.last()
    }

    private fun oppositeOf(direction: Direction): Direction =
            when (direction) {
                Direction.NORTH -> Direction.SOUTH
                Direction.EAST -> Direction.WEST
                Direction.SOUTH -> Direction.NORTH
                Direction.WEST -> Direction.EAST
                else -> throw RuntimeException("Non-orthogonal supplied to oppositeOf.")
            }

    private fun addRoomToHallway(from: Area, toCoords: Coords, hallway: Area) {
        for (group in groups) {
            for (h in group.hallways) {
                if (h.contains(toCoords)) {
                    group.hallways.add(hallway)
                    group.rooms.add(from)
                    return
                }
            }
        }
        throw RuntimeException("Connected to unknown hallway!")
    }

    private fun addRoomToRoom(from: Area, to: Area, hallway: Area) {
        for (group in groups) {
            if (group.rooms.contains(to)) {
                group.rooms.add(from)
                group.hallways.add(hallway)
                return
            }
        }
        val group = SubGroup()
        group.rooms.add(to)
        group.rooms.add(from)
        group.hallways.add(hallway)
        groups.add(group)
    }

    fun findLegalHallway(from: Area): Digger {
        for (i in 0..1000) {
            val digger = makeDigger(from, randoms.orthogonal())
            if (map[digger.cause] == Cell.BORDER) continue
            return digger
        }
        throw java.lang.RuntimeException("No legal hallway")
    }

    private fun makeDigger(area: Area, direction: Direction): Digger {
        val start = chooseEdge(area, direction)
        val digger = Digger(map, start, direction)
        return digger
    }

    fun chooseEdge(area: Area, direction: Direction): Coords {
        when (direction) {
            Direction.NORTH -> return Coords(randoms.interval(area.west, area.east), area.north)
            Direction.SOUTH -> return Coords(randoms.interval(area.west, area.east), area.south)
            Direction.WEST -> return Coords(area.west, randoms.interval(area.north, area.south))
            Direction.EAST -> return Coords(area.east, randoms.interval(area.north, area.south))
            else -> throw RuntimeException("Illegal direction for choose edge.")
        }
    }

    companion object {
        const val MIN_WIDTH = 10
        const val MAX_WIDTH = 20
        const val MIN_HEIGHT = 10
        const val MAX_HEIGHT = 20
    }


}