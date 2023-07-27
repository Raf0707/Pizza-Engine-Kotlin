package pize.tests.voxelgame.client.level

import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.main.level.Level

class ClientLevel(val session: Minecraft, levelName: String?) : Level() {
    override val chunkManager: ClientChunkManager
    override val configuration: ClientLevelConfiguration

    init {
        chunkManager = ClientChunkManager(this)
        configuration = ClientLevelConfiguration()
        configuration.load(levelName)
    }

    override fun getBlock(x: Int, y: Int, z: Int): Short {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.getBlock(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z))
            ?: Blocks.VOID_AIR.defaultData
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Short): Boolean {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.setBlock(
            ChunkUtils.getLocalCoord(x),
            y,
            ChunkUtils.getLocalCoord(z),
            block
        )
            ?: false
    }

    override fun getHeight(x: Int, z: Int): Int {
        val targetChunk = getBlockChunk(x, z)
        return if (targetChunk != null) targetChunk.getHeightMap(HeightmapType.HIGHEST)!!
            .getHeight(ChunkUtils.getLocalCoord(x), ChunkUtils.getLocalCoord(z)) else 0
    }

    override fun getLight(x: Int, y: Int, z: Int): Byte {
        val targetChunk = getBlockChunk(x, z)
        return targetChunk?.getLight(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z)) ?: 15
    }

    override fun setLight(x: Int, y: Int, z: Int, level: Int) {
        val targetChunk = getBlockChunk(x, z)
        targetChunk?.setLight(ChunkUtils.getLocalCoord(x), y, ChunkUtils.getLocalCoord(z), level)
    }

    override fun getBlockChunk(blockX: Int, blockZ: Int): ClientChunk? {
        return chunkManager.getChunk(ChunkUtils.getChunkPos(blockX), ChunkUtils.getChunkPos(blockZ))
    }
}
