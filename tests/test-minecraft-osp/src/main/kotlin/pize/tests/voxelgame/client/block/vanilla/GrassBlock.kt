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

class GrassBlock(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = 0
        opacity = ChunkUtils.MAX_LIGHT_LEVEL
        translucent = false
        soundPack = BlockSoundPack.GRASS
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .sideXZFaces(resources!!.getBlockRegion("grass_block_side"))
                .sideXZFaces(resources.getBlockRegion("grass_block_side_overlay"), COLOR)
                .pyFace(resources.getBlockRegion("grass_block_top"), COLOR)
                .nyFace(resources.getBlockRegion("dirt")),
            BlockCollide.Companion.SOLID,
            BlockCursor.Companion.SOLID
        )
    }

    companion object {
        val COLOR = ImmutableColor(0.3, 0.55, 0.15, 1.0)
    }
}
