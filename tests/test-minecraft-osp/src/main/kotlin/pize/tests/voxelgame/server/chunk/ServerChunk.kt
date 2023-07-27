package pize.tests.voxelgame.server.chunk

import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.Level
import pize.tests.voxelgame.server.level.ServerLevel

class ServerChunk(level: ServerLevel, position: ChunkPos?) : LevelChunk(level, position) {
    override val level: Level
        get() = level as ServerLevel

    override fun setBlock(lx: Int, y: Int, lz: Int, state: Short): Boolean {
        val blockProperties = BlockData.getProps(state)
        val result = super.setBlock(lx, y, lz, state)
        if (result) {
            getHeightMap(HeightmapType.HIGHEST)!!.update(lx, y, lz, !blockProperties.isEmpty)
            return true
        }
        return false
    }

    fun setBlockFast(lx: Int, y: Int, lz: Int, state: Short): Boolean {
        return super.setBlock(lx, y, lz, state)
    }

    override fun setLight(lx: Int, y: Int, lz: Int, level: Int) {
        super.setLight(lx, y, lz, level)
    }
}
