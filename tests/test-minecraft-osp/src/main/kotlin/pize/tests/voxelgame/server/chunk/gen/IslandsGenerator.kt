package pize.tests.voxelgame.server.chunk.gen

import pize.math.Maths.round
import pize.math.function.FastNoiseLite
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.server.chunk.ServerChunk

class IslandsGenerator private constructor() : ChunkGenerator {
    private val continentalnessNoise: FastNoiseLite
    private val erosionNoise: FastNoiseLite
    private val peaksValleysNoise: FastNoiseLite
    private val temperatureNoise: FastNoiseLite
    private val humidityNoise: FastNoiseLite
    private val noiseLight = FastNoiseLite()
    override fun generate(chunk: ServerChunk) {
        val baseX = ChunkUtils.SIZE * chunk.position.x
        val baseZ = ChunkUtils.SIZE * chunk.position.z
        val seed = chunk.level.configuration.seed
        continentalnessNoise.setSeed(seed)
        erosionNoise.setSeed(seed)
        peaksValleysNoise.setSeed(seed)
        temperatureNoise.setSeed(seed)
        humidityNoise.setSeed(seed)

        // Stopwatch timer = new Stopwatch().start();
        for (lx in 0 until ChunkUtils.SIZE) {
            val x = lx + baseX
            for (lz in 0 until ChunkUtils.SIZE) {
                val z = lz + baseZ
                val erosion = (erosionNoise.getNoise(x.toFloat(), z.toFloat()) + 1) * 0.5f
                val density = 0.1f / erosion
                val height = round((continentalnessNoise.getNoise(x.toFloat(), z.toFloat()) * 16 + 128).toDouble())
                for (y in height until ChunkUtils.HEIGHT) {
                    val continentalness3D =
                        (continentalnessNoise.getNoise(x.toFloat(), y.toFloat(), z.toFloat()) + 1) * 0.5f
                    if (continentalness3D < y.toFloat() / (ChunkUtils.HEIGHT - height) * density) chunk.setBlockFast(
                        lx,
                        y,
                        lz,
                        Blocks.STONE.defaultData
                    )
                }
            }
        }
        chunk.getHeightMap(HeightmapType.HIGHEST)!!.update()
        for (lx in 0 until ChunkUtils.SIZE) {
            for (lz in 0 until ChunkUtils.SIZE) {
                val height = chunk.getHeightMap(HeightmapType.HIGHEST)!!.getHeight(lx, lz)
                if (height > 60) chunk.setBlockFast(lx, height, lz, Blocks.GRASS_BLOCK.defaultData)
            }
        }

        // System.out.println("Gen: " + timer.getMillis());
    }

    override fun generateDecorations(chunk: ServerChunk?) {}

    init {
        continentalnessNoise = FastNoiseLite()
        erosionNoise = FastNoiseLite()
        peaksValleysNoise = FastNoiseLite()
        temperatureNoise = FastNoiseLite()
        humidityNoise = FastNoiseLite()
        continentalnessNoise.setFrequency(0.002f)
        continentalnessNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        continentalnessNoise.setFractalOctaves(7)
        erosionNoise.setFrequency(0.002f)
        erosionNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        erosionNoise.setFractalOctaves(5)
        noiseLight.setFrequency(0.03f)
    }

    override val iD: String
        get() = "islands"

    companion object {
        var instance: IslandsGenerator? = null
            get() {
                if (field == null) field = IslandsGenerator()
                return field
            }
            private set
    }
}
