package pize.graphics.texture

import org.lwjgl.BufferUtils
import pize.app.Resizable
import pize.graphics.gl.SizedFormat
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.math.Maths
import pize.math.vecmath.vector.Vec2d
import java.nio.*
import kotlin.math.*

open class Pixmap : Sizable, Resizable {
    // GET BUFFER
    var buffer: ByteBuffer
        private set

    // CONSTRUCTOR
    constructor(buffer: ByteBuffer, width: Int, height: Int) : super(width, height) {
        this.buffer = buffer.duplicate()
    }

    constructor(pixmap: Pixmap) : this(pixmap.buffer, pixmap.width, pixmap.height)
    constructor(width: Int, height: Int) : super(width, height) {
        buffer = BufferUtils.createByteBuffer(width * height * 4)
    }

    // SET
    fun set(pixmap: Pixmap?) {
        if (pixmap == null) return
        if (!pixmap.match(this)) {
            setSize(pixmap)
            buffer.clear()
            // Utils.free(buffer);
            buffer = BufferUtils.createByteBuffer(width * height * 4)
        }
        for (i in 0 until buffer.limit()) buffer.put(i, pixmap.buffer[i])
    }

    // SET PIXEL
    fun setPixel(x: Int, y: Int, color: Int) {
        if (outOfBounds(x, y)) return
        val blendColor = blend(
            getPixelColor(x, y),
            Color(color shr 24 and 0xFF, color shr 16 and 0xFF, color shr 8 and 0xFF, color and 0xFF)
        )
        buffer.put(getIndex(x, y, 0), (color shr 24 and 0xFF).toByte())
        buffer.put(getIndex(x, y, 1), (color shr 16 and 0xFF).toByte())
        buffer.put(getIndex(x, y, 2), (color shr 8 and 0xFF).toByte())
        buffer.put(getIndex(x, y, 3), (color and 0xFF).toByte())
    }

    fun setPixel(x: Int, y: Int, color: IColor) {
        if (outOfBounds(x, y)) return
        val blendColor = blend(getPixelColor(x, y), color)
        buffer.put(getIndex(x, y, 0), ((blendColor.r() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 1), ((blendColor.g() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 2), ((blendColor.b() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 3), ((blendColor.a() * 255).toInt() and 0xFF).toByte())
    }

    fun setPixel(x: Int, y: Int, r: Double, g: Double, b: Double, a: Double) {
        if (outOfBounds(x, y)) return
        val blendColor = blend(getPixelColor(x, y), Color(r, g, b, a))
        buffer.put(getIndex(x, y, 0), ((blendColor.r() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 1), ((blendColor.g() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 2), ((blendColor.b() * 255).toInt() and 0xFF).toByte())
        buffer.put(getIndex(x, y, 3), ((blendColor.a() * 255).toInt() and 0xFF).toByte())
    }

    fun setPixel(x: Int, y: Int, r: Int, g: Int, b: Int, a: Int) {
        if (outOfBounds(x, y)) return
        val blendColor = blend(getPixelColor(x, y), Color(r, g, b, a))
        buffer.put(getIndex(x, y, 0), (r and 0xFF).toByte())
        buffer.put(getIndex(x, y, 1), (g and 0xFF).toByte())
        buffer.put(getIndex(x, y, 2), (b and 0xFF).toByte())
        buffer.put(getIndex(x, y, 3), (a and 0xFF).toByte())
    }

    // BLENDING
    private fun blend(color1: Color, color2: IColor): Color {
        color1.set(
            color1.r() * (1 - color2.a()) + color2.r() * color2.a(),
            color1.g() * (1 - color2.a()) + color2.g() * color2.a(),
            color1.b() * (1 - color2.a()) + color2.b() * color2.a(), max(color1.a().toDouble(), color2.a().toDouble())
        )
        return color1
    }

    private fun blend(color1: Color, r2: Double, g2: Double, b2: Double, a2: Double): Color {
        color1[color1.r() * (1 - a2) + r2 * a2, color1.g() * (1 - a2) + g2 * a2, color1.b() * (1 - a2) + b2 * a2] =
            max(color1.a().toDouble(), a2)
        return color1
    }

    private fun blend(
        r1: Double,
        g1: Double,
        b1: Double,
        a1: Double,
        r2: Double,
        g2: Double,
        b2: Double,
        a2: Double
    ): IColor {
        return Color(
            r1 * (1 - a2) + r2 * a2,
            g1 * (1 - a2) + g2 * a2,
            b1 * (1 - a2) + b2 * a2, max(a1, a2)
        )
    }

    // GET PIXEL
    fun getPixelABGR(x: Int, y: Int): Int {
        val r = buffer[getIndex(x, y, 0)].toInt() and 0xFF
        val g = buffer[getIndex(x, y, 1)].toInt() and 0xFF
        val b = buffer[getIndex(x, y, 2)].toInt() and 0xFF
        val a = buffer[getIndex(x, y, 3)].toInt() and 0xFF
        return r shl 24 or (g shl 16) or (b shl 8) or a
    }

    fun getPixelBGRA(x: Int, y: Int): Int {
        val r = buffer[getIndex(x, y, 0)].toInt() and 0xFF
        val g = buffer[getIndex(x, y, 1)].toInt() and 0xFF
        val b = buffer[getIndex(x, y, 2)].toInt() and 0xFF
        val a = buffer[getIndex(x, y, 3)].toInt() and 0xFF
        return a shl 24 or (r shl 16) or (g shl 8) or b
    }

    fun getPixelRGBA(x: Int, y: Int): Int {
        val r = buffer[getIndex(x, y, 0)].toInt() and 0xFF
        val g = buffer[getIndex(x, y, 1)].toInt() and 0xFF
        val b = buffer[getIndex(x, y, 2)].toInt() and 0xFF
        val a = buffer[getIndex(x, y, 3)].toInt() and 0xFF
        return a shl 24 or (b shl 16) or (g shl 8) or r
    }

    fun getPixelColor(x: Int, y: Int): Color {
        return Color(
            (buffer[getIndex(x, y, 0)].toInt() and 0xFF) / 255f,
            (buffer[getIndex(x, y, 1)].toInt() and 0xFF) / 255f,
            (buffer[getIndex(x, y, 2)].toInt() and 0xFF) / 255f,
            (buffer[getIndex(x, y, 3)].toInt() and 0xFF) / 255f
        )
    }

    fun getPixelColor(x: Int, y: Int, color: Color) {
        color[(buffer[getIndex(x, y, 0)].toInt() and 0xFF) / 255f, (buffer[getIndex(
            x,
            y,
            1
        )].toInt() and 0xFF) / 255f, (buffer[getIndex(x, y, 2)].toInt() and 0xFF) / 255f] =
            (buffer[getIndex(x, y, 3)].toInt() and 0xFF) / 255f
    }

    // SAMPLE PIXEL
    fun samplePixel(x: Double, y: Double): Int {
        return getPixelABGR((x * width).toInt(), (y * height).toInt())
    }

    fun samplePixelColor(x: Double, y: Double): IColor {
        return getPixelColor((x * width).toInt(), (y * height).toInt())
    }

    // FILL
    fun fill(beginX: Int, beginY: Int, endX: Int, endY: Int, color: Int) {
        val iEnd = min((endX + 1).toDouble(), width.toDouble()).toFloat()
        val jEnd = min((endY + 1).toDouble(), height.toDouble()).toFloat()
        var i = max(0.0, beginX.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, beginY.toDouble()).toInt()
            while (j < jEnd) {
                setPixel(i, j, color)
                j++
            }
            i++
        }
    }

    fun fill(beginX: Int, beginY: Int, endX: Int, endY: Int, color: IColor) {
        val iEnd = min((endX + 1).toDouble(), width.toDouble()).toFloat()
        val jEnd = min((endY + 1).toDouble(), height.toDouble()).toFloat()
        var i = max(0.0, beginX.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, beginY.toDouble()).toInt()
            while (j < jEnd) {
                setPixel(i, j, color)
                j++
            }
            i++
        }
    }

    fun fill(beginX: Int, beginY: Int, endX: Int, endY: Int, r: Double, g: Double, b: Double, a: Double) {
        val iEnd = min((endX + 1).toDouble(), width.toDouble()).toFloat()
        val jEnd = min((endY + 1).toDouble(), height.toDouble()).toFloat()
        var i = max(0.0, beginX.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, beginY.toDouble()).toInt()
            while (j < jEnd) {
                setPixel(i, j, r, g, b, a)
                j++
            }
            i++
        }
    }

    fun fill(beginX: Int, beginY: Int, endX: Int, endY: Int, r: Int, g: Int, b: Int, a: Int) {
        val iEnd = min((endX + 1).toDouble(), width.toDouble()).toFloat()
        val jEnd = min((endY + 1).toDouble(), height.toDouble()).toFloat()
        var i = max(0.0, beginX.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, beginY.toDouble()).toInt()
            while (j < jEnd) {
                setPixel(i, j, r, g, b, a)
                j++
            }
            i++
        }
    }

    // DRAW LINE
    fun drawLine(beginX: Int, beginY: Int, endX: Int, endY: Int, r: Double, g: Double, b: Double, a: Double) {
        val vec = Vec2d((endX - beginX).toFloat(), (endY - beginY).toFloat())
        val angle = atan2(vec.y, vec.x)
        val offsetX = cos(angle)
        val offsetY = sin(angle)
        var x = beginX.toFloat()
        var y = beginY.toFloat()
        var length = 0f
        while (length < vec.len()) {
            setPixel(Maths.round(x.toDouble()), Maths.round(y.toDouble()), r, g, b, a)
            x += offsetX.toFloat()
            y += offsetY.toFloat()
            length++
        }
    }

    // DRAW DOTTED LINE
    fun drawDottedLine(
        beginX: Int,
        beginY: Int,
        endX: Int,
        endY: Int,
        lineLength: Double,
        r: Double,
        g: Double,
        b: Double,
        a: Double
    ) {
        val vec = Vec2d((endX - beginX).toFloat(), (endY - beginY).toFloat())
        val angle = atan2(vec.y, vec.x)
        val offsetX = cos(angle)
        val offsetY = sin(angle)
        var x = beginX.toFloat()
        var y = beginY.toFloat()
        var length = 0f
        while (length < vec.len()) {
            if (sin(length / lineLength) >= 0) setPixel(
                Maths.round(x.toDouble()),
                Maths.round(y.toDouble()),
                r,
                g,
                b,
                a
            )
            x += offsetX.toFloat()
            y += offsetY.toFloat()
            length++
        }
    }

    // DRAW PIXMAP
    fun drawPixmap(pixmap: Pixmap?) {
        if (pixmap == null) return
        val iEnd = min(pixmap.width.toDouble(), width.toDouble()).toFloat()
        val jEnd = min(pixmap.height.toDouble(), height.toDouble()).toFloat()
        var i = 0
        while (i < iEnd) {
            var j = 0
            while (j < jEnd) {
                setPixel(i, j, pixmap.getPixelColor(i, j))
                j++
            }
            i++
        }
    }

    fun drawPixmap(pixmap: Pixmap?, x: Int, y: Int) {
        if (pixmap == null) return
        val iEnd = if ((x + pixmap.width).also { iEnd = it } > width) width.toFloat() else iEnd
        val jEnd = if ((y + pixmap.height).also { jEnd = it } > height) height.toFloat() else jEnd
        var i = max(0.0, x.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, y.toDouble()).toInt()
            while (j < jEnd) {
                val px = Maths.round((i - x).toDouble())
                val py = Maths.round((j - y).toDouble())
                setPixel(i, j, pixmap.getPixelColor(px, py))
                j++
            }
            i++
        }
    }

    fun drawPixmap(pixmap: Pixmap?, scaleX: Double, scaleY: Double) {
        if (pixmap == null || scaleX <= 0 || scaleY <= 0) return
        val iEnd = if ((pixmap.width * scaleX).also { iEnd = it } > width) width.toDouble() else iEnd
        val jEnd = if ((pixmap.height * scaleY).also { jEnd = it } > height) height.toDouble() else jEnd
        var i = 0
        while (i < iEnd) {
            var j = 0
            while (j < jEnd) {
                val px = (i / scaleX).toInt()
                val py = (j / scaleY).toInt()
                setPixel(i, j, pixmap.getPixelColor(px, py))
                j++
            }
            i++
        }
    }

    fun drawPixmap(pixmap: Pixmap?, scale: Double) {
        drawPixmap(pixmap, scale, scale)
    }

    fun drawPixmap(pixmap: Pixmap?, x: Int, y: Int, scaleX: Double, scaleY: Double) {
        if (pixmap == null || scaleX <= 0 || scaleY <= 0) return
        val iEnd = if ((x + pixmap.width * scaleX).also { iEnd = it } > width) width.toDouble() else iEnd
        val jEnd = if ((y + pixmap.height * scaleY).also { jEnd = it } > height) height.toDouble() else jEnd
        var i = max(0.0, x.toDouble()).toInt()
        while (i < iEnd) {
            var j = max(0.0, y.toDouble()).toInt()
            while (j < jEnd) {
                val px = ((i - x) / scaleX).toInt()
                val py = ((j - y) / scaleY).toInt()
                setPixel(i, j, pixmap.getPixelColor(px, py))
                j++
            }
            i++
        }
    }

    fun drawPixmap(pixmap: Pixmap?, x: Int, y: Int, scale: Double) {
        drawPixmap(pixmap, x, y, scale, scale)
    }

    // UTILS
    fun colorize(r: Double, g: Double, b: Double) {
        val color = Color()
        for (x in 0 until width) {
            for (y in 0 until height) {
                getPixelColor(x, y, color)
                color[(color.r() * 0.2126 + r) / 2, (color.g() * 0.7152 + g) / 2] = (color.b() * 0.0722 + b) / 2
                setPixel(x, y, color)
            }
        }
    }

    fun clearChannel(channel: Int, value: Double) {
        var i = 0
        while (i < buffer.capacity()) {
            buffer.put(i + channel, (value * 255).toInt().toByte())
            i += 4
        }
    }

    // CLEAR
    fun clear() {
        for (i in 0 until buffer.capacity()) buffer.put(i, 0.toByte())
    }

    fun clear(r: Double, g: Double, b: Double, a: Double) {
        var i = 0
        while (i < buffer.capacity()) {
            buffer.put(i, (r * 255).toInt().toByte())
            buffer.put(i + 1, (g * 255).toInt().toByte())
            buffer.put(i + 2, (b * 255).toInt().toByte())
            buffer.put(i + 3, (a * 255).toInt().toByte())
            i += 4
        }
    }

    fun clear(r: Int, g: Int, b: Int, a: Int) {
        var i = 0
        while (i < buffer.capacity()) {
            buffer.put(i, r.toByte())
            buffer.put(i + 1, g.toByte())
            buffer.put(i + 2, b.toByte())
            buffer.put(i + 3, a.toByte())
            i += 4
        }
    }

    fun clear(color: IColor) {
        var i = 0
        while (i < buffer.capacity()) {
            buffer.put(i, (color.r() * 255).toInt().toByte())
            buffer.put(i + 1, (color.g() * 255).toInt().toByte())
            buffer.put(i + 2, (color.b() * 255).toInt().toByte())
            buffer.put(i + 3, (color.a() * 255).toInt().toByte())
            i += 4
        }
    }

    // RESIZE
    override fun resize(width: Int, height: Int) {
        if (super.match(width, height)) return
        setSize(width, height)
        buffer.clear()
        // Utils.free(buffer);
        buffer = BufferUtils.createByteBuffer(width * height * 4)
    }

    val mipmapped: Pixmap
        // MIPMAPPED
        get() {
            val pixmap = Pixmap(width / 2, height / 2)
            for (x in 0 until pixmap.width) {
                for (y in 0 until pixmap.height) {
                    pixmap.setPixel(x, y, 1.0, 0.0, 0.0, 1.0)
                }
            }
            return pixmap
        }
    val alphaMultipliedBuffer: ByteBuffer
        get() {
            val buffer = buffer.duplicate()
            var i = 0
            while (i < buffer.capacity()) {
                val alpha = (buffer[i + 3].toInt() and 0xFF) / 255f
                buffer.put(i, (((buffer[i].toInt() and 0xFF) * alpha).toInt() and 0xFF).toByte())
                buffer.put(i + 1, (((buffer[i + 1].toInt() and 0xFF) * alpha).toInt() and 0xFF).toByte())
                buffer.put(i + 2, (((buffer[i + 2].toInt() and 0xFF) * alpha).toInt() and 0xFF).toByte())
                i += 4
            }
            return buffer
        }

    // OTHER
    fun getIndex(x: Int, y: Int, channel: Int): Int {
        return (y * width + x) * FORMAT.channels + channel
    }

    fun outOfBounds(x: Int, y: Int): Boolean {
        return x < 0 || y < 0 || x >= width || y >= height
    }

    fun copy(): Pixmap {
        return Pixmap(this)
    }

    companion object {
        val FORMAT = SizedFormat.RGBA8
    }
}
