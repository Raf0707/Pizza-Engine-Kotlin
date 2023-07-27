package pize.tests.voxelgame.client.block.vanilla

import pize.graphics.util.color.ImmutableColor
import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.block.model.Face
import pize.tests.voxelgame.client.block.model.Quad
import pize.tests.voxelgame.client.block.shape.BlockCursor
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.audio.BlockSoundPack

class Grass(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = false
        lightLevel = 0
        opacity = 0
        translucent = false
        soundPack = BlockSoundPack.GRASS
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.SOLID)
                .face(
                    Face(
                        Quad(0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f),
                        resources!!.getBlockRegion("grass"),
                        COLOR
                    )
                )
                .face(
                    Face(
                        Quad(1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f),
                        resources.getBlockRegion("grass"),
                        COLOR
                    )
                ),
            null,
            BlockCursor.Companion.SOLID
        )
    }

    companion object {
        val COLOR = ImmutableColor(0.3, 0.55, 0.15, 1.0)
    }
}