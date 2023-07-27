package pize.tests.voxelgame.server.level

import pize.math.vecmath.vector.Vec2i
import pize.math.vecmath.vector.Vec3i
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.Direction
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.server.chunk.ServerChunk
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.max

class LightEngine(private val level: ServerLevel) {
    private class LightNode(val chunk: ServerChunk?, lx: Int, y: Int, lz: Int, level: Int) {
        val lx: Byte
        val y: Short
        val lz: Byte
        val level: Byte

        init {
            this.lx = lx.toByte()
            this.y = y.toShort()
            this.lz = lz.toByte()
            this.level = level.toByte()
        }
    }

    private val bfsIncreaseQueue: ConcurrentLinkedQueue<LightNode>
    private val bfsDecreaseQueue: ConcurrentLinkedQueue<LightNode>

    init {
        bfsIncreaseQueue = ConcurrentLinkedQueue()
        bfsDecreaseQueue = ConcurrentLinkedQueue()
    }

    fun increase(chunk: ServerChunk, lx: Int, y: Int, lz: Int, level: Int) {
        if (chunk.getLight(lx, y, lz) >= level) return
        chunk.setLight(lx, y, lz, level)
        addIncrease(chunk, lx, y, lz, level)
        propagateIncrease()
    }

    private fun addIncrease(chunk: ServerChunk?, lx: Int, y: Int, lz: Int, level: Int) {
        bfsIncreaseQueue.add(LightNode(chunk, lx, y, lz, level))
    }

    private fun propagateIncrease() {
        var neighborChunk: ServerChunk?
        var neighborX: Int
        var neighborY: Int
        var neighborZ: Int
        var targetLevel: Int
        while (!bfsIncreaseQueue.isEmpty()) {
            val lightEntry = bfsIncreaseQueue.poll()
            val chunk = lightEntry.chunk
            val x = lightEntry.lx
            val y = lightEntry.y
            val z = lightEntry.lz
            val level = lightEntry.level
            for (i in 0..5) {
                val normal = Direction.entries[i].normal
                neighborX = x + normal!!.x
                neighborZ = z + normal.z
                if (neighborX > ChunkUtils.SIZE_IDX || neighborZ > ChunkUtils.SIZE_IDX || neighborX < 0 || neighborZ < 0) {
                    neighborChunk = ChunkUtils.getNeighborChunk(chunk, normal.x, normal.z)
                    if (neighborChunk == null) continue
                    neighborX = ChunkUtils.getLocalCoord(neighborX)
                    neighborZ = ChunkUtils.getLocalCoord(neighborZ)
                } else neighborChunk = chunk
                neighborY = y + normal.y
                if (neighborY < 0 || neighborY > ChunkUtils.HEIGHT_IDX) continue
                val neighborLevel = neighborChunk!!.getLight(neighborX, neighborY, neighborZ).toInt()
                if (neighborLevel >= level - 1) continue
                val neighborProperties = neighborChunk.getBlockProps(neighborX, neighborY, neighborZ)
                targetLevel = (level - max(1.0, neighborProperties.opacity.toDouble())).toInt()
                if (targetLevel > neighborLevel) {
                    neighborChunk.setLight(neighborX, neighborY, neighborZ, targetLevel)
                    addIncrease(neighborChunk, neighborX, neighborY, neighborZ, targetLevel)
                }
            }
        }
    }

    fun decrease(chunk: ServerChunk, lx: Int, y: Int, lz: Int, level: Int) {
        if (chunk.getLight(lx, y, lz) >= level) return
        chunk.setLight(lx, y, lz, 0)
        addDecrease(chunk, lx, y, lz, level)
        propagateDecrease()
    }

    private fun addDecrease(chunk: ServerChunk?, lx: Int, y: Int, lz: Int, level: Int) {
        bfsIncreaseQueue.add(LightNode(chunk, lx, y, lz, level))
    }

    private fun propagateDecrease() {
        var neighborChunk: ServerChunk?
        var neighborX: Int
        var neighborY: Int
        var neighborZ: Int
        while (!bfsDecreaseQueue.isEmpty()) {
            val lightEntry = bfsDecreaseQueue.poll()
            val chunk = lightEntry.chunk
            val x = lightEntry.lx
            val y = lightEntry.y
            val z = lightEntry.lz
            val level = lightEntry.level
            for (i in 0..5) {
                val normal: Vec3i = Direction.Companion.normal3DFromIndex(i)
                neighborX = x + normal.x
                neighborZ = z + normal.z
                if (neighborX > ChunkUtils.SIZE_IDX || neighborZ > ChunkUtils.SIZE_IDX || neighborX < 0 || neighborZ < 0) {
                    neighborChunk = ChunkUtils.getNeighborChunk(chunk, normal.x, normal.z)
                    if (neighborChunk == null) continue
                    neighborX = ChunkUtils.getLocalCoord(neighborX)
                    neighborZ = ChunkUtils.getLocalCoord(neighborZ)
                } else neighborChunk = chunk
                neighborY = y + normal.y
                if (neighborY < 0 || neighborY > ChunkUtils.HEIGHT_IDX) continue
                val neighborLevel = neighborChunk!!.getLight(neighborX, neighborY, neighborZ).toInt()
                if (neighborLevel != 0 && level > neighborLevel) {
                    neighborChunk.setLight(neighborX, neighborY, neighborZ, 0)
                    val neighborBlock = neighborChunk.getBlockProps(neighborX, neighborY, neighborZ)
                    addDecrease(
                        neighborChunk,
                        neighborX,
                        neighborY,
                        neighborZ,
                        max(level.toDouble(), (neighborLevel + neighborBlock.opacity).toDouble())
                            .toInt()
                    )
                } else if (level <= neighborLevel) addIncrease(
                    neighborChunk,
                    neighborX,
                    neighborY,
                    neighborZ,
                    neighborLevel
                )
            }
        }
        propagateIncrease()
    }

    fun updateSkyLight(chunk: ServerChunk) {
        val heightmapHighest = chunk.getHeightMap(HeightmapType.HIGHEST)
        // final Heightmap heightmapOpaque = chunk.getHeightMap(HeightmapType.OPAQUE);
        for (lx in 0 until ChunkUtils.SIZE) {
            for (lz in 0 until ChunkUtils.SIZE) {
                val height = heightmapHighest!!.getHeight(lx, lz)
                for (y in ChunkUtils.HEIGHT_IDX downTo height) {
                    //chunk.setLight(lx, y, lz, 15);
                    updateSideSkyLight(chunk, lx, lz)
                }
                increase(chunk, lx, height, lz, 15)
            }
        }
    }

    private fun updateSideSkyLight(chunk: ServerChunk, x: Int, z: Int) {
        val chunkPos = chunk.position
        val heightmapHighest = chunk.getHeightMap(HeightmapType.HIGHEST)
        val height = heightmapHighest!!.getHeight(x, z)
        for (i in 0..3) {
            val dirNor: Vec2i = Direction.Companion.normal2DFromIndex(i)
            val globalSideX = chunkPos!!.globalX() + x + dirNor.x
            val globalSideZ = chunkPos.globalZ() + z + dirNor.y
            val sideChunk = level.getBlockChunk(globalSideX, globalSideZ) ?: continue
            val localSideX = ChunkUtils.getLocalCoord(globalSideX)
            val localSideZ = ChunkUtils.getLocalCoord(globalSideZ)
            val sideHeight = heightmapHighest.getHeight(localSideX, localSideZ)
            if (sideHeight <= height) continue
            var checkY = height + 1
            if (checkY == sideHeight) continue
            while (checkY < sideHeight) {
                if (chunk.getBlockID(localSideX, checkY, localSideZ) != Blocks.AIR.id) {
                    checkY++
                    continue
                }
                increase(sideChunk, localSideX, checkY, localSideZ, 15)
                chunk.setBlock(localSideX, checkY, localSideZ, Blocks.GLASS.defaultData)
                checkY++
            }
        }
    }
}
