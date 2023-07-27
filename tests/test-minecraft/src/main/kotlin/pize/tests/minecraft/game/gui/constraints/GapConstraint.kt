package pize.tests.minecraft.game.gui.constraints

import pize.gui.constraint.Constraint.setValue
import pize.gui.constraint.PixelConstraint
import pize.gui.constraint.PixelConstraint.setValue
import pize.tests.minecraft.game.gui.constraints.GapConstraint
import java.util.function.DoubleSupplier

object GapConstraint : PixelConstraint() {
    fun gap(pixels: Double): GapConstraint {
        return GapConstraint().setValue(pixels)
    }

    fun gap(pixelsSupplier: DoubleSupplier?): GapConstraint {
        return GapConstraint().setValue(pixelsSupplier)
    }
}
