package pize.tests.voxelgame.main.chunk

import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.server.chunk.ServerChunk

object ChunkUtils {
    const val SIZE_SHIFT = 4 // Степень двойки
    const val SIZE = 1 shl SIZE_SHIFT // SIZE = 2 ^ SIZE_SHIFT
    const val SIZE_IDX = SIZE - 1
    const val HEIGHT = 256
    const val HEIGHT_IDX = HEIGHT - 1
    const val AREA = SIZE * SIZE
    const val VOLUME = AREA * SIZE

    // Light
    const val MAX_LIGHT_LEVEL = 15
    fun getIndex(x: Int, y: Int, z: Int): Int {
        return x + z * SIZE + y * AREA
    }

    fun getIndex(x: Int, z: Int): Int {
        return x + z * SIZE
    }

    fun getSectionIndex(y: Int): Int {
        return getChunkPos(y)
    }

    fun getSectionY(index: Int): Int {
        return index shl SIZE_SHIFT
    }

    fun getLocalCoord(xyz: Int): Int {
        return xyz and SIZE_IDX
    }

    fun getChunkPos(XorZ: Int): Int {
        return XorZ shr SIZE_SHIFT
    }

    fun isOutOfBounds(x: Int, y: Int, z: Int): Boolean {
        return x > SIZE_IDX || y > HEIGHT_IDX || z > SIZE_IDX || x < 0 || y < 0 || z < 0
    }

    fun isOutOfBounds(x: Int, z: Int): Boolean {
        return x > SIZE_IDX || z > SIZE_IDX || x < 0 || z < 0
    }

    fun isOutOfBounds(y: Int): Boolean {
        return y > HEIGHT_IDX || y < 0
    }

    fun getNeighborChunk(chunk: LevelChunk, x: Int, z: Int): LevelChunk? {
        return chunk.getLevel().getChunkManager().getChunk(chunk.getPosition().getNeighbor(x, z))
    }

    fun getNeighborChunk(chunk: ClientChunk?, x: Int, z: Int): ClientChunk? {
        return chunk.getLevel().chunkManager.getChunk(chunk.getPosition().getNeighbor(x, z))
    }

    fun getNeighborChunk(chunk: ServerChunk?, x: Int, z: Int): ServerChunk? {
        return chunk.getLevel().chunkManager.getChunk(chunk.getPosition().getNeighbor(x, z))
    }
}
