package pize.tests.terraria.map

import pize.tests.terraria.tile.TileType

class MapTile(type: TileType?, light: Int) {
    var type: TileType? = null
    var light: Byte = 0
        private set

    init {
        this.type = type
        setLight(light)
    }

    fun setLight(light: Int) {
        this.light = light.toByte()
    }
}
