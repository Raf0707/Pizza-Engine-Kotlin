package pize.tests.voxelgame.client.chunk

import pize.math.vecmath.matrix.Matrix4f
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.chunk.mesh.ChunkMeshStack
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.level.ClientLevel
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.ChunkManagerUtils
import pize.tests.voxelgame.main.level.Level

class ClientChunk(level: ClientLevel, position: ChunkPos?) : LevelChunk(level, position) {
    val meshStack: ChunkMeshStack
    val translationMatrix: Matrix4f

    init {
        meshStack = ChunkMeshStack()
        translationMatrix = Matrix4f()
    }

    fun updateTranslationMatrix(camera: GameCamera) {
        translationMatrix.toTranslated(
            position!!.globalX() - camera.x,
            0f,
            position.globalZ() - camera.z
        )
    }

    override fun setBlock(lx: Int, y: Int, lz: Int, blockState: Short): Boolean {
        if (!super.setBlock(lx, y, lz, blockState) || ChunkUtils.isOutOfBounds(lx, lz)) return false
        getHeightMap(HeightmapType.HIGHEST)!!.update(lx, y, lz, BlockData.getID(blockState) != Blocks.AIR.id)
        rebuild(true)
        ChunkManagerUtils.rebuildNeighborChunks(this, lx, lz)
        return true
    }

    override fun setLight(lx: Int, y: Int, lz: Int, level: Int) {
        super.setLight(lx, y, lz, level)
        rebuild(true)
    }

    fun rebuild(important: Boolean) {
        this.level.getChunkManager().rebuildChunk(this, important)
    }

    override val level: Level
        get() = level as ClientLevel

    override fun getNeighbor(chunkX: Int, chunkZ: Int): ClientChunk? {
        return this.level.getChunkManager().getChunk(position!!.x + chunkX, position.z + chunkZ)
    }
}
