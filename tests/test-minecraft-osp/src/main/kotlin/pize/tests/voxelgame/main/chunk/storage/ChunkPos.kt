package pize.tests.voxelgame.main.chunk.storage

import pize.tests.voxelgame.main.chunk.ChunkUtils
import java.util.*

class ChunkPos(val x: Int, val z: Int) {
    fun getNeighbor(x: Int, z: Int): ChunkPos {
        return ChunkPos(this.x + x, this.z + z)
    }

    fun globalX(): Int {
        return x * ChunkUtils.SIZE
    }

    fun globalZ(): Int {
        return z * ChunkUtils.SIZE
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` === this) return true
        if (`object` == null || `object`.javaClass != javaClass) return false
        val chunkPos = `object` as ChunkPos
        return x == chunkPos.x && z == chunkPos.z
    }

    override fun hashCode(): Int {
        return Objects.hash(x, z)
    }

    override fun toString(): String {
        return "$x, $z"
    }
}
