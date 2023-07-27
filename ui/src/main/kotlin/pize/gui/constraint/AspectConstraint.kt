package pize.gui.constraint

import java.util.function.DoubleSupplier

class AspectConstraint : Constraint() {
    override fun setValue(valueSupplier: DoubleSupplier): AspectConstraint {
        supplier = valueSupplier
        return this
    }

    override fun setValue(value: Double): AspectConstraint {
        supplier = DoubleSupplier { value }
        return this
    }

    override val type: ConstraintType
        get() = ConstraintType.ASPECT
}
