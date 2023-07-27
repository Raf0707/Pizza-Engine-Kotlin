package pize.tests.voxelgame.client.block.vanilla

import pize.graphics.util.color.ImmutableColor
import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.model.BlockModel
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshType
import pize.tests.voxelgame.client.resources.GameResources
import pize.tests.voxelgame.main.Direction

class Water(id: Int) : BlockProperties(id) {
    override fun load(resources: GameResources?) {
        solid = true
        lightLevel = 0
        opacity = 0
        translucent = false
        soundPack = null
        newState(
            Direction.NONE,
            BlockModel(ChunkMeshType.TRANSLUCENT)
                .allFaces(resources!!.getBlockRegion("water"), COLOR),  //BlockCollide.SOLID,
            null,  //BlockCursor.SOLID
            null
        )
    }

    companion object {
        val COLOR = ImmutableColor(0.0, 0.2, 0.9, 1.0)
    }
}
