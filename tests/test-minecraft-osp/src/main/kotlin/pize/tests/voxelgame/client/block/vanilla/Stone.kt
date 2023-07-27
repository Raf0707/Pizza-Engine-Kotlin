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

class Stone(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = 0
        opacity = ChunkUtils.MAX_LIGHT_LEVEL
        translucent = false
        soundPack = BlockSoundPack.STONE
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .allFaces(resources!!.getBlockRegion("stone")),
            BlockCollide.Companion.SOLID,
            BlockCursor.Companion.SOLID
        )
    }
}
