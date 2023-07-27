package pize.gui.constraint

import java.util.function.DoubleSupplier

class RelativeConstraint : Constraint() {
    enum class RelativeTo {
        AUTO,
        WIDTH,
        HEIGHT
    }

    var relativeTo: RelativeTo
        private set

    init {
        relativeTo = RelativeTo.AUTO
    }

    override fun setValue(valueSupplier: DoubleSupplier): RelativeConstraint {
        supplier = valueSupplier
        return this
    }

    override fun setValue(value: Double): RelativeConstraint {
        supplier = DoubleSupplier { value }
        return this
    }

    fun setRelativeTo(relativeTo: RelativeTo): RelativeConstraint {
        this.relativeTo = relativeTo
        return this
    }

    override val type: ConstraintType
        get() = ConstraintType.RELATIVE
}
