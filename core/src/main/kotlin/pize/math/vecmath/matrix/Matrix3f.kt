package pize.math.vecmath.matrix

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.vector.Vec2f
import java.util.*

class Matrix3f : Matrix3 {
    val `val`: FloatArray

    constructor() {
        `val` = FloatArray(16)
        `val`[Matrix3.Companion.m00.toInt()] = 1f
        `val`[Matrix3.Companion.m11.toInt()] = 1f
        `val`[Matrix3.Companion.m22.toInt()] = 1f
    }

    constructor(values: FloatArray?) {
        `val` = FloatArray(16)
        set(values)
    }

    constructor(matrix: Matrix3f) : this(matrix.`val`)

    fun zero(): Matrix3f {
        Arrays.fill(`val`, 0f)
        return this
    }

    fun identity(): Matrix3f {
        `val`[Matrix3.Companion.m00.toInt()] = 1f
        `val`[Matrix3.Companion.m10.toInt()] = 0f
        `val`[Matrix3.Companion.m20.toInt()] = 0f
        `val`[Matrix3.Companion.m01.toInt()] = 0f
        `val`[Matrix3.Companion.m11.toInt()] = 1f
        `val`[Matrix3.Companion.m21.toInt()] = 0f
        `val`[Matrix3.Companion.m02.toInt()] = 0f
        `val`[Matrix3.Companion.m12.toInt()] = 0f
        `val`[Matrix3.Companion.m22.toInt()] = 1f
        return this
    }

    fun set(values: FloatArray?): Matrix3f {
        `val`[Matrix3.Companion.m00.toInt()] = values!![Matrix3.Companion.m00.toInt()]
        `val`[Matrix3.Companion.m10.toInt()] = values[Matrix3.Companion.m10.toInt()]
        `val`[Matrix3.Companion.m20.toInt()] = values[Matrix3.Companion.m20.toInt()]
        `val`[Matrix3.Companion.m01.toInt()] = values[Matrix3.Companion.m01.toInt()]
        `val`[Matrix3.Companion.m11.toInt()] = values[Matrix3.Companion.m11.toInt()]
        `val`[Matrix3.Companion.m21.toInt()] = values[Matrix3.Companion.m21.toInt()]
        `val`[Matrix3.Companion.m02.toInt()] = values[Matrix3.Companion.m02.toInt()]
        `val`[Matrix3.Companion.m12.toInt()] = values[Matrix3.Companion.m12.toInt()]
        `val`[Matrix3.Companion.m22.toInt()] = values[Matrix3.Companion.m22.toInt()]
        return this
    }

    fun set(matrix: Matrix3f): Matrix3f {
        set(matrix.`val`)
        return this
    }

    fun translate(translation: Vec2f): Matrix3f {
        return translate(translation.x.toDouble(), translation.y.toDouble())
    }

    fun translate(x: Double, y: Double): Matrix3f {
        `val`[Matrix3.Companion.m02.toInt()] += (`val`[Matrix3.Companion.m00.toInt()] * x + `val`[Matrix3.Companion.m01.toInt()] * y).toFloat()
        `val`[Matrix3.Companion.m12.toInt()] += (`val`[Matrix3.Companion.m10.toInt()] * x + `val`[Matrix3.Companion.m11.toInt()] * y).toFloat()
        `val`[Matrix3.Companion.m22.toInt()] += (`val`[Matrix3.Companion.m20.toInt()] * x + `val`[Matrix3.Companion.m21.toInt()] * y).toFloat()
        return this
    }

    fun toScaled(scale: Float): Matrix3f {
        return toScaled(scale, scale)
    }

    fun toScaled(x: Float, y: Float): Matrix3f {
        identity()
        `val`[Matrix3.Companion.m00.toInt()] = x
        `val`[Matrix3.Companion.m11.toInt()] = y
        return this
    }

    fun toTranslated(position: Vec2f): Matrix3f {
        return toTranslated(position.x, position.y)
    }

    fun toTranslated(x: Float, y: Float): Matrix3f {
        identity()
        `val`[Matrix3.Companion.m02.toInt()] = x
        `val`[Matrix3.Companion.m12.toInt()] = y
        return this
    }

    /**
     * Set matrix to rotated
     *
     * @param angle Angle in degrees
     * @return      That instance
     */
    fun toRotated(angle: Double): Matrix3f {
        identity()
        val cos = Mathc.cos(angle * Maths.ToRad)
        val sin = Mathc.sin(angle * Maths.ToRad)
        `val`[Matrix3.Companion.m00.toInt()] = cos
        `val`[Matrix3.Companion.m01.toInt()] = -sin
        `val`[Matrix3.Companion.m10.toInt()] = sin
        `val`[Matrix3.Companion.m11.toInt()] = cos
        return this
    }

    fun toSheared(angleX: Float, angleY: Float): Matrix3f {
        identity()
        `val`[Matrix3.Companion.m01.toInt()] = Mathc.tan((angleX * Maths.ToRad).toDouble())
        `val`[Matrix3.Companion.m10.toInt()] = Mathc.tan((angleY * Maths.ToRad).toDouble())
        return this
    }

    fun getMul(matrix: Matrix3f): FloatArray {
        return mul(this, matrix)
    }

    fun mul(matrix: Matrix3f): Matrix3f {
        return set(mul(this, matrix))
    }

    fun getMul(matrix: FloatArray?): FloatArray {
        return mul(`val`, matrix)
    }

    fun mul(matrix: FloatArray?): Matrix3f {
        return set(mul(`val`, matrix))
    }

    fun copy(): Matrix3f {
        return Matrix3f(this)
    }

    companion object {
        fun mul(a: Matrix3f, b: Matrix3f): FloatArray {
            return mul(a.`val`, b.`val`)
        }

        fun mul(a: FloatArray, b: FloatArray?): FloatArray {
            return floatArrayOf(
                a[Matrix3.Companion.m00.toInt()] * b!![Matrix3.Companion.m00.toInt()] + a[Matrix3.Companion.m01.toInt()] * b[Matrix3.Companion.m10.toInt()] + a[Matrix3.Companion.m02.toInt()] * b[Matrix3.Companion.m20.toInt()],
                a[Matrix3.Companion.m10.toInt()] * b[Matrix3.Companion.m00.toInt()] + a[Matrix3.Companion.m11.toInt()] * b[Matrix3.Companion.m10.toInt()] + a[Matrix3.Companion.m12.toInt()] * b[Matrix3.Companion.m20.toInt()],
                a[Matrix3.Companion.m20.toInt()] * b[Matrix3.Companion.m00.toInt()] + a[Matrix3.Companion.m21.toInt()] * b[Matrix3.Companion.m10.toInt()] + a[Matrix3.Companion.m22.toInt()] * b[Matrix3.Companion.m20.toInt()],
                a[Matrix3.Companion.m00.toInt()] * b[Matrix3.Companion.m01.toInt()] + a[Matrix3.Companion.m01.toInt()] * b[Matrix3.Companion.m11.toInt()] + a[Matrix3.Companion.m02.toInt()] * b[Matrix3.Companion.m21.toInt()],
                a[Matrix3.Companion.m10.toInt()] * b[Matrix3.Companion.m01.toInt()] + a[Matrix3.Companion.m11.toInt()] * b[Matrix3.Companion.m11.toInt()] + a[Matrix3.Companion.m12.toInt()] * b[Matrix3.Companion.m21.toInt()],
                a[Matrix3.Companion.m20.toInt()] * b[Matrix3.Companion.m01.toInt()] + a[Matrix3.Companion.m21.toInt()] * b[Matrix3.Companion.m11.toInt()] + a[Matrix3.Companion.m22.toInt()] * b[Matrix3.Companion.m21.toInt()],
                a[Matrix3.Companion.m00.toInt()] * b[Matrix3.Companion.m02.toInt()] + a[Matrix3.Companion.m01.toInt()] * b[Matrix3.Companion.m12.toInt()] + a[Matrix3.Companion.m02.toInt()] * b[Matrix3.Companion.m22.toInt()],
                a[Matrix3.Companion.m10.toInt()] * b[Matrix3.Companion.m02.toInt()] + a[Matrix3.Companion.m11.toInt()] * b[Matrix3.Companion.m12.toInt()] + a[Matrix3.Companion.m12.toInt()] * b[Matrix3.Companion.m22.toInt()],
                a[Matrix3.Companion.m20.toInt()] * b[Matrix3.Companion.m02.toInt()] + a[Matrix3.Companion.m21.toInt()] * b[Matrix3.Companion.m12.toInt()] + a[Matrix3.Companion.m22.toInt()] * b[Matrix3.Companion.m22.toInt()]
            )
        }
    }
}
