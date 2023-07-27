package pize.tests.minecraft.game.gui.components

import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent
import pize.gui.constraint.Constraint.Companion.matchParent

class BaseLayout : UIComponent<TextureBatch?>() {
    init {
        super.setSize(matchParent(), matchParent())
    }

    protected override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        // if(isHover()){
        //     batch.drawQuad(0.1, x, y, width, height);
        //     batch.resetColor();
        // }
    }
}