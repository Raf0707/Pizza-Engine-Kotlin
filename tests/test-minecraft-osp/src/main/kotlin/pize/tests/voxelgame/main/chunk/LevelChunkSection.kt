package pize.tests.voxelgame.main.chunk

class LevelChunkSection @JvmOverloads constructor(
    val blocks: ShortArray = ShortArray(ChunkUtils.VOLUME),
    val light: ByteArray = ByteArray(ChunkUtils.VOLUME)
) {
    var blocksNum = 0

    fun getBlock(lx: Int, ly: Int, lz: Int): Short {
        return blocks[ChunkUtils.getIndex(lx, ly, lz)]
    }

    fun setBlock(lx: Int, ly: Int, lz: Int, blockState: Short) {
        blocks[ChunkUtils.getIndex(lx, ly, lz)] = blockState
    }

    fun getLight(lx: Int, ly: Int, lz: Int): Byte {
        return light[ChunkUtils.getIndex(lx, ly, lz)]
    }

    fun setLight(lx: Int, ly: Int, lz: Int, level: Int) {
        light[ChunkUtils.getIndex(lx, ly, lz)] = level.toByte()
    }
}
