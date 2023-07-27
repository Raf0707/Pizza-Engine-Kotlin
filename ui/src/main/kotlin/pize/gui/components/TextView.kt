package pize.gui.components

import pize.graphics.font.BitmapFont
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.gui.UIComponent
import pize.gui.constraint.*
import pize.math.Maths.round

open class TextView(var text: String, var font: BitmapFont?) : UIComponent<TextureBatch>() {
    val color: Color
    val backgroundColor: Color
    private var shadow = false
    val shadowColor: Color
    private var shadowOffsetX = 0f
    private var shadowOffsetY = 0f

    init {
        color = Color()
        backgroundColor = Color(0, 0, 0, 0)
        shadowColor = Color(0, 0, 0, 0)
    }

    public override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        if (font == null) return

        // Calculate a True Text Gravity
        val bounds = font!!.getBounds(text)
        val renderX = x - bounds!!.x // * (gravityOffsetX / parentWidth );
        val renderY = y - bounds.y // * (gravityOffsetY / parentHeight);

        // Render
        if (backgroundColor.a() != 0f) batch.drawQuad(backgroundColor, renderX, renderY, bounds.x, bounds.y)
        if (shadow) {
            batch.setColor(shadowColor)
            font!!.drawText(
                batch, text,
                renderX + round(shadowOffsetX * font.getScale()),
                renderY + round(shadowOffsetY * font.getScale())
            )
        }
        batch.setColor(color)
        font!!.drawText(batch, text, renderX, renderY)
        batch.resetColor()
    }

    override fun setWidth(constraintWidth: Constraint?) {}
    override fun setHeight(width: Constraint?) {}
    fun setShadow(x: Float, y: Float, shadowColor: IColor?) {
        shadow = true
        shadowOffsetX = x
        shadowOffsetY = y
        this.shadowColor.set(shadowColor!!)
    }

    fun disableShadow() {
        shadow = false
    }
}
