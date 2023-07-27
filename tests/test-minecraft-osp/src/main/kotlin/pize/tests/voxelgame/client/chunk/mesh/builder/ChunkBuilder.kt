package pize.tests.voxelgame.client.chunk.mesh.builder

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshCullingOff
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshPackedCullingOn
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshTranslucentCullingOn
import pize.tests.voxelgame.client.level.ClientChunkManager
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.util.time.Stopwatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.Volatile

class ChunkBuilder(val chunkManager: ClientChunkManager) {
    var chunk: ClientChunk? = null
    var solidMesh: ChunkMeshPackedCullingOn? = null
    var customMesh: ChunkMeshCullingOff? = null
    var translucentMesh: ChunkMeshTranslucentCullingOn? = null
    var buildTime = 0.0
    var verticesNum = 0
    private var neighborChunks: Array<ClientChunk?>

    @Volatile
    private val state = AtomicInteger()
    fun getState(): Int {
        return state.get()
    }

    fun build(chunk: ClientChunk): Boolean {
        if (state.get() != 0) return false
        state.set(1)
        val timer = Stopwatch().start()

        // Init
        this.chunk = chunk
        neighborChunks = arrayOf( // Rows - X, Columns - Z
            chunk.getNeighbor(-1, -1), chunk.getNeighbor(0, -1), chunk.getNeighbor(1, -1),
            chunk.getNeighbor(-1, 0), chunk, chunk.getNeighbor(1, 0),
            chunk.getNeighbor(-1, 1), chunk.getNeighbor(0, 1), chunk.getNeighbor(1, 1)
        )

        // Get Meshes
        val meshStack = chunk.meshStack
        solidMesh = meshStack.packed
        customMesh = meshStack.custom
        translucentMesh = meshStack.translucent

        // Build
        val heightmap = chunk.getHeightMap(HeightmapType.HIGHEST)
        state.incrementAndGet()
        if (!chunk.isEmpty) for (lx in 0 until ChunkUtils.SIZE) {
            state.incrementAndGet()
            for (lz in 0 until ChunkUtils.SIZE) {
                val height = heightmap!!.getHeight(lx, lz) + 1
                for (y in 0 until height) {
                    val blockData = chunk.getBlock(lx, y, lz)
                    val block = BlockData.getProps(blockData)
                    val blockState = BlockData.getState(blockData)
                    if (block.isEmpty) continue
                    val model = block!!.getState(blockState.toInt()).model ?: continue
                    model.build(this, block, lx, y, lz)

                    // if(block.isSolid()){
                    //     final BlockModelSolid solidModel = model.asSolid();
                    //
                    //     if(isGenSolidFace(lx, y, lz, -1,  0,  0, block)) solidModel.buildNxFaces(this, lx, y, lz);
                    //     if(isGenSolidFace(lx, y, lz, +1,  0,  0, block)) solidModel.buildPxFaces(this, lx, y, lz);
                    //     if(isGenSolidFace(lx, y, lz,  0, -1,  0, block)) solidModel.buildNyFaces(this, lx, y, lz);
                    //     if(isGenSolidFace(lx, y, lz,  0, +1,  0, block)) solidModel.buildPyFaces(this, lx, y, lz);
                    //     if(isGenSolidFace(lx, y, lz,  0,  0, -1, block)) solidModel.buildNzFaces(this, lx, y, lz);
                    //     if(isGenSolidFace(lx, y, lz,  0,  0, +1, block)) solidModel.buildPzFaces(this, lx, y, lz);
                    // }else{
                    //     final float[] add = new float[]{lx, y, lz, 0, 0, 0, 0, 0, 0};
                    //     final float light = chunk.getLight(lx, y, lz) / 15F;
                    //     final float[] mul = new float[]{1, 1, 1, light, light, light, 1, 1, 1};
                    //     final BlockModelCustom customModel = model.asCustom();
                    //     for(int i = 0; i < customModel.getVertices().size(); i++){
                    //         float vertex = customModel.getVertices().get(i) * mul[i % 9] + add[i % 9];
                    //         customMesh.put(vertex);
                    //     }
                    // }
                }
            }
        }
        state.set(54)

        // Update meshes
        verticesNum = 0
        verticesNum += solidMesh!!.updateVertices()
        verticesNum += customMesh!!.updateVertices()
        verticesNum += translucentMesh!!.updateVertices()

        // Time
        buildTime = timer.millis
        state.set(0)
        return true
    }

    fun isGenSolidFace(
        lx: Int,
        y: Int,
        lz: Int,
        normalX: Int,
        normalY: Int,
        normalZ: Int,
        block: BlockProperties?
    ): Boolean {
        if (ChunkUtils.isOutOfBounds(y)) return true
        val neighborData = getBlock(lx + normalX, y + normalY, lz + normalZ)
        val neighborState = BlockData.getState(neighborData)
        val neighbor = BlockData.getProps(neighborData)
        return if (neighbor.id == Blocks.VOID_AIR.id) false else neighbor!!.isSolid && neighbor.getState(neighborState.toInt()).model.isFaceTransparentForNeighbors(
            -normalX,
            -normalY,
            -normalZ
        ) || neighbor.isEmpty || neighbor.isLightTranslucent && !block!!.isLightTranslucent
    }

    fun getAO(
        x1: Int,
        y1: Int,
        z1: Int,
        x2: Int,
        y2: Int,
        z2: Int,
        x3: Int,
        y3: Int,
        z3: Int,
        x4: Int,
        y4: Int,
        z4: Int
    ): Float {
        val block1 = getBlockProps(x1, y1, z1)
        val block2 = getBlockProps(x2, y2, z2)
        val block3 = getBlockProps(x3, y3, z3)
        val block4Data = getBlock(x4, y4, z4)
        val block4State = BlockData.getState(block4Data)
        val block4 = BlockData.getProps(block4Data)
        var result = 0f
        if (!(block1.isEmpty() || block1!!.isLightTranslucent)) result++
        if (!(block2.isEmpty() || block2!!.isLightTranslucent)) result++
        if (!(block3.isEmpty() || block3!!.isLightTranslucent)) result++
        val normalX = (x1 + x2 + x3 + x4) / 2
        val normalY = (y1 + y2 + y3 + y4) / 2
        val normalZ = (z1 + z2 + z3 + z4) / 2
        if (!(block4.isEmpty || block4!!.isLightTranslucent) || block4!!.isSolid && block4.getState(block4State.toInt()).model.isFaceTransparentForNeighbors(
                normalX,
                normalY,
                normalZ
            )
        ) result++
        return 1 - result / 5
    }

    fun getLight(
        x1: Int,
        y1: Int,
        z1: Int,
        x2: Int,
        y2: Int,
        z2: Int,
        x3: Int,
        y3: Int,
        z3: Int,
        x4: Int,
        y4: Int,
        z4: Int
    ): Float {
        var result = 0f
        var n: Byte = 0
        if (getBlockProps(x1, y1, z1)!!.isLightTranslucent) {
            result += getLight(x1, y1, z1).toFloat()
            n++
        }
        if (getBlockProps(x2, y2, z2)!!.isLightTranslucent) {
            result += getLight(x2, y2, z2).toFloat()
            n++
        }
        if (getBlockProps(x3, y3, z3)!!.isLightTranslucent) {
            result += getLight(x3, y3, z3).toFloat()
            n++
        }
        if (getBlockProps(x4, y4, z4)!!.isLightTranslucent) {
            result += getLight(x4, y4, z4).toFloat()
            n++
        }
        return if (n.toInt() == 0) 0 else result / n
    }

    fun getBlockProps(lx: Int, y: Int, lz: Int): BlockProperties? {
        return BlockData.getProps(getBlock(lx, y, lz))
    }

    fun getBlock(lx: Int, y: Int, lz: Int): Short {
        // Находим соседний чанк в массиве 3x3
        var lx = lx
        var lz = lz
        var signX = 0
        var signZ = 0
        if (lx > ChunkUtils.SIZE_IDX) {
            signX = 1
            lx -= ChunkUtils.SIZE
        } else if (lx < 0) {
            signX = -1
            lx += ChunkUtils.SIZE
        }
        if (lz > ChunkUtils.SIZE_IDX) {
            signZ = 1
            lz -= ChunkUtils.SIZE
        } else if (lz < 0) {
            signZ = -1
            lz += ChunkUtils.SIZE
        }
        val chunk = neighborChunks[(signZ + 1) * 3 + (signX + 1)] ?: return Blocks.VOID_AIR.defaultData

        // Возвращаем блок
        return chunk.getBlock(lx, y, lz)
    }

    fun getLight(lx: Int, y: Int, lz: Int): Byte {
        // Находим соседний чанк в массиве 3x3 (neighborChunks)
        var lx = lx
        var lz = lz
        var signX = 0
        var signZ = 0
        if (lx > ChunkUtils.SIZE_IDX) {
            signX = 1
            lx -= ChunkUtils.SIZE
        } else if (lx < 0) {
            signX = -1
            lx += ChunkUtils.SIZE
        }
        if (lz > ChunkUtils.SIZE_IDX) {
            signZ = 1
            lz -= ChunkUtils.SIZE
        } else if (lz < 0) {
            signZ = -1
            lz += ChunkUtils.SIZE
        }
        val chunk = neighborChunks[(signZ + 1) * 3 + (signX + 1)] ?: return 15

        // Возвращаем уровень света
        return chunk.getLight(lx, y, lz)
    }
}
