package pize.tests.voxelgame.server.chunk.gen

import pize.math.Maths.round
import pize.math.function.FastNoiseLite
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.server.chunk.ServerChunk

class FlatGenerator : ChunkGenerator {
    private val noiseLight = FastNoiseLite()
    override fun generate(chunk: ServerChunk) {
        val baseX = ChunkUtils.SIZE * chunk.position.x
        val baseZ = ChunkUtils.SIZE * chunk.position.z

        // Stopwatch timer = new Stopwatch().start();
        for (i in 0 until ChunkUtils.SIZE) for (j in 0 until ChunkUtils.SIZE) for (k in 0..4) chunk.setBlockFast(
            i,
            k,
            j,
            Blocks.STONE.defaultData
        )
        chunk.getHeightMap(HeightmapType.HIGHEST)!!.update()
        for (i in 0 until ChunkUtils.SIZE) for (j in 0 until ChunkUtils.SIZE) {
            val y = chunk.getHeightMap(HeightmapType.HIGHEST)!!.getHeight(i, j)
            chunk.setBlockFast(i, y, j, Blocks.GRASS_BLOCK.defaultData)
        }
        // System.out.println("Gen: " + timer.getMillis());
        for (lx in 0 until ChunkUtils.SIZE) {
            val x = lx + baseX
            for (lz in 0 until ChunkUtils.SIZE) {
                val z = lz + baseZ
                for (y in 0 until ChunkUtils.HEIGHT) chunk.setLight(
                    lx,
                    y,
                    lz,
                    round(((noiseLight.getNoise(x.toFloat(), y.toFloat(), z.toFloat()) + 1) / 2 * 15).toDouble())
                )
            }
        }
    }

    override fun generateDecorations(chunk: ServerChunk?) {}

    init {
        noiseLight.setFrequency(0.03f)
    }

    override val iD: String
        get() = "flat"

    companion object {
        var instance: FlatGenerator? = null
            get() {
                if (field == null) field = FlatGenerator()
                return field
            }
            private set
    }
}