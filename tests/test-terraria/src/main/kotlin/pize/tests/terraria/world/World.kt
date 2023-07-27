package pize.tests.terraria.world

import pize.tests.terraria.entity.Entity
import pize.tests.terraria.map.WorldMap
import pize.tests.terraria.tile.TileType

class World(width: Int, height: Int) {
    val wallMap: WorldMap
    val tileMap: WorldMap
    val entities: MutableList<Entity?>

    init {
        wallMap = WorldMap(width, height)
        tileMap = WorldMap(width, height)
        for (i in tileMap.tiles.indices) tileMap.tiles[i].type = TileType.DIRT
        entities = ArrayList()
    }
}
