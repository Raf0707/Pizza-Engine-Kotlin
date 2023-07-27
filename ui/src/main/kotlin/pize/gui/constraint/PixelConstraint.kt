package pize.gui.constraint

import java.util.function.DoubleSupplier

open class PixelConstraint : Constraint() {
    override fun setValue(valueSupplier: DoubleSupplier): PixelConstraint {
        supplier = valueSupplier
        return this
    }

    override fun setValue(value: Double): PixelConstraint {
        supplier = DoubleSupplier { value }
        return this
    }

    override val type: ConstraintType
        get() = ConstraintType.PIXEL
}
