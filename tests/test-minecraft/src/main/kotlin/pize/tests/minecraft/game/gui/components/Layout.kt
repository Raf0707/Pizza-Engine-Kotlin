package pize.tests.minecraft.game.gui.components

import pize.Pize
import pize.graphics.util.batch.TextureBatch
import pize.gui.constraint.Constraint.Companion.matchParent

class Layout : MComponent() {
    init {
        super.setSize(matchParent(), matchParent())
    }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        // if(isHoverIgnoreChildren())
        //     batch.drawQuad(0.85F, 1F, 0.9F, 0.3F, x, y, width, height);
    }

    val isHoverIgnoreChildren: Boolean
        get() {
            val mouseX = Pize.x.toFloat()
            val mouseY = Pize.y.toFloat()
            return !(mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height)
        }
}
