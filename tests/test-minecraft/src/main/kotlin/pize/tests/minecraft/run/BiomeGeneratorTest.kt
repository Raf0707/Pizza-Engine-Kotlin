package pize.tests.minecraft.run

import pize.Pize.exit
import pize.Pize.fPS
import pize.Pize.height
import pize.Pize.window
import pize.Pize.x
import pize.Pize.y
import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.io.glfw.Key
import pize.math.Maths.floor
import pize.math.Maths.randomSeed
import pize.math.function.FastNoiseLite

class BiomeGeneratorTest : AppAdapter() {
    private var batch: TextureBatch? = null
    private var mapTexture: Texture? = null
    private var cellTexture: Texture? = null
    override fun init() {
        batch = TextureBatch()
        generate(16 * 100)
        val cellPixmap = Pixmap(16, 16)
        cellPixmap.clear(1.0, 1.0, 1.0, 1.0)
        cellPixmap.fill(1, 1, 14, 14, 0.0, 0.0, 0.0, 0.0)
        cellTexture = Texture(cellPixmap)
    }

    override fun render() {
        window()!!.title = "Minecraft (fps: " + fPS + ")"
        if (Key.ESCAPE.isDown) exit()
        if (Key.F11.isDown) window()!!.toggleFullscreen()
        if (Key.V.isDown) window()!!.toggleVsync()
        clearColorBuffer()
        clearColor(0.4, 0.6, 1.0)
        batch!!.begin()
        batch!!.draw(mapTexture!!, 0f, 0f, height.toFloat(), height.toFloat())
        val pixel16 = height / mapTexture!!.height.toFloat() * 16
        batch!!.draw(
            cellTexture!!,
            floor((x / pixel16).toDouble()) * pixel16,
            floor((y / pixel16).toDouble()) * pixel16,
            pixel16,
            pixel16
        )
        batch!!.end()
    }

    override fun dispose() {
        mapTexture!!.dispose()
        cellTexture!!.dispose()
        batch!!.dispose()
    }

    private fun generate(size: Int) {
        val mapPixmap = Pixmap(size, size)
        mapPixmap.clear(1.0, 1.0, 1.0, 1.0)
        val heightMapNoise = FastNoiseLite()
        heightMapNoise.setNoiseType(FastNoiseLite.NoiseType.Perlin)
        heightMapNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        heightMapNoise.setSeed(randomSeed(4).toInt())
        heightMapNoise.setFractalOctaves(8)
        heightMapNoise.setFrequency(0.002f)
        heightMapNoise.setRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes)
        val riverNoise = FastNoiseLite()
        riverNoise.setNoiseType(FastNoiseLite.NoiseType.Perlin)
        riverNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        riverNoise.setSeed(randomSeed(4).toInt())
        riverNoise.setFractalOctaves(16)
        riverNoise.setFrequency(0.003f)
        val temperatureNoise = FastNoiseLite()
        temperatureNoise.setNoiseType(FastNoiseLite.NoiseType.Perlin)
        temperatureNoise.setFractalType(FastNoiseLite.FractalType.FBm)
        temperatureNoise.setSeed(randomSeed(4).toInt())
        temperatureNoise.setFrequency(0.002f)
        temperatureNoise.setFractalOctaves(8)
        for (x in 0 until mapPixmap.width) {
            for (z in 0 until mapPixmap.height) {
                val temperature = temperatureNoise.getNoise(x.toFloat(), z.toFloat()) / 2 + 0.5f
                //float longHeight = periodicHeightMapNoise.getNoise(x,z);
                //longHeight = longHeight / Mathc.pow(1 - longHeight,-1.2);
                val height = heightMapNoise.getNoise(x.toFloat(), z.toFloat()) / 2 + 0.5f // + longHeight;
                val oceanLevel = 0.43f
                val humidity = 1 - height
                val river = riverNoise.getNoise(x.toFloat(), z.toFloat()) / 2 + 0.5f
                val color = Color(0f, 0f, 0f, 0f)
                if (height <= oceanLevel) {
                    if (temperature > 0.4) color[0.1f, 0.3f, 1f] = 1f // ocean
                    else color[0.6f, 0.8f, 1f] = 1f // ice ocean
                } else if (river > 0.49 && river < 0.51) {
                    if (temperature > 0.4) color[0.2f, 0.4f, 0.9f] = 1f // river
                    else color[0.7f, 0.8f, 0.9f] = 1f // ice river
                } else {
                    if (humidity > 0.55) {
                        if (temperature > 0.4) color[1f, 1f, 0f] = 1f // beach
                        else color[1f, 1f, 0.5f] = 1f // snowy beach
                    } else if (humidity > 0.35) {
                        if (temperature > 0.8) color[0.4f, 0.6f, 0f] = 1f // savanna
                        else if (temperature > 0.52) color[0f, 0.8f, 0.2f] = 1f // normal
                        else if (temperature > 0.4) color[0.1f, 0.6f, 0.2f] = 1f // taiga
                        else color[1f, 1f, 1f] = 1f // snowy taiga
                    } else {
                        if (temperature > 0.6) color[1f, 1f, 0f] = 1f // desert
                        else color[0.6f, 0.8f, 0.6f] = 1f // windswept hills
                    }
                }
                mapPixmap.setPixel(x, z, color)
            }
        }
        mapTexture = Texture(mapPixmap)
    }
}