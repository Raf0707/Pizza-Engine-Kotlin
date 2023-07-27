package pize.tests.voxelgame.main.chunk.storage

import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.LevelChunk

class Heightmap {
    val chunk: LevelChunk
    val type: HeightmapType
    val values: ShortArray

    constructor(chunk: LevelChunk, type: HeightmapType) {
        this.chunk = chunk
        this.type = type
        values = ShortArray(ChunkUtils.AREA)
    }

    constructor(chunk: LevelChunk, type: HeightmapType, values: ShortArray) {
        this.chunk = chunk
        this.type = type
        this.values = values
    }

    fun getHeight(lx: Int, lz: Int): Int {
        return values[ChunkUtils.getIndex(lx, lz)].toInt()
    }

    fun setHeight(lx: Int, lz: Int, height: Int) {
        values[ChunkUtils.getIndex(lx, lz)] = height.toShort()
    }

    fun update(lx: Int, y: Int, lz: Int, blockPlaced: Boolean) {
        var height = getHeight(lx, lz)
        if (y == height && !blockPlaced) {
            height--
            while (type.isOpaque.test(chunk.getBlockID(lx, height, lz)) && height >= 0) {
                height--
            }
        } else if (y > height && blockPlaced) height = y
        setHeight(lx, lz, height)
    }

    fun update(lx: Int, lz: Int) {
        var height = ChunkUtils.HEIGHT
        height--
        while (type.isOpaque.test(chunk.getBlockID(lx, height, lz)) && height >= 0) {
            height--
        }
        setHeight(lx, lz, height)
    }

    fun update() {
        for (lx in 0 until ChunkUtils.SIZE) for (lz in 0 until ChunkUtils.SIZE) update(lx, lz)
    }

    fun updateFrom(heightmap: Heightmap) {
        System.arraycopy(heightmap.values, 0, values, 0, heightmap.values.size)
    }
}
