package pize.tests.voxelgame.client.block.vanilla

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.shape.BlockCollide
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.audio.BlockSoundPack
import pize.tests.voxelgame.main.chunk.ChunkUtils

class Lamp(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = ChunkUtils.MAX_LIGHT_LEVEL
        opacity = ChunkUtils.MAX_LIGHT_LEVEL
        translucent = false
        soundPack = BlockSoundPack.GLASS
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .allFaces(resources!!.getBlockRegion("redstone_lamp_on")),
            BlockCollide.Companion.SOLID,
            BlockCursor.Companion.SOLID
        )
    }
}
