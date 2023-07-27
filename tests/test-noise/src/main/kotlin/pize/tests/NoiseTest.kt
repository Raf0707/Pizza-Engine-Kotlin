package pize.tests

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.height
import pize.Pize.run
import pize.Pize.width
import pize.app.AppAdapter
import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.math.Maths.map

class NoiseTest : AppAdapter() {
    private val batch: TextureBatch
    private val noise: MyNoise
    private val mapTexture: Texture

    init {
        batch = TextureBatch()
        noise = MyNoise()
        val pixmap = Pixmap(2048, 2048)
        for (x in 0 until pixmap.width) {
            for (y in 0 until pixmap.height) {
                var grayscale = OpenSimplex2S.noise2_ImproveX(22854, (x / 256f).toDouble(), (y / 256f).toDouble())
                grayscale = map(grayscale, -1f, 1f, 0f, 1f)
                println(grayscale)
                pixmap.setPixel(x, y, grayscale.toDouble(), grayscale.toDouble(), grayscale.toDouble(), 1.0)
            }
        }
        mapTexture = Texture(pixmap)
    }

    override fun render() {
        batch.begin()
        batch.draw(mapTexture, 0f, 0f, width.toFloat(), height.toFloat())
        batch.end()
        if (Key.ESCAPE.isDown) exit()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Noise", 720, 720)
            run(NoiseTest())
        }
    }
}