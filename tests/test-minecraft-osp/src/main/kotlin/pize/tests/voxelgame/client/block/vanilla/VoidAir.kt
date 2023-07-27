package pize.tests.voxelgame.client.block.vanilla

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.resources.GameResources

class VoidAir(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = false
        lightLevel = 0
        opacity = 0
        translucent = false
        soundPack = null
    }
}
