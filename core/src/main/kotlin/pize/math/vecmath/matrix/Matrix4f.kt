package pize.math.vecmath.matrix

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.vector.*
import java.util.*

class Matrix4f : Matrix4 {
    val `val`: FloatArray

    constructor() {
        `val` = FloatArray(16)
        `val`[Matrix4.Companion.m00.toInt()] = 1f
        `val`[Matrix4.Companion.m11.toInt()] = 1f
        `val`[Matrix4.Companion.m22.toInt()] = 1f
        `val`[Matrix4.Companion.m33.toInt()] = 1f
    }

    constructor(matrix4: Matrix4f?) {
        `val` = FloatArray(16)
        set(matrix4)
    }

    constructor(values: FloatArray?) {
        `val` = FloatArray(16)
        set(values)
    }

    fun identity(): Matrix4f {
        `val`[Matrix4.Companion.m00.toInt()] = 1f
        `val`[Matrix4.Companion.m10.toInt()] = 0f
        `val`[Matrix4.Companion.m20.toInt()] = 0f
        `val`[Matrix4.Companion.m30.toInt()] = 0f
        `val`[Matrix4.Companion.m01.toInt()] = 0f
        `val`[Matrix4.Companion.m11.toInt()] = 1f
        `val`[Matrix4.Companion.m21.toInt()] = 0f
        `val`[Matrix4.Companion.m31.toInt()] = 0f
        `val`[Matrix4.Companion.m02.toInt()] = 0f
        `val`[Matrix4.Companion.m12.toInt()] = 0f
        `val`[Matrix4.Companion.m22.toInt()] = 1f
        `val`[Matrix4.Companion.m32.toInt()] = 0f
        `val`[Matrix4.Companion.m03.toInt()] = 0f
        `val`[Matrix4.Companion.m13.toInt()] = 0f
        `val`[Matrix4.Companion.m23.toInt()] = 0f
        `val`[Matrix4.Companion.m33.toInt()] = 1f
        return this
    }

    fun zero(): Matrix4f {
        Arrays.fill(`val`, 0f)
        return this
    }

    fun set(values: FloatArray?): Matrix4f {
        `val`[Matrix4.Companion.m00.toInt()] = values!![Matrix4.Companion.m00.toInt()]
        `val`[Matrix4.Companion.m10.toInt()] = values[Matrix4.Companion.m10.toInt()]
        `val`[Matrix4.Companion.m20.toInt()] = values[Matrix4.Companion.m20.toInt()]
        `val`[Matrix4.Companion.m30.toInt()] = values[Matrix4.Companion.m30.toInt()]
        `val`[Matrix4.Companion.m01.toInt()] = values[Matrix4.Companion.m01.toInt()]
        `val`[Matrix4.Companion.m11.toInt()] = values[Matrix4.Companion.m11.toInt()]
        `val`[Matrix4.Companion.m21.toInt()] = values[Matrix4.Companion.m21.toInt()]
        `val`[Matrix4.Companion.m31.toInt()] = values[Matrix4.Companion.m31.toInt()]
        `val`[Matrix4.Companion.m02.toInt()] = values[Matrix4.Companion.m02.toInt()]
        `val`[Matrix4.Companion.m12.toInt()] = values[Matrix4.Companion.m12.toInt()]
        `val`[Matrix4.Companion.m22.toInt()] = values[Matrix4.Companion.m22.toInt()]
        `val`[Matrix4.Companion.m32.toInt()] = values[Matrix4.Companion.m32.toInt()]
        `val`[Matrix4.Companion.m03.toInt()] = values[Matrix4.Companion.m03.toInt()]
        `val`[Matrix4.Companion.m13.toInt()] = values[Matrix4.Companion.m13.toInt()]
        `val`[Matrix4.Companion.m23.toInt()] = values[Matrix4.Companion.m23.toInt()]
        `val`[Matrix4.Companion.m33.toInt()] = values[Matrix4.Companion.m33.toInt()]
        return this
    }

    fun set(matrix: Matrix4f?): Matrix4f {
        set(matrix!!.`val`)
        return this
    }

    fun translate(v: Vec2f): Matrix4f {
        return translate(v.x.toDouble(), v.y.toDouble(), 0.0)
    }

    fun translate(v: Vec3f): Matrix4f {
        return translate(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
    }

    fun translate(v: Vec3d): Matrix4f {
        return translate(v.x, v.y, v.z)
    }

    fun translate(x: Double, y: Double, z: Double): Matrix4f {
        `val`[Matrix4.Companion.m03.toInt()] += (`val`[Matrix4.Companion.m00.toInt()] * x + `val`[Matrix4.Companion.m01.toInt()] * y + `val`[Matrix4.Companion.m02.toInt()] * z).toFloat()
        `val`[Matrix4.Companion.m13.toInt()] += (`val`[Matrix4.Companion.m10.toInt()] * x + `val`[Matrix4.Companion.m11.toInt()] * y + `val`[Matrix4.Companion.m12.toInt()] * z).toFloat()
        `val`[Matrix4.Companion.m23.toInt()] += (`val`[Matrix4.Companion.m20.toInt()] * x + `val`[Matrix4.Companion.m21.toInt()] * y + `val`[Matrix4.Companion.m22.toInt()] * z).toFloat()
        `val`[Matrix4.Companion.m33.toInt()] += (`val`[Matrix4.Companion.m30.toInt()] * x + `val`[Matrix4.Companion.m31.toInt()] * y + `val`[Matrix4.Companion.m32.toInt()] * z).toFloat()
        return this
    }

    fun toScaled(v: Vec3f): Matrix4f {
        return toScaled(v.x, v.y, v.z)
    }

    fun toScaled(v: Vec3d): Matrix4f {
        return toScaled(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
    }

    fun toScaled(v: Float): Matrix4f {
        return toScaled(v, v, v)
    }

    fun toScaled(x: Float, y: Float, z: Float): Matrix4f {
        identity()
        `val`[Matrix4.Companion.m00.toInt()] = x
        `val`[Matrix4.Companion.m11.toInt()] = y
        `val`[Matrix4.Companion.m22.toInt()] = z
        return this
    }

    fun toTranslated(v: Vec2f): Matrix4f {
        return toTranslated(v.x, v.y, 0f)
    }

    fun toTranslated(v: Vec2i): Matrix4f {
        return toTranslated(v.x.toFloat(), v.y.toFloat(), 0f)
    }

    fun toTranslated(v: Vec3f): Matrix4f {
        return toTranslated(v.x, v.y, v.z)
    }

    fun toTranslated(v: Vec3i): Matrix4f {
        return toTranslated(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
    }

    fun toTranslated(v: Vec3d): Matrix4f {
        return toTranslated(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
    }

    fun toTranslated(x: Float, y: Float, z: Float): Matrix4f {
        identity()
        `val`[Matrix4.Companion.m03.toInt()] = x
        `val`[Matrix4.Companion.m13.toInt()] = y
        `val`[Matrix4.Companion.m23.toInt()] = z
        return this
    }

    fun toRotatedX(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        `val`[Matrix4.Companion.m11.toInt()] = cos
        `val`[Matrix4.Companion.m12.toInt()] = -sin
        `val`[Matrix4.Companion.m21.toInt()] = sin
        `val`[Matrix4.Companion.m22.toInt()] = cos
        return this
    }

    fun toRotatedY(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        `val`[Matrix4.Companion.m00.toInt()] = cos
        `val`[Matrix4.Companion.m02.toInt()] = sin
        `val`[Matrix4.Companion.m20.toInt()] = -sin
        `val`[Matrix4.Companion.m22.toInt()] = cos
        return this
    }

    fun toRotatedZ(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        `val`[Matrix4.Companion.m00.toInt()] = cos
        `val`[Matrix4.Companion.m01.toInt()] = -sin
        `val`[Matrix4.Companion.m10.toInt()] = sin
        `val`[Matrix4.Companion.m11.toInt()] = cos
        return this
    }

    fun toOrthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
        identity()
        `val`[Matrix4.Companion.m00.toInt()] = 2 / (right - left)
        `val`[Matrix4.Companion.m11.toInt()] = 2 / (top - bottom)
        `val`[Matrix4.Companion.m22.toInt()] = -2 / (far - near)
        `val`[Matrix4.Companion.m03.toInt()] = -(right + left) / (right - left)
        `val`[Matrix4.Companion.m13.toInt()] = -(top + bottom) / (top - bottom)
        `val`[Matrix4.Companion.m23.toInt()] = -(far + near) / (far - near)
        return this
    }

    fun toOrthographic(x: Float, y: Float, width: Float, height: Float): Matrix4f {
        identity()
        `val`[Matrix4.Companion.m00.toInt()] = 2 / width
        `val`[Matrix4.Companion.m11.toInt()] = 2 / height
        `val`[Matrix4.Companion.m22.toInt()] = -2f
        `val`[Matrix4.Companion.m03.toInt()] = -(x * 2 + width) / width
        `val`[Matrix4.Companion.m13.toInt()] = -(y * 2 + height) / height
        `val`[Matrix4.Companion.m23.toInt()] = -1f
        return this
    }

    fun toPerspective(width: Float, height: Float, near: Float, far: Float, fov: Float): Matrix4f {
        zero()
        val ctgFov = 1 / Mathc.tan(Math.toRadians(fov * 0.5))
        val aspect = width / height
        `val`[Matrix4.Companion.m00.toInt()] = ctgFov / aspect
        `val`[Matrix4.Companion.m11.toInt()] = ctgFov
        `val`[Matrix4.Companion.m22.toInt()] = (far + near) / (far - near)
        `val`[Matrix4.Companion.m32.toInt()] = 1f
        `val`[Matrix4.Companion.m23.toInt()] = -(2 * far * near) / (far - near)
        return this
    }

    fun toLookAt(
        leftX: Float,
        leftY: Float,
        leftZ: Float,
        upX: Float,
        upY: Float,
        upZ: Float,
        forwardX: Float,
        forwardY: Float,
        forwardZ: Float
    ): Matrix4f {
        identity()
        `val`[Matrix4.Companion.m00.toInt()] = leftX
        `val`[Matrix4.Companion.m01.toInt()] = leftY
        `val`[Matrix4.Companion.m02.toInt()] = leftZ
        `val`[Matrix4.Companion.m10.toInt()] = upX
        `val`[Matrix4.Companion.m11.toInt()] = upY
        `val`[Matrix4.Companion.m12.toInt()] = upZ
        `val`[Matrix4.Companion.m20.toInt()] = forwardX
        `val`[Matrix4.Companion.m21.toInt()] = forwardY
        `val`[Matrix4.Companion.m22.toInt()] = forwardZ
        return this
    }

    fun toLookAt(left: Vec3f, up: Vec3f, forward: Vec3f?): Matrix4f {
        return toLookAt(left.x, left.y, left.z, up.x, up.y, up.z, forward!!.x, forward.y, forward.z)
    }

    fun toLookAt(posX: Float, posY: Float, posZ: Float, left: Vec3f, up: Vec3f, forward: Vec3f?): Matrix4f {
        return toLookAt(left, up, forward).mul(Matrix4f().toTranslated(-posX, -posY, -posZ))
    }

    fun toLookAt(position: Vec3f, direction: Vec3f?): Matrix4f {
        return toLookAt(position.x, position.y, position.z, direction)
    }

    fun toLookAt(posX: Float, posY: Float, posZ: Float, direction: Vec3f?): Matrix4f {
        left.set(Vec3f.Companion.crs(up, direction!!.nor()).nor())
        camUp.set(Vec3f.Companion.crs(direction, left).nor())
        return toLookAt(posX, posY, posZ, left, camUp, direction)
    }

    fun toLookAt(direction: Vec3f): Matrix4f {
        left.set(Vec3f.Companion.crs(up, direction.nor()).nor())
        camUp.set(Vec3f.Companion.crs(direction, left).nor())
        return toLookAt(left, camUp, direction)
    }

    fun cullPosition(): Matrix4f {
        `val`[Matrix4.Companion.m03.toInt()] = 0f
        `val`[Matrix4.Companion.m13.toInt()] = 0f
        `val`[Matrix4.Companion.m23.toInt()] = 0f
        return this
    }

    fun cullRotation(): Matrix4f {
        `val`[Matrix4.Companion.m00.toInt()] = 1f
        `val`[Matrix4.Companion.m10.toInt()] = 0f
        `val`[Matrix4.Companion.m20.toInt()] = 0f
        `val`[Matrix4.Companion.m01.toInt()] = 0f
        `val`[Matrix4.Companion.m11.toInt()] = 1f
        `val`[Matrix4.Companion.m21.toInt()] = 0f
        `val`[Matrix4.Companion.m02.toInt()] = 0f
        `val`[Matrix4.Companion.m12.toInt()] = 0f
        `val`[Matrix4.Companion.m22.toInt()] = 1f
        return this
    }

    fun getMul(matrix: Matrix4f?): FloatArray {
        return mul(this, matrix)
    }

    fun mul(matrix: Matrix4f?): Matrix4f {
        return set(mul(this, matrix))
    }

    fun getMul(matrix: FloatArray?): FloatArray {
        return mul(`val`, matrix)
    }

    fun mul(matrix: FloatArray?): Matrix4f {
        return set(mul(`val`, matrix))
    }

    fun copy(): Matrix4f {
        return Matrix4f(this)
    }

    companion object {
        private val upDouble = Vec3d(0, 1, 0)
        private val up = Vec3f(0, 1, 0)
        private val left = Vec3f()
        private val camUp = Vec3f()
        fun mul(a: Matrix4f, b: Matrix4f?): FloatArray {
            return mul(a.`val`, b!!.`val`)
        }

        fun mul(a: FloatArray?, b: FloatArray?): FloatArray {
            return floatArrayOf(
                a!![Matrix4.Companion.m00.toInt()] * b!![Matrix4.Companion.m00.toInt()] + a[Matrix4.Companion.m01.toInt()] * b[Matrix4.Companion.m10.toInt()] + a[Matrix4.Companion.m02.toInt()] * b[Matrix4.Companion.m20.toInt()] + a[Matrix4.Companion.m03.toInt()] * b[Matrix4.Companion.m30.toInt()],
                a[Matrix4.Companion.m10.toInt()] * b[Matrix4.Companion.m00.toInt()] + a[Matrix4.Companion.m11.toInt()] * b[Matrix4.Companion.m10.toInt()] + a[Matrix4.Companion.m12.toInt()] * b[Matrix4.Companion.m20.toInt()] + a[Matrix4.Companion.m13.toInt()] * b[Matrix4.Companion.m30.toInt()],
                a[Matrix4.Companion.m20.toInt()] * b[Matrix4.Companion.m00.toInt()] + a[Matrix4.Companion.m21.toInt()] * b[Matrix4.Companion.m10.toInt()] + a[Matrix4.Companion.m22.toInt()] * b[Matrix4.Companion.m20.toInt()] + a[Matrix4.Companion.m23.toInt()] * b[Matrix4.Companion.m30.toInt()],
                a[Matrix4.Companion.m30.toInt()] * b[Matrix4.Companion.m00.toInt()] + a[Matrix4.Companion.m31.toInt()] * b[Matrix4.Companion.m10.toInt()] + a[Matrix4.Companion.m32.toInt()] * b[Matrix4.Companion.m20.toInt()] + a[Matrix4.Companion.m33.toInt()] * b[Matrix4.Companion.m30.toInt()],
                a[Matrix4.Companion.m00.toInt()] * b[Matrix4.Companion.m01.toInt()] + a[Matrix4.Companion.m01.toInt()] * b[Matrix4.Companion.m11.toInt()] + a[Matrix4.Companion.m02.toInt()] * b[Matrix4.Companion.m21.toInt()] + a[Matrix4.Companion.m03.toInt()] * b[Matrix4.Companion.m31.toInt()],
                a[Matrix4.Companion.m10.toInt()] * b[Matrix4.Companion.m01.toInt()] + a[Matrix4.Companion.m11.toInt()] * b[Matrix4.Companion.m11.toInt()] + a[Matrix4.Companion.m12.toInt()] * b[Matrix4.Companion.m21.toInt()] + a[Matrix4.Companion.m13.toInt()] * b[Matrix4.Companion.m31.toInt()],
                a[Matrix4.Companion.m20.toInt()] * b[Matrix4.Companion.m01.toInt()] + a[Matrix4.Companion.m21.toInt()] * b[Matrix4.Companion.m11.toInt()] + a[Matrix4.Companion.m22.toInt()] * b[Matrix4.Companion.m21.toInt()] + a[Matrix4.Companion.m23.toInt()] * b[Matrix4.Companion.m31.toInt()],
                a[Matrix4.Companion.m30.toInt()] * b[Matrix4.Companion.m01.toInt()] + a[Matrix4.Companion.m31.toInt()] * b[Matrix4.Companion.m11.toInt()] + a[Matrix4.Companion.m32.toInt()] * b[Matrix4.Companion.m21.toInt()] + a[Matrix4.Companion.m33.toInt()] * b[Matrix4.Companion.m31.toInt()],
                a[Matrix4.Companion.m00.toInt()] * b[Matrix4.Companion.m02.toInt()] + a[Matrix4.Companion.m01.toInt()] * b[Matrix4.Companion.m12.toInt()] + a[Matrix4.Companion.m02.toInt()] * b[Matrix4.Companion.m22.toInt()] + a[Matrix4.Companion.m03.toInt()] * b[Matrix4.Companion.m32.toInt()],
                a[Matrix4.Companion.m10.toInt()] * b[Matrix4.Companion.m02.toInt()] + a[Matrix4.Companion.m11.toInt()] * b[Matrix4.Companion.m12.toInt()] + a[Matrix4.Companion.m12.toInt()] * b[Matrix4.Companion.m22.toInt()] + a[Matrix4.Companion.m13.toInt()] * b[Matrix4.Companion.m32.toInt()],
                a[Matrix4.Companion.m20.toInt()] * b[Matrix4.Companion.m02.toInt()] + a[Matrix4.Companion.m21.toInt()] * b[Matrix4.Companion.m12.toInt()] + a[Matrix4.Companion.m22.toInt()] * b[Matrix4.Companion.m22.toInt()] + a[Matrix4.Companion.m23.toInt()] * b[Matrix4.Companion.m32.toInt()],
                a[Matrix4.Companion.m30.toInt()] * b[Matrix4.Companion.m02.toInt()] + a[Matrix4.Companion.m31.toInt()] * b[Matrix4.Companion.m12.toInt()] + a[Matrix4.Companion.m32.toInt()] * b[Matrix4.Companion.m22.toInt()] + a[Matrix4.Companion.m33.toInt()] * b[Matrix4.Companion.m32.toInt()],
                a[Matrix4.Companion.m00.toInt()] * b[Matrix4.Companion.m03.toInt()] + a[Matrix4.Companion.m01.toInt()] * b[Matrix4.Companion.m13.toInt()] + a[Matrix4.Companion.m02.toInt()] * b[Matrix4.Companion.m23.toInt()] + a[Matrix4.Companion.m03.toInt()] * b[Matrix4.Companion.m33.toInt()],
                a[Matrix4.Companion.m10.toInt()] * b[Matrix4.Companion.m03.toInt()] + a[Matrix4.Companion.m11.toInt()] * b[Matrix4.Companion.m13.toInt()] + a[Matrix4.Companion.m12.toInt()] * b[Matrix4.Companion.m23.toInt()] + a[Matrix4.Companion.m13.toInt()] * b[Matrix4.Companion.m33.toInt()],
                a[Matrix4.Companion.m20.toInt()] * b[Matrix4.Companion.m03.toInt()] + a[Matrix4.Companion.m21.toInt()] * b[Matrix4.Companion.m13.toInt()] + a[Matrix4.Companion.m22.toInt()] * b[Matrix4.Companion.m23.toInt()] + a[Matrix4.Companion.m23.toInt()] * b[Matrix4.Companion.m33.toInt()],
                a[Matrix4.Companion.m30.toInt()] * b[Matrix4.Companion.m03.toInt()] + a[Matrix4.Companion.m31.toInt()] * b[Matrix4.Companion.m13.toInt()] + a[Matrix4.Companion.m32.toInt()] * b[Matrix4.Companion.m23.toInt()] + a[Matrix4.Companion.m33.toInt()] * b[Matrix4.Companion.m33.toInt()]
            )
        }
    }
}