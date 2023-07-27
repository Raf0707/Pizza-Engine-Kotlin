package pize.gui.components

import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent
import pize.gui.constraint.*

class Layout : UIComponent<TextureBatch?>() {
    init {
        super.setSize(Constraint.Companion.matchParent(), Constraint.Companion.matchParent())
    }

    override fun render(batch: TextureBatch?, x: Float, y: Float, width: Float, height: Float) {}
}
