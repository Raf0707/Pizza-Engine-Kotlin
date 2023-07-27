package pize.tests.voxelgame.main.level

import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.chunk.storage.ChunkPos

abstract class ChunkManager {
    abstract fun getChunk(chunkPos: ChunkPos?): LevelChunk?
    abstract fun getChunk(chunkX: Int, chunkZ: Int): LevelChunk?
}
