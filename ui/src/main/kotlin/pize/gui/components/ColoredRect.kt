package pize.gui.components

import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.gui.UIComponent

class ColoredRect(color: IColor?) : UIComponent<TextureBatch>() {
    val color: Color

    init {
        this.color = Color(color!!)
    }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        batch.drawQuad(color, x, y, width, height)
    }
}
