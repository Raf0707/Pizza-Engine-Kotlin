package pize.tests.minecraft.run

import pize.Pize.exit
import pize.Pize.fPS
import pize.Pize.isTouched
import pize.Pize.mouse
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
import pize.graphics.util.color.IColor
import pize.io.glfw.Key
import pize.math.Maths.random
import pize.math.vecmath.vector.Vec2i
import kotlin.math.min

class GreedyMeshTest : AppAdapter() {
    private var batch: TextureBatch? = null
    private var voxelTexture: Texture? = null
    private val WIDTH = 5
    private val HEIGHT = 5
    private val SIZE = 140
    private var colors: Array<IColor?>
    private var map: Array<IntArray>
    private var sizes: Array<Array<Vec2i?>>
    var mesh: ArrayList<Quad>? = null
    override fun init() {
        batch = TextureBatch(5000)
        val voxelPixmap = Pixmap(16, 16)
        voxelPixmap.fill(0, 0, 15, 15, 1.0, 1.0, 1.0, 1.0)
        voxelPixmap.fill(0, 0, 0, 15, 0.0, 0.0, 0.0, 1.0)
        voxelPixmap.fill(0, 0, 15, 0, 0.0, 0.0, 0.0, 1.0)
        voxelPixmap.fill(15, 0, 15, 15, 0.5, 0.5, 0.5, 1.0)
        voxelPixmap.fill(0, 15, 14, 15, 0.5, 0.5, 0.5, 1.0)
        voxelTexture = Texture(voxelPixmap)
        map = Array(WIDTH) { IntArray(HEIGHT) }
        for (i in 0 until WIDTH) for (j in 0 until HEIGHT) map[i][j] = random(2)
        sizes = Array(WIDTH) { arrayOfNulls(HEIGHT) }
        for (i in 0 until WIDTH) for (j in 0 until HEIGHT) sizes[i][j] = Vec2i(1)
        colors = arrayOfNulls(3)
        colors[0] = Color(1f, 0f, 0f, 1f)
        colors[1] = Color(0f, 1f, 0f, 1f)
        colors[2] = Color(0f, 0f, 1f, 1f)
        mesh = ArrayList()
        buildMesh()
    }

    fun buildMesh() {
        for (i in 0 until WIDTH) for (j in 0 until HEIGHT) sizes[i][j]!!.set(1)
        mesh!!.clear()
        for (j in 0 until HEIGHT) for (i in 0 until WIDTH - 1) if (map[i][j] == map[i + 1][j]) {
            val offset = min(sizes[i][j]!!.x.toDouble(), 0.0).toInt()
            val size = sizes[i + offset][j]
            size!!.x += sizes[i + 1][j]!!.x
            sizes[i + 1][j]!!.x = offset - 1
        }
        var i = 0
        while (i < WIDTH) {
            for (j in 0 until HEIGHT - 1) if (map[i][j] == map[i][j + 1]) {
                if (sizes[i][j]!!.y < 0 || sizes[i][j]!!.x < 0) continue
                val offsetX = min(sizes[i][j]!!.y.toDouble(), 0.0).toInt()
                println(offsetX)
                if (sizes[i][j]!!.x == sizes[i][j + 1]!!.x) {
                    val offsetY = min(sizes[i][j]!!.y.toDouble(), 0.0).toInt()
                    val size = sizes[i][j + offsetY]
                    size!!.y += sizes[i][j + 1]!!.y
                    sizes[i][j + 1]!!.y = offsetY - 1
                    if (i + offsetY < WIDTH - 1) i += offsetY
                }
            }
            i++
        }
        for (j in 0 until HEIGHT) for (i in 0 until WIDTH) {
            if (sizes[i][j]!!.y < 0 || sizes[i][j]!!.x < 0) continue
            val size = sizes[i][j]
            mesh!!.add(Quad(i, j, size!!.x, size.y, map[i][j]))
        }
    }

    override fun render() {
        window()!!.title = "Minecraft (fps: " + fPS + ")"
        if (Key.ESCAPE.isDown) exit()
        if (Key.F11.isDown) window()!!.toggleFullscreen()
        if (Key.V.isDown) window()!!.toggleVsync()
        clearColorBuffer()
        clearColor(0.4, 0.6, 1.0)
        batch!!.begin()
        if (Key.S.isPressed) for (i in 0 until WIDTH) for (j in 0 until HEIGHT) {
            batch!!.setColor(colors[map[i][j]]!!)
            batch!!.draw(voxelTexture!!, (i * SIZE).toFloat(), (j * SIZE).toFloat(), SIZE.toFloat(), SIZE.toFloat())
        } else {
            for ((x1, y1, width1, height1, color) in mesh!!) {
                batch!!.setColor(colors[color]!!)
                batch!!.draw(
                    voxelTexture!!,
                    (x1 * SIZE).toFloat(),
                    (y1 * SIZE).toFloat(),
                    (width1 * SIZE).toFloat(),
                    (height1 * SIZE).toFloat()
                )
            }
        }
        if (Key.S.isDown || Key.S.isReleased) println(batch!!.size())
        if (isTouched) {
            val color = if (mouse()!!.isLeftPressed) 1 else 0
            val x = x / SIZE
            val y = y / SIZE
            if (map[x][y] != color) {
                map[x][y] = color
                buildMesh()
            }
        }
        batch!!.end()
    }

    override fun dispose() {
        voxelTexture!!.dispose()
        batch!!.dispose()
    }

    @JvmRecord
    data class Quad(val x: Int, val y: Int, val width: Int, val height: Int, val color: Int)
}
