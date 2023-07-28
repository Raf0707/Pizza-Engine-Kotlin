package pize.math.vecmath.matrix

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.vector.*
import java.util.*

class Matrix4f : Matrix4 {
    val values: FloatArray

    constructor() {
        values = FloatArray(16)
        values[Matrix4.m00] = 1F
        values[Matrix4.m11] = 1F
        values[Matrix4.m22] = 1F
        values[Matrix4.m33] = 1F
    }

    constructor(matrix4: Matrix4f?) {
        values = FloatArray(16)
        set(matrix4)
    }

    constructor(values: FloatArray?) {
        this.values = FloatArray(16)
        set(values)
    }

    fun identity(): Matrix4f {
        values[Matrix4.m00] = 1F
        values[Matrix4.m10] = 0F
        values[Matrix4.m20] = 0F
        values[Matrix4.m30] = 0F
        values[Matrix4.m01] = 0F
        values[Matrix4.m11] = 1F
        values[Matrix4.m21] = 0F
        values[Matrix4.m31] = 0F
        values[Matrix4.m02] = 0F
        values[Matrix4.m12] = 0F
        values[Matrix4.m22] = 1F
        values[Matrix4.m32] = 0F
        values[Matrix4.m03] = 0F
        values[Matrix4.m13] = 0F
        values[Matrix4.m23] = 0F
        values[Matrix4.m33] = 1F
        return this
    }

    fun zero(): Matrix4f {
        Arrays.fill(values, 0F)
        return this
    }

    fun set(values: FloatArray?): Matrix4f {
        this.values[Matrix4.m00] = values!![Matrix4.m00]
        this.values[Matrix4.m10] = values[Matrix4.m10]
        this.values[Matrix4.m20] = values[Matrix4.m20]
        this.values[Matrix4.m30] = values[Matrix4.m30]
        this.values[Matrix4.m01] = values[Matrix4.m01]
        this.values[Matrix4.m11] = values[Matrix4.m11]
        this.values[Matrix4.m21] = values[Matrix4.m21]
        this.values[Matrix4.m31] = values[Matrix4.m31]
        this.values[Matrix4.m02] = values[Matrix4.m02]
        this.values[Matrix4.m12] = values[Matrix4.m12]
        this.values[Matrix4.m22] = values[Matrix4.m22]
        this.values[Matrix4.m32] = values[Matrix4.m32]
        this.values[Matrix4.m03] = values[Matrix4.m03]
        this.values[Matrix4.m13] = values[Matrix4.m13]
        this.values[Matrix4.m23] = values[Matrix4.m23]
        this.values[Matrix4.m33] = values[Matrix4.m33]
        return this
    }

    fun set(matrix: Matrix4f?): Matrix4f {
        set(matrix!!.values)
        return this
    }

    fun translate(v: Vec2f): Matrix4f {
        return translate(v.x, v.y, 0F)
    }

    fun translate(v: Vec3f): Matrix4f {
        return translate(v.x, v.y, v.z)
    }

    fun translate(v: Vec3d): Matrix4f {
        return translate(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
    }

    fun translate(x: Float, y: Float, z: Float): Matrix4f {
        values[Matrix4.m03] += (values[Matrix4.m00] * x + values[Matrix4.m01] * y + values[Matrix4.m02] * z)
        values[Matrix4.m13] += (values[Matrix4.m10] * x + values[Matrix4.m11] * y + values[Matrix4.m12] * z)
        values[Matrix4.m23] += (values[Matrix4.m20] * x + values[Matrix4.m21] * y + values[Matrix4.m22] * z)
        values[Matrix4.m33] += (values[Matrix4.m30] * x + values[Matrix4.m31] * y + values[Matrix4.m32] * z)
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
        values[Matrix4.m00] = x
        values[Matrix4.m11] = y
        values[Matrix4.m22] = z
        return this
    }

    fun toTranslated(v: Vec2f): Matrix4f {
        return toTranslated(v.x, v.y, 0F)
    }

    fun toTranslated(v: Vec2i): Matrix4f {
        return toTranslated(v.x.toFloat(), v.y.toFloat(), 0F)
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
        values[Matrix4.m03] = x
        values[Matrix4.m13] = y
        values[Matrix4.m23] = z
        return this
    }

    fun toRotatedX(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        values[Matrix4.m11] = cos
        values[Matrix4.m12] = -sin
        values[Matrix4.m21] = sin
        values[Matrix4.m22] = cos
        return this
    }

    fun toRotatedY(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        values[Matrix4.m00] = cos
        values[Matrix4.m02] = sin
        values[Matrix4.m20] = -sin
        values[Matrix4.m22] = cos
        return this
    }

    fun toRotatedZ(angle: Double): Matrix4f {
        identity()
        val cos = Maths.cosDeg(angle)
        val sin = Maths.sinDeg(angle)
        values[Matrix4.m00] = cos
        values[Matrix4.m01] = -sin
        values[Matrix4.m10] = sin
        values[Matrix4.m11] = cos
        return this
    }

    fun toOrthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
        identity()
        values[Matrix4.m00] = 2 / (right - left)
        values[Matrix4.m11] = 2 / (top - bottom)
        values[Matrix4.m22] = -2 / (far - near)
        values[Matrix4.m03] = -(right + left) / (right - left)
        values[Matrix4.m13] = -(top + bottom) / (top - bottom)
        values[Matrix4.m23] = -(far + near) / (far - near)
        return this
    }

    fun toOrthographic(x: Float, y: Float, width: Float, height: Float): Matrix4f {
        identity()
        values[Matrix4.m00] = 2 / width
        values[Matrix4.m11] = 2 / height
        values[Matrix4.m22] = -2F
        values[Matrix4.m03] = -(x * 2 + width) / width
        values[Matrix4.m13] = -(y * 2 + height) / height
        values[Matrix4.m23] = -1F
        return this
    }

    fun toPerspective(width: Float, height: Float, near: Float, far: Float, fov: Float): Matrix4f {
        zero()
        val ctgFov = 1 / Mathc.tan(Math.toRadians(fov * 0.5))
        val aspect = width / height
        values[Matrix4.m00] = ctgFov / aspect
        values[Matrix4.m11] = ctgFov
        values[Matrix4.m22] = (far + near) / (far - near)
        values[Matrix4.m32] = 1F
        values[Matrix4.m23] = -(2 * far * near) / (far - near)
        return this
    }

    fun toLookAt(leftX: Float, leftY: Float, leftZ: Float, upX: Float, upY: Float, upZ: Float, forwardX: Float, forwardY: Float, forwardZ: Float): Matrix4f {
        identity()
        values[Matrix4.m00] = leftX
        values[Matrix4.m01] = leftY
        values[Matrix4.m02] = leftZ
        values[Matrix4.m10] = upX
        values[Matrix4.m11] = upY
        values[Matrix4.m12] = upZ
        values[Matrix4.m20] = forwardX
        values[Matrix4.m21] = forwardY
        values[Matrix4.m22] = forwardZ
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
        left.set(Vec3f.crs(up, direction!!.nor()).nor())
        camUp.set(Vec3f.crs(direction, left).nor())
        return toLookAt(posX, posY, posZ, left, camUp, direction)
    }

    fun toLookAt(direction: Vec3f): Matrix4f {
        left.set(Vec3f.crs(up, direction.nor()).nor())
        camUp.set(Vec3f.crs(direction, left).nor())
        return toLookAt(left, camUp, direction)
    }

    fun cullPosition(): Matrix4f {
        values[Matrix4.m03] = 0F
        values[Matrix4.m13] = 0F
        values[Matrix4.m23] = 0F
        return this
    }

    fun cullRotation(): Matrix4f {
        values[Matrix4.m00] = 1F
        values[Matrix4.m10] = 0F
        values[Matrix4.m20] = 0F
        values[Matrix4.m01] = 0F
        values[Matrix4.m11] = 1F
        values[Matrix4.m21] = 0F
        values[Matrix4.m02] = 0F
        values[Matrix4.m12] = 0F
        values[Matrix4.m22] = 1F
        return this
    }

    fun getMul(matrix: Matrix4f?): FloatArray {
        return mul(this, matrix)
    }

    fun mul(matrix: Matrix4f?): Matrix4f {
        return set(mul(this, matrix))
    }

    fun getMul(matrix: FloatArray?): FloatArray {
        return mul(values, matrix)
    }

    fun mul(matrix: FloatArray?): Matrix4f {
        return set(mul(values, matrix))
    }

    fun copy(): Matrix4f {
        return Matrix4f(this)
    }

    companion object {
        private val up = Vec3f(0, 1, 0)
        private val left = Vec3f()
        private val camUp = Vec3f()
        fun mul(a: Matrix4f, b: Matrix4f?): FloatArray {
            return mul(a.values, b!!.values)
        }

        fun mul(a: FloatArray?, b: FloatArray?): FloatArray {
            return floatArrayOf(
                a!![Matrix4.m00] * b!![Matrix4.m00] + a[Matrix4.m01] * b[Matrix4.m10] + a[Matrix4.m02] * b[Matrix4.m20] + a[Matrix4.m03] * b[Matrix4.m30],
                a[Matrix4.m10] * b[Matrix4.m00] + a[Matrix4.m11] * b[Matrix4.m10] + a[Matrix4.m12] * b[Matrix4.m20] + a[Matrix4.m13] * b[Matrix4.m30],
                a[Matrix4.m20] * b[Matrix4.m00] + a[Matrix4.m21] * b[Matrix4.m10] + a[Matrix4.m22] * b[Matrix4.m20] + a[Matrix4.m23] * b[Matrix4.m30],
                a[Matrix4.m30] * b[Matrix4.m00] + a[Matrix4.m31] * b[Matrix4.m10] + a[Matrix4.m32] * b[Matrix4.m20] + a[Matrix4.m33] * b[Matrix4.m30],
                a[Matrix4.m00] * b[Matrix4.m01] + a[Matrix4.m01] * b[Matrix4.m11] + a[Matrix4.m02] * b[Matrix4.m21] + a[Matrix4.m03] * b[Matrix4.m31],
                a[Matrix4.m10] * b[Matrix4.m01] + a[Matrix4.m11] * b[Matrix4.m11] + a[Matrix4.m12] * b[Matrix4.m21] + a[Matrix4.m13] * b[Matrix4.m31],
                a[Matrix4.m20] * b[Matrix4.m01] + a[Matrix4.m21] * b[Matrix4.m11] + a[Matrix4.m22] * b[Matrix4.m21] + a[Matrix4.m23] * b[Matrix4.m31],
                a[Matrix4.m30] * b[Matrix4.m01] + a[Matrix4.m31] * b[Matrix4.m11] + a[Matrix4.m32] * b[Matrix4.m21] + a[Matrix4.m33] * b[Matrix4.m31],
                a[Matrix4.m00] * b[Matrix4.m02] + a[Matrix4.m01] * b[Matrix4.m12] + a[Matrix4.m02] * b[Matrix4.m22] + a[Matrix4.m03] * b[Matrix4.m32],
                a[Matrix4.m10] * b[Matrix4.m02] + a[Matrix4.m11] * b[Matrix4.m12] + a[Matrix4.m12] * b[Matrix4.m22] + a[Matrix4.m13] * b[Matrix4.m32],
                a[Matrix4.m20] * b[Matrix4.m02] + a[Matrix4.m21] * b[Matrix4.m12] + a[Matrix4.m22] * b[Matrix4.m22] + a[Matrix4.m23] * b[Matrix4.m32],
                a[Matrix4.m30] * b[Matrix4.m02] + a[Matrix4.m31] * b[Matrix4.m12] + a[Matrix4.m32] * b[Matrix4.m22] + a[Matrix4.m33] * b[Matrix4.m32],
                a[Matrix4.m00] * b[Matrix4.m03] + a[Matrix4.m01] * b[Matrix4.m13] + a[Matrix4.m02] * b[Matrix4.m23] + a[Matrix4.m03] * b[Matrix4.m33],
                a[Matrix4.m10] * b[Matrix4.m03] + a[Matrix4.m11] * b[Matrix4.m13] + a[Matrix4.m12] * b[Matrix4.m23] + a[Matrix4.m13] * b[Matrix4.m33],
                a[Matrix4.m20] * b[Matrix4.m03] + a[Matrix4.m21] * b[Matrix4.m13] + a[Matrix4.m22] * b[Matrix4.m23] + a[Matrix4.m23] * b[Matrix4.m33],
                a[Matrix4.m30] * b[Matrix4.m03] + a[Matrix4.m31] * b[Matrix4.m13] + a[Matrix4.m32] * b[Matrix4.m23] + a[Matrix4.m33] * b[Matrix4.m33]
            )
        }
    }
}