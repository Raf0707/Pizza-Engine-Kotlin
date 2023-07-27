package pize.tests.voxelgame.server.chunk.gen

import pize.math.Mathc.pow
import pize.math.Maths
import pize.math.Maths.map
import pize.math.Maths.quintic
import pize.math.Maths.random
import pize.math.Maths.round
import pize.math.function.FastNoiseLite
import pize.math.util.Random
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.chunk.storage.HeightmapType
import pize.tests.voxelgame.server.chunk.ServerChunk

class DefaultGenerator private constructor() : ChunkGenerator {
    private val continentalnessNoise: FastNoiseLite
    private val erosionNoise: FastNoiseLite
    private val peaksValleysNoise: FastNoiseLite
    private val temperatureNoise: FastNoiseLite
    private val humidityNoise: FastNoiseLite
    private val random = Random(22854)
    override fun generate(chunk: ServerChunk) {
        val baseX = ChunkUtils.SIZE * chunk.position.x
        val baseZ = ChunkUtils.SIZE * chunk.position.z
        val seed = chunk.level.configuration.seed
        continentalnessNoise.setSeed(seed)
        erosionNoise.setSeed(seed)
        peaksValleysNoise.setSeed(seed)
        temperatureNoise.setSeed(seed)
        humidityNoise.setSeed(seed)
        val heightmapSurface = chunk.getHeightMap(HeightmapType.SURFACE)

        // Stopwatch timer = new Stopwatch().start();
        for (lx in 0 until ChunkUtils.SIZE) {
            val x = lx + baseX
            for (lz in 0 until ChunkUtils.SIZE) {
                val z = lz + baseZ
                val erosion = erosionNoise.getNoise(x.toFloat(), z.toFloat()) * Maths.Sqrt2 * 0.5f // [0, 1]
                val continentalness = pow(continentalnessNoise.getNoise(x.toFloat(), z.toFloat()) - 0.4, 3.0)
                val height = round((continentalness * 32 + 128).toDouble())
                for (y in 0 until height) chunk.setBlockFast(lx, y, lz, Blocks.STONE.defaultData)
                val yRange = (ChunkUtils.HEIGHT_IDX - height).toFloat()
                for (y in height until ChunkUtils.HEIGHT) {
                    val heightK = (y - height) / yRange // [0, 1]
                    val continentalness3D = continentalnessNoise.getNoise(
                        x.toFloat(),
                        y.toFloat(),
                        z.toFloat()
                    ) * Maths.Sqrt3 * 0.5f + 1 // [0, 1]
                    if (quintic(erosion * continentalness3D) * 1.2f > heightK) chunk.setBlockFast(
                        lx,
                        y,
                        lz,
                        Blocks.STONE.defaultData
                    )
                }
                for (y in height until OCEAN_LEVEL) if (chunk.getBlockID(
                        lx,
                        y,
                        lz
                    ) == Blocks.AIR.id
                ) chunk.setBlockFast(lx, y, lz, Blocks.WATER.defaultData)
            }
        }
        heightmapSurface!!.update()
        for (lx in 0 until ChunkUtils.SIZE) {
            for (lz in 0 until ChunkUtils.SIZE) {
                val height = heightmapSurface.getHeight(lx, lz)
                if (chunk.getBlockProps(lx, height, lz).id == Blocks.WATER.id) continue
                val stoneLevel = random(
                    round((height.toFloat() / ChunkUtils.HEIGHT_IDX * 2).toDouble()),
                    round((height.toFloat() / ChunkUtils.HEIGHT_IDX * 5).toDouble())
                )
                for (y in height - stoneLevel until height) chunk.setBlockFast(lx, y, lz, Blocks.DIRT.defaultData)
                if (height > OCEAN_LEVEL + 3) chunk.setBlockFast(
                    lx,
                    height,
                    lz,
                    Blocks.GRASS_BLOCK.defaultData
                ) else if (height > OCEAN_LEVEL - 6) chunk.setBlockFast(
                    lx,
                    height,
                    lz,
                    Blocks.SAND.defaultData
                ) else chunk.setBlockFast(lx, height, lz, Blocks.DIRT.defaultData)
            }
        }
        chunk.getHeightMap(HeightmapType.HIGHEST)!!.update()
    }

    override fun generateDecorations(chunk: ServerChunk?) {
        val baseX = ChunkUtils.SIZE * chunk.getPosition().x
        val baseZ = ChunkUtils.SIZE * chunk.getPosition().z
        val heightmapSurface = chunk!!.getHeightMap(HeightmapType.SURFACE)
        val heightmapHighest = chunk.getHeightMap(HeightmapType.HIGHEST)
        for (lx in 0 until ChunkUtils.SIZE) {
            val x = lx + baseX
            for (lz in 0 until ChunkUtils.SIZE) {
                val z = lz + baseZ
                val height = heightmapSurface!!.getHeight(lx, lz)
                if (chunk.getBlockID(lx, height, lz) != Blocks.GRASS_BLOCK.id) continue
                val spawnTree = random.randomBoolean(0.01)
                val generateGrass = random.randomBoolean(0.2)
                if (generateGrass) {
                    if (chunk.setBlockFast(lx, height + 1, lz, Blocks.GRASS.defaultData)) heightmapHighest!!.update(
                        lx,
                        lz
                    )
                } else if (spawnTree) spawnTreeGlobal(chunk, x, height + 1, z)
            }
        }
    }

    private fun spawnTreeGlobal(chunk: ServerChunk?, x: Int, y: Int, z: Int) {
        val logHeight = Math.round(
            map(
                continentalnessNoise.getNoise(x.toFloat(), z.toFloat()),
                -Maths.Sqrt2 * 0.5f,
                Maths.Sqrt2 * 0.5f,
                5f,
                7f
            )
        )
        for (ly in 0 until logHeight) chunk.getLevel().setBlock(x, y + ly, z, Blocks.OAK_LOG.defaultData)
        val peak = y + logHeight

        // Верхний 1
        chunk.getLevel().setBlock(x, peak, z, Blocks.OAK_LEAVES.defaultData)

        // Окружающие ствол дерева 1х4
        chunk.getLevel().setBlock(x - 1, peak, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 1, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 2, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 3, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 1, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 2, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 3, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 1, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 2, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 3, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 1, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 2, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 3, z + 1, Blocks.OAK_LEAVES.defaultData)

        // Другие 1х3
        chunk.getLevel().setBlock(x - 1, peak - 1, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 2, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 3, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 1, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 2, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 3, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 1, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 2, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 3, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 1, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 2, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 3, z + 1, Blocks.OAK_LEAVES.defaultData)

        // Другие по краям 3х2
        chunk.getLevel().setBlock(x - 2, peak - 2, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 2, peak - 2, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 2, peak - 2, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 2, peak - 3, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 2, peak - 3, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 2, peak - 3, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 2, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 2, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 2, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 3, z - 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 3, z, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 2, peak - 3, z + 1, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 2, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 2, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 2, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 3, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 3, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 3, z - 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 2, z + 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 2, z + 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 2, z + 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x - 1, peak - 3, z + 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x, peak - 3, z + 2, Blocks.OAK_LEAVES.defaultData)
        chunk.getLevel().setBlock(x + 1, peak - 3, z + 2, Blocks.OAK_LEAVES.defaultData)
    }

    init {
        continentalnessNoise = FastNoiseLite()
        erosionNoise = FastNoiseLite()
        peaksValleysNoise = FastNoiseLite()
        temperatureNoise = FastNoiseLite()
        humidityNoise = FastNoiseLite()
        continentalnessNoise.setFrequency(0.002f)
        continentalnessNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        continentalnessNoise.setFractalOctaves(7)
        erosionNoise.setFrequency(0.0009f)
        erosionNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        erosionNoise.setFractalOctaves(6)
    }

    override val iD: String
        get() = "default"

    companion object {
        private const val OCEAN_LEVEL = 120
        var instance: DefaultGenerator? = null
            get() {
                if (field == null) field = DefaultGenerator()
                return field
            }
            private set
    }
}
