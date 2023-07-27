package pize.gui.constraint

import pize.Pize
import pize.gui.constraint.RelativeConstraint.RelativeTo
import java.util.function.DoubleSupplier

abstract class Constraint {
    protected var supplier: DoubleSupplier? = null
    val value: Float
        get() = supplier!!.asDouble.toFloat()

    open fun setValue(valueSupplier: DoubleSupplier): Constraint {
        supplier = valueSupplier
        return this
    }

    open fun setValue(value: Double): Constraint {
        supplier = DoubleSupplier { value }
        return this
    }

    abstract val type: ConstraintType

    companion object {
        @JvmStatic
        fun zero(): PixelConstraint? {
            return pixel(0.0)
        }

        fun scrWidth(): PixelConstraint? {
            return pixel(Pize::width)
        }

        fun scrHeight(): PixelConstraint? {
            return pixel(Pize::height)
        }

        @JvmStatic
        fun matchParent(): RelativeConstraint? {
            return relative(1.0)
        }

        @JvmStatic
        fun pixel(pixels: Double): PixelConstraint? {
            return PixelConstraint()
                .setValue(pixels)
        }

        fun pixel(pixelsSupplier: DoubleSupplier): PixelConstraint? {
            return PixelConstraint()
                .setValue(pixelsSupplier)
        }

        @JvmStatic
        fun aspect(aspect: Double): AspectConstraint? {
            return AspectConstraint()
                .setValue(aspect)
        }

        fun aspect(aspectSupplier: DoubleSupplier): AspectConstraint? {
            return AspectConstraint()
                .setValue(aspectSupplier)
        }

        @JvmStatic
        fun relative(percentage: Double): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentage)
                .setRelativeTo(RelativeTo.AUTO)
        }

        fun relativeToWidth(percentage: Double): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentage)
                .setRelativeTo(RelativeTo.WIDTH)
        }

        @JvmStatic
        fun relativeToHeight(percentage: Double): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentage)
                .setRelativeTo(RelativeTo.HEIGHT)
        }

        fun relative(percentageSupplier: DoubleSupplier): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentageSupplier)
                .setRelativeTo(RelativeTo.AUTO)
        }

        fun relativeToWidth(percentageSupplier: DoubleSupplier): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentageSupplier)
                .setRelativeTo(RelativeTo.WIDTH)
        }

        fun relativeToHeight(percentageSupplier: DoubleSupplier): RelativeConstraint? {
            return RelativeConstraint()
                .setValue(percentageSupplier)
                .setRelativeTo(RelativeTo.HEIGHT)
        }
    }
}
