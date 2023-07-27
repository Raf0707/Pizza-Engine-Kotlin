package pize.tests.minecraft.game.gui.components

import pize.Pize.dt
import pize.graphics.font.BitmapFont
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.gui.constraint.Constraint
import pize.gui.constraint.Constraint.Companion.pixel
import pize.math.Mathc.cos
import pize.math.Mathc.hypot
import pize.math.Mathc.sin
import pize.math.Maths
import pize.math.vecmath.vector.Vec2f
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.text.Component
import pize.tests.minecraft.game.gui.text.TextComponent
import kotlin.math.atan
import kotlin.math.max

class TextView(private val session: Session, var text: Component?) : MComponent() {
    var font: BitmapFont?
    var sizeConstraint: Constraint?
        private set
    var rotation = 0f
    private var blocked = false
    private var disableShadow = false
    var isScissor = false
    private var components: List<TextComponent?>? = null
    private val textList: MutableList<String?>
    private var bounds: Vec2f? = null
    private var scrollDir = false
    private var scrollShiftX = 0f

    init {
        font = session.resourceManager.getFont("font_minecraft")
        sizeConstraint = pixel(8.0)
        textList = ArrayList()
    }

    public override fun correctSize() {
        if (font == null) return
        font!!.scale = super.calcConstraintY(sizeConstraint) / font!!.getLineHeight()
        components = text!!.getAllComponents(session)
        bounds = getBounds(components)
        width = bounds!!.x
        height = bounds!!.y
    }

    public override fun correctPos() {
        // Scroll
        val parent = getParent()
        val widthDifference: Float
        if (isScissor && parent != null && (width - parent.width).also { widthDifference = it } > 0) {
            val xDifference = this.x - parent.x
            val relativeX = xDifference + scrollShiftX
            val increment = dt * parent.width * widthDifference / 5000
            val scissorOffset = Math.round(parent.height / 20) * 2
            if (scrollDir) {
                if (relativeX + scissorOffset <= -widthDifference) {
                    scrollShiftX = -widthDifference - xDifference - scissorOffset
                    scrollDir = false
                } else scrollShiftX -= increment
            } else {
                if (relativeX - scissorOffset >= 0) {
                    scrollShiftX = 0 - xDifference + scissorOffset
                    scrollDir = true
                } else scrollShiftX += increment
            }
        } else scrollShiftX = 0f
        super.correctPos()
    }

    public override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        if (font == null || components!!.size == 0) return

        // Rotate glyphs on batch
        batch.setTransformOrigin(0.0, 0.0)
        batch.rotate(rotation)

        // Init
        val lineHeight = font!!.getLineHeight()
        val scale = font!!.scale
        var advanceX = 0f
        var advanceY = lineHeight * (text!!.getAllText(session).split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().size - 1)

        // Calculate centering offset
        val angle = rotation * Maths.ToRad + atan((bounds!!.y / bounds!!.x).toDouble())
        val boundsCenter = hypot((bounds!!.x / 2).toDouble(), (bounds!!.y / 2).toDouble())
        val centeringOffsetX = boundsCenter * cos(angle) - bounds!!.x / 2
        val centeringOffsetY = boundsCenter * sin(angle) - bounds!!.y / 2

        // Rotation
        val cos = cos((rotation * Maths.ToRad).toDouble())
        val sin = sin((rotation * Maths.ToRad).toDouble())

        // Calc shadow offset
        val shadowOffsetX = (cos + sin) * scale * SHADOW_OFFSET
        val shadowOffsetY = (sin - cos) * scale * SHADOW_OFFSET

        // Begin scissor
        val parent = getParent()
        if (isScissor && parent != null) {
            val offset = parent.height / 20 * 2
            batch.scissor.begin(
                54,
                (
                        parent.x + offset).toDouble(), (parent.y + offset).toDouble(),
                (
                        parent.width - offset * 2).toDouble(), (parent.height - offset * 2).toDouble(),
                228
            )
        }
        // Text cycle
        var textIndex = 0
        for (textComponent in components!!) {
            val text = textList[textIndex++]
            val style = textComponent.getStyle()
            val italic = style!!.italic
            val bold = style!!.bold
            val underline = style!!.underline
            val strikethrough = style!!.strikethrough
            val lineBeginX = advanceX * scale
            val lineBeginY = advanceY * scale
            var lineWidth = 0f
            val color = Color(style!!.color)
            if (blocked) color.mul(0.6, 0.6, 0.6, 1.0)
            for (i in 0 until text!!.length) {
                // Getting glyph
                val code = Character.codePointAt(text, i)
                if (code == 10) {
                    advanceY -= lineHeight
                    advanceX = 0f
                    continue
                }
                val glyph = font!!.getGlyph(code) ?: continue

                // Calculate glyph render position
                val xOffset = (advanceX + glyph.offsetX) * scale + scrollShiftX
                val yOffset = (advanceY + glyph.offsetY) * scale
                val renderX = x + xOffset * cos - yOffset * sin - centeringOffsetX
                val renderY = y + yOffset * cos + xOffset * sin - centeringOffsetY

                // Render shadow
                batch.shear(if (italic) BitmapFont.ITALIC_ANGLE else 0, 0f)
                batch.setColor(color)
                if (!disableShadow && !blocked) {
                    batch.setColor(color.r() * 0.25, color.g() * 0.25, color.b() * 0.25, color.a().toDouble())
                    glyph.render(batch, renderX + shadowOffsetX, renderY + shadowOffsetY)
                    if (bold) glyph.render(
                        batch,
                        renderX + cos * scale * BOLD_OFFSET + shadowOffsetX,
                        renderY + sin * scale * BOLD_OFFSET + shadowOffsetY
                    )
                }

                // Render glyph
                batch.setColor(color)
                glyph.render(batch, renderX, renderY)
                if (bold) glyph.render(batch, renderX + cos * scale, renderY + sin * scale)

                // AdvanceX increase num
                val advanceXIncrease: Float = glyph.advanceX + if (bold) BOLD_OFFSET else 0

                // Strikethrough & Underline
                if (strikethrough && i == text.length - 1) {
                    batch.drawQuad(
                        1.0, 1.0, 1.0, 1.0,
                        x - centeringOffsetX + lineBeginX,
                        y - centeringOffsetY + lineBeginY + font!!.getLineHeight() * 3 / 8f * scale,
                        (lineWidth + advanceXIncrease) * scale,
                        scale
                    )
                }
                if (underline && i == text.length - 1) {
                    batch.drawQuad(
                        1.0, 1.0, 1.0, 1.0,
                        x - centeringOffsetX + lineBeginX,
                        y - centeringOffsetY + lineBeginY - font!!.getLineHeight() / 8f * scale,
                        (lineWidth + advanceXIncrease) * scale,
                        scale
                    )
                }
                lineWidth += advanceXIncrease

                // AdvanceX
                advanceX += advanceXIncrease
            }
        }

        // End scissor
        if (isScissor && parent != null) batch.scissor.end(54)

        // End
        font!!.rotation = 0f
        batch.rotate(0f)
        batch.shear(0f, 0f)
        batch.scale(1f)
        batch.resetColor()
    }

    private fun getBounds(components: List<TextComponent?>?): Vec2f {
        val lineHeight = font!!.getLineHeight()
        val scale = font!!.scale
        var advanceX = 0
        var advanceY = lineHeight
        var maxX = 0
        textList.clear()
        for (component in components!!) {
            val text = component!!.getText()
            textList.add(text)
            val style = component.style
            for (i in 0 until text!!.length) {
                val code = Character.codePointAt(text, i)
                if (code == 10) {
                    advanceY += lineHeight
                    advanceX = 0
                    continue
                }
                val glyph = font!!.getGlyph(code) ?: continue
                advanceX = (advanceX + (glyph.advanceX + if (style!!.bold) BOLD_OFFSET else 0)).toInt()
                maxX = max(maxX.toDouble(), advanceX.toDouble()).toInt()
            }
        }
        return Vec2f(maxX.toFloat(), advanceY).mul(scale)
    }

    fun block(blocked: Boolean) {
        this.blocked = blocked
    }

    override fun setSize(lineHeight: Constraint?) {
        sizeConstraint = lineHeight
    }

    fun disableShadow(disableShadow: Boolean) {
        this.disableShadow = disableShadow
    }

    companion object {
        const val SHADOW_OFFSET = 1f
        const val BOLD_OFFSET = 1f
    }
}
