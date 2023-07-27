package pize.tests.minecraft.game.gui.components

import pize.Pize
import pize.Pize.keyboard
import pize.Pize.mouse
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths.clamp

class ListView : MComponent() {
    private var scrollX = 0f
    private var scrollY = 0f
    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        batch.scissor.begin(228, x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
        scrollX = clamp(scrollX + mouse()!!.getScrollX(keyboard()!!) * 50, -1000f, 1000f)
        scrollY = clamp(scrollY + mouse()!!.getScrollY(keyboard()!!) * 50, -200f, 0f)
        setChildShift(scrollX, -scrollY)
    }

    override fun renderEnd(batch: TextureBatch) {
        batch.scissor.end(228)
    }

    val isHoverIgnoreChildren: Boolean
        get() {
            val mouseX = Pize.x.toFloat()
            val mouseY = Pize.y.toFloat()
            return !(mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height)
        }
}
