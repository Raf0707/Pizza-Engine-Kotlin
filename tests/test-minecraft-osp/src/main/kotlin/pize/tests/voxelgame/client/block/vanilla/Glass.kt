package pize.tests.voxelgame.client.block.vanilla

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.shape.BlockCollide
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.audio.BlockSoundPack

class Glass(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = 0
        opacity = 4
        translucent = false
        soundPack = BlockSoundPack.GLASS
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .allFaces(resources!!.getBlockRegion("glass")),
            BlockCollide.Companion.SOLID,
            BlockCursor.Companion.SOLID
        )
    }
}