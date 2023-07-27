package pize.tests.minecraft.game.gui.components

import pize.Pize
import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent
import pize.gui.constraint.Constraint
import pize.gui.constraint.Constraint.Companion.pixel
import pize.gui.constraint.PixelConstraint
import pize.math.Maths.round
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import kotlin.math.max

abstract class MComponent : UIComponent<TextureBatch?>() {
    private var widthC: Constraint? = null
    private var heightC: Constraint? = null
    private var xC: Constraint? = null
    private var yC: Constraint? = null
    private var textSizeC: Constraint? = null
    private var scale = 0
    private var needInit = true
    private fun init() {
        if (this is TextView) textSizeC = component.getSizeConstraint() else {
            widthC = widthConstraint
            heightC = heightConstraint
        }
        xC = xConstraint
        yC = yConstraint
    }

    override fun correctConstraints() {
        if (needInit) {
            init()
            needInit = false
        }
        scale = max(
            1.0,
            round((Pize.height.toFloat() * INTERFACE_SCALE / 20 / 50).toDouble()).toDouble()
        ).toInt()
        if (textSizeC != null) {
            if (textSizeC is PixelConstraint) setSize(pixel((textSizeC.value * scale).toDouble()))
        } else {
            if (widthC is PixelConstraint) setWidth(pixel((widthC.value * scale).toDouble()))
            if (heightC is PixelConstraint) setHeight(pixel((heightC.value * scale).toDouble()))
        }
        if (xC is GapConstraint) setX(GapConstraint.gap((xC.value * scale).toDouble()))
        if (yC is GapConstraint) setY(GapConstraint.gap((yC.value * scale).toDouble()))
    }

    override fun correctPos() {
        x = round(x.toDouble()).toFloat()
        y = round(y.toDouble()).toFloat()
    }

    override fun correctSize() {
        width = (round((width / scale).toDouble()) * scale).toFloat()
        height = (round((height / scale).toDouble()) * scale).toFloat()
    }

    companion object {
        const val INTERFACE_SCALE = 3
    }

    abstract fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float)
}
