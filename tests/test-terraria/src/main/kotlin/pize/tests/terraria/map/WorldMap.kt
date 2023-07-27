package pize.tests.terraria.map

import pize.tests.terraria.tile.TileType

class WorldMap(val width: Int, val height: Int) {
    val tiles: Array<MapTile?>

    init {
        tiles = arrayOfNulls(width * height)
        for (i in tiles.indices) tiles[i] = MapTile(TileType.AIR, 15)
    }

    fun getTile(x: Int, y: Int): MapTile? {
        return if (isOutOfBounds(x, y)) null else tiles[getIndex(x, y)]
    }

    fun getIndex(x: Int, y: Int): Int {
        return y * width + x
    }

    fun isOutOfBounds(x: Int, y: Int): Boolean {
        return x >= width || y >= height || x < 0 || y < 0
    }
}