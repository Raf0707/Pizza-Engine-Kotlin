package pize.tests.terraria.tile

enum class TileType(id: Int, val collidable: Boolean) {
    AIR(0, false),
    DIRT(1, true),
    STONE(2, true),
    GRASS(3, true);

    val id: Short

    init {
        this.id = id.toShort()
    }
}
