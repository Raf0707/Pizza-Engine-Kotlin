package pize.graphics.font

import pize.app.Disposable
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.vector.Vec2f
import pize.util.StringUtils
import kotlin.math.atan
import kotlin.math.max

class BitmapFont : Disposable {
    private val glyphs: MutableMap<Int, Glyph> = HashMap()
    private val pages: MutableMap<Int, Texture> = HashMap()
    var lineHeight = 0
    @JvmField
    var scale = 0f
    @JvmField
    var rotation = 0f
    @JvmField
    var lineGaps = 0f
    var isItalic = false

    init {
        scale = 1f
    }

    fun getGlyph(code: Int): Glyph? {
        return glyphs[code]
    }

    fun addGlyph(glyph: Glyph) {
        glyphs[glyph.id] = glyph
    }

    fun getPage(id: Int): Texture? {
        return pages[id]
    }

    fun addPage(id: Int, page: Texture) {
        pages[id] = page
    }

    fun getLineHeight(): Float {
        return lineHeight.toFloat()
    }

    val lineHeightScaled: Float
        get() = lineHeight * scale
    val lineAdvance: Float
        get() = lineHeight + lineGaps
    val lineAdvanceScaled: Float
        get() = lineAdvance * scale

    fun setLineHeight(lineHeight: Int) {
        this.lineHeight = lineHeight
    }

    fun getBounds(text: String): Vec2f? {
        return this.getBounds(text, -1.0)
    }

    fun getBounds(text: String, width: Double): Vec2f? {
        val lineAdvance = lineAdvance
        var maxAdvanceX = 0f
        var advanceX = 0f
        var advanceY = lineAdvance
        for (i in 0 until text.length) {
            val code = Character.codePointAt(text, i)
            val glyph = glyphs[code] ?: continue
            if (code == 10) {
                maxAdvanceX = max(maxAdvanceX.toDouble(), advanceX.toDouble()).toFloat()
                advanceX = 0f
                advanceY += lineAdvance
                continue
            }
            if (width > 0 && (advanceX + glyph.advanceX) * scale > width) {
                maxAdvanceX = max(maxAdvanceX.toDouble(), advanceX.toDouble()).toFloat()
                advanceX = 0f
                advanceY += lineAdvance
            }
            advanceX += glyph.advanceX
        }
        return Vec2f(max(advanceX.toDouble(), maxAdvanceX.toDouble()), advanceY.toDouble()).mul(scale)
    }

    fun getTextHeight(text: String): Vec2f? {
        return this.getBounds(text, -1.0)
    }

    fun getTextHeight(text: String, width: Double): Float {
        val lineAdvance = lineAdvance
        var advanceX = 0f
        var advanceY = lineAdvance
        for (i in 0 until text.length) {
            val code = Character.codePointAt(text, i)
            val glyph = glyphs[code] ?: continue
            if (code == 10) {
                advanceX = 0f
                advanceY += lineAdvance
                continue
            }
            if (width > 0 && (advanceX + glyph.advanceX) * scale > width) {
                advanceX = 0f
                advanceY += lineAdvance
            }
            advanceX += glyph.advanceX
        }
        return advanceY * scale
    }

    fun getLineWidth(line: String): Float {
        var advanceX = 0f
        for (i in 0 until line.length) {
            val code = Character.codePointAt(line, i)
            if (code == 10) continue
            val glyph = glyphs[code] ?: continue
            advanceX += glyph.advanceX
        }
        return advanceX * scale
    }

    @JvmOverloads
    fun drawText(batch: TextureBatch, text: String?, x: Float, y: Float, width: Double = -1.0) {
        if (text == null) return
        val lineAdvance = lineAdvance
        var advanceX = 0f
        var advanceY = (lineHeight * StringUtils.count(text, "\n")).toFloat()
        batch.setTransformOrigin(0.0, 0.0)
        batch.rotate(rotation)
        batch.shear((if (isItalic) ITALIC_ANGLE else 0) as Float, 0f)

        // Calculate centering offset
        val bounds = getBounds(text, width)
        val angle = rotation * Maths.ToRad + atan((bounds!!.y / bounds.x).toDouble())
        val boundsCenter = Mathc.hypot((bounds.x / 2).toDouble(), (bounds.y / 2).toDouble())
        val centeringOffsetX = boundsCenter * Mathc.cos(angle) - bounds.x / 2
        val centeringOffsetY = boundsCenter * Mathc.sin(angle) - bounds.y / 2

        // Rotation
        val cos = Mathc.cos((rotation * Maths.ToRad).toDouble())
        val sin = Mathc.sin((rotation * Maths.ToRad).toDouble())
        for (i in 0 until text.length) {
            val code = Character.codePointAt(text, i)
            if (code == 10) {
                advanceY -= lineAdvance
                advanceX = 0f
                continue
            }
            val glyph = glyphs[code] ?: continue
            if (width > 0 && (advanceX + glyph.advanceX) * scale > width) {
                advanceY -= lineAdvance
                advanceX = 0f
            }
            val xOffset = (advanceX + glyph.offsetX) * scale
            val yOffset = (advanceY + glyph.offsetY) * scale
            val renderX = x + xOffset * cos - yOffset * sin - centeringOffsetX
            val renderY = y + yOffset * cos + xOffset * sin - centeringOffsetY
            glyph.render(batch, renderX, renderY)
            advanceX += glyph.advanceX
        }
        batch.rotate(0f)
        batch.shear(0f, 0f)
    }

    override fun dispose() {
        for (page in pages.values) page.dispose()
    }

    companion object {
        const val ITALIC_ANGLE = 15f
    }
}