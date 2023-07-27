package pize.tests.voxelgame.client.block

import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.block.vanilla.*

object Blocks {
    private val fromID: MutableMap<Byte, BlockProperties> = HashMap()
    fun fromID(blockID: Byte): BlockProperties? {
        return fromID[blockID]
    }

    fun register(properties: BlockProperties): BlockProperties {
        fromID[properties.id] = properties
        return properties
    }

    fun init(session: Minecraft) {
        val resources = session.renderer.session.resources
        for (blockProperties in fromID.values) blockProperties.load(resources)
    }

    val VOID_AIR = register(VoidAir(-1))
    val AIR = register(Air(0))
    val DIRT = register(Dirt(1))
    val GRASS_BLOCK = register(GrassBlock(2))
    val STONE = register(Stone(3))
    val GLASS = register(Glass(4))
    val OAK_LOG = register(OakLog(5))
    val LAMP = register(Lamp(6))
    val GRASS = register(Grass(7))
    val OAK_LEAVES = register(OakLeaves(8))
    val WATER = register(Water(9))
    val SAND = register(Sand(10))
}
