package org.geepawhill.dungeon

class MapMaker(val map: Map) {

    val randoms = Randoms()
    val rooms = mutableListOf<Area>()
    val groups = mutableListOf<SubGroup>()

    private var rules = MapRules()

    fun generate(rules: MapRules = MapRules()) {
        this.rules = rules
        randoms.reseed(rules.seed)
        while (true) {
            rooms.clear()
            groups.clear()
            map.reset()
            makePlacements()
            makeRandomRooms(rules.density)
            if (!makeGroups()) continue
            if (connectGroups()) return
        }
    }

    fun place(area: Area) {
        rooms.add(area)
        for (c in area.west..area.east) {
            for (r in area.north..area.south) {
                map.cell[c, r] = CellType.FLOOR
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
    }

    fun attemptFill(): Int {
        val west = randoms.interval(1, map.width)
        val north = randoms.interval(1, map.height)
        val east = randoms.interval(west + MIN_WIDTH, west + MAX_WIDTH)
        val south = randoms.interval(north + MIN_HEIGHT, north + MAX_HEIGHT)
        if (east > map.width - 2) return 0
        if (south > map.height - 2) return 0
        val attempt = Area(west, north, east, south)
        for (room in rooms) {
            if (room.margin(1).intersects(attempt.margin(1))) return 0
        }
        place(attempt)
        return (attempt.east - attempt.west) * (attempt.south - attempt.north)
    }

    fun makeGroups(): Boolean {
        val disconnecteds = rooms.toMutableList()
        var attempts = 0
        while (disconnecteds.isNotEmpty() && attempts < 1000) {
            attempts += 1
            val from = randoms.choose(disconnecteds)
            val connectors = findLegalHallway(from)
            if (connectors.isEmpty()) continue
            val connector = connectors[0]
            if (map[connector.end.cause] == CellType.FLOOR) {
                val to = rooms.filter { it.contains(connector.end.cause) }[0]
                disconnecteds.remove(to)
                addRoomToRoom(from, to, connector.area)
            } else {
                addRoomToHallway(from, connector.end.cause, connector.area)
            }
            disconnecteds.remove(from)
            connector.commit(CellType.HALLWAY)
        }
        return disconnecteds.isEmpty()
    }

    fun makePlacements() {
        // placeholder for placement-first maps to use as callback
    }

    fun connectGroups(): Boolean {
        var attempts = 0
        while (groups.size > 1 && attempts < rules.groupAttempts) {
            attempts += 1
            val from = randoms.choose(groups)
            val direction = randoms.orthogonal()
            val seed = chooseGroupEdge(from, direction)[direction]
            val neigbhor1 = map[seed[direction.neighbors().first]]
            val neighbor2 = map[seed[direction.neighbors().second]]
            if (neigbhor1 != CellType.GRANITE) continue
            if (neighbor2 != CellType.GRANITE) continue
            val connector = Connector(map, seed, direction)
            if (!connector.isLegal) continue
            val to = groups.filter { it.containsInside(connector.end.cause) }[0]
            if (to == from) continue
            connector.commit(CellType.GROUP_HALLWAY)
            mergeGroups(from, to, connector.area)
        }
        return true
    }

    fun mergeGroups(from: SubGroup, to: SubGroup, hallway: Area) {
        groups.remove(from)
        to.rooms.addAll(from.rooms)
        to.hallways.addAll(from.hallways)
        to.hallways.add(hallway)
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

    fun findLegalHallway(from: Area): List<Connector> {
        for (i in 0..1000) {
            val connector = makeConnector(from, randoms.orthogonal())
            if (map[connector.end.cause] == CellType.BORDER) continue
            return listOf(connector)
        }
        return emptyList()
    }

    private fun makeConnector(area: Area, direction: Direction): Connector {
        val start = chooseEdge(area, direction)
        val connector = Connector(map, start[direction], direction)
        return connector
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

    fun chooseGroupEdge(group: SubGroup, direction: Direction): Coords {
        var groupEdge = chooseEdge(group.union, direction)[direction]
        while (map[groupEdge] != CellType.BORDER) groupEdge = groupEdge[direction]
        groupEdge = groupEdge[direction.opposite()]
        while (true) {
            if (group.containsInside(groupEdge)) return groupEdge
            groupEdge = groupEdge[direction.opposite()]
        }
    }

    companion object {
        const val MIN_WIDTH = 10
        const val MAX_WIDTH = 20
        const val MIN_HEIGHT = 10
        const val MAX_HEIGHT = 20
    }


}