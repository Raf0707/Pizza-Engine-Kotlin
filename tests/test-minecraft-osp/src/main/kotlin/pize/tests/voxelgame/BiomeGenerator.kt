package pize.tests.voxelgame

import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths
import pize.math.Maths.map
import pize.math.Maths.round
import pize.math.function.FastNoiseLite

class BiomeGenerator : AppAdapter() {
    private val batch: TextureBatch
    private var mapTexture: Texture? = null
    private val continentalnessNoise: FastNoiseLite
    private val erosionNoise: FastNoiseLite
    private val peaksValleysNoise: FastNoiseLite
    private val temperatureNoise: FastNoiseLite
    private val humidityNoise: FastNoiseLite

    init {
        batch = TextureBatch()
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
    }

    override fun init() {
        val map = Pixmap(1024, 1024)
        for (x in 0 until map.width) {
            for (y in 0 until map.height) {
                val continentalness = continentalnessNoise.getNoise(x.toFloat(), y.toFloat())
                val erosion = erosionNoise.getNoise(x.toFloat(), y.toFloat())
                val grayscale =
                    round(map(erosion.toDouble(), -0.55 * Maths.Sqrt2, 0.55 * Maths.Sqrt2, 0.0, 1.0) * 5) / 5f
                map.setPixel(x, y, grayscale.toDouble(), grayscale.toDouble(), grayscale.toDouble(), 1.0)
            }
        }
        mapTexture = Texture(map)
    }

    override fun render() {
        clearColorBuffer()
        batch.begin()
        batch.draw(mapTexture!!, 0f, 0f, 1280f, 1280f)
        batch.end()
    }
}
