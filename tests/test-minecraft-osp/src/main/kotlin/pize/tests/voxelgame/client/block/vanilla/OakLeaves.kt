package pize.tests.voxelgame.client.block.vanilla

import pize.graphics.util.color.ImmutableColor
import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.shape.BlockCollide
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.audio.BlockSoundPack
import pize.tests.voxelgame.main.chunk.ChunkUtils

class OakLeaves(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = 0
        opacity = ChunkUtils.MAX_LIGHT_LEVEL
        translucent = false
        soundPack = BlockSoundPack.GRASS
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .allFaces(resources!!.getBlockRegion("oak_leaves"), COLOR)
                .setFacesTransparentForNeighbors(true),
            BlockCollide.Companion.SOLID,
            BlockCursor.Companion.SOLID
        )
    }

    companion object {
        val COLOR = ImmutableColor(0.25, 0.6, 0.05, 1.0)
    }
}