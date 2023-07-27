package pize.tests.voxelgame.main.level

import pize.math.vecmath.vector.Vec2f
import pize.math.vecmath.vector.Vec2f.Companion.len
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.main.chunk.ChunkUtils

object ChunkManagerUtils {
    fun rebuildNeighborChunks(chunk: ClientChunk?) {
        var neighbor: ClientChunk?
        neighbor = ChunkUtils.getNeighborChunk(chunk, -1, 0)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, -1, 1)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, -1, -1)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, 1, 0)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, 1, 1)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, 1, -1)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, 0, 1)
        neighbor?.rebuild(false)
        neighbor = ChunkUtils.getNeighborChunk(chunk, 0, -1)
        neighbor?.rebuild(false)
    }

    fun rebuildNeighborChunks(chunk: ClientChunk?, lx: Int, lz: Int) {
        var neighbor: ClientChunk?
        if (lx == 0) {
            neighbor = ChunkUtils.getNeighborChunk(chunk, -1, 0)
            neighbor?.rebuild(false)
            if (lz == ChunkUtils.SIZE_IDX) {
                neighbor = ChunkUtils.getNeighborChunk(chunk, -1, 1)
                neighbor?.rebuild(false)
            } else if (lz == 0) {
                neighbor = ChunkUtils.getNeighborChunk(chunk, -1, -1)
                neighbor?.rebuild(false)
            }
        } else if (lx == ChunkUtils.SIZE_IDX) {
            neighbor = ChunkUtils.getNeighborChunk(chunk, 1, 0)
            neighbor?.rebuild(false)
            if (lz == ChunkUtils.SIZE_IDX) {
                neighbor = ChunkUtils.getNeighborChunk(chunk, 1, 1)
                neighbor?.rebuild(false)
            } else if (lz == 0) {
                neighbor = ChunkUtils.getNeighborChunk(chunk, 1, -1)
                neighbor?.rebuild(false)
            }
        }
        if (lz == ChunkUtils.SIZE_IDX) {
            neighbor = ChunkUtils.getNeighborChunk(chunk, 0, 1)
            neighbor?.rebuild(false)
        } else if (lz == 0) {
            neighbor = ChunkUtils.getNeighborChunk(chunk, 0, -1)
            neighbor?.rebuild(false)
        }
    }

    fun distToChunk(x: Int, z: Int, pos: Vec3f): Float {
        return len((x - pos.x / ChunkUtils.SIZE + 0.5f).toDouble(), (z - pos.z / ChunkUtils.SIZE + 0.5f).toDouble())
    }

    fun distToChunk(x: Int, z: Int, pos: Vec2f?): Float {
        return len((x - pos!!.x / ChunkUtils.SIZE + 0.5f).toDouble(), (z - pos.y / ChunkUtils.SIZE + 0.5f).toDouble())
    }
}
