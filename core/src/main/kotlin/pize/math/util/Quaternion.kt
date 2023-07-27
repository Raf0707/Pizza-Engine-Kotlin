package pize.math.util

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix4
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f

class Quaternion {
    var x = 0f
    var y = 0f
    var z = 0f
    var w = 0f

    constructor() {
        w = 1f
    }

    constructor(quat: Quaternion) {
        set(quat)
    }

    constructor(x: Float, y: Float, z: Float, w: Float) {
        set(x, y, z, w)
    }

    constructor(axis: Vec3f, angle: Float, degrees: Boolean) {
        set(axis, angle.toDouble(), degrees)
    }

    constructor(eulerAngles: EulerAngles) {
        set(eulerAngles)
    }

    fun set(quat: Quaternion): Quaternion {
        return set(quat.x, quat.y, quat.z, quat.w)
    }

    operator fun set(x: Float, y: Float, z: Float, w: Float): Quaternion {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }

    operator fun set(x: Double, y: Double, z: Double, w: Double): Quaternion {
        return set(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())
    }

    operator fun set(axis: Vec3f, angleRad: Double): Quaternion {
        val sin = Mathc.sin(angleRad * 0.5)
        x = axis.x * sin
        y = axis.y * sin
        z = axis.z * sin
        w = Mathc.cos(angleRad * 0.5)
        return this
    }

    operator fun set(axis: Vec3f, angle: Double, degrees: Boolean): Quaternion {
        return set(axis, if (degrees) angle * Maths.ToRad else angle)
    }

    operator fun set(yaw: Double, pitch: Double, roll: Double): Quaternion {
        val cr = Mathc.cos(roll * 0.5)
        val sr = Mathc.sin(roll * 0.5)
        val cp = Mathc.cos(pitch * 0.5)
        val sp = Mathc.sin(pitch * 0.5)
        val cy = Mathc.cos(yaw * 0.5)
        val sy = Mathc.sin(yaw * 0.5)
        w = cr * cp * cy + sr * sp * sy
        x = sr * cp * cy - cr * sp * sy
        y = cr * sp * cy + sr * cp * sy
        z = cr * cp * sy - sr * sp * cy
        return this
    }

    fun set(eulerAngles: EulerAngles): Quaternion {
        return set(
            (eulerAngles.yaw * Maths.ToRad).toDouble(),
            (eulerAngles.pitch * Maths.ToRad).toDouble(),
            (eulerAngles.roll * Maths.ToRad).toDouble()
        )
    }

    fun len2(): Float {
        return x * x + y * y + z * z + w * w
    }

    fun len(): Float {
        return Mathc.sqrt(len2().toDouble())
    }

    fun nor(): Quaternion {
        var len = len2()
        if (len == 0f || len == 1f) return this
        len = Maths.invSqrt(len)
        w *= len
        x *= len
        y *= len
        z *= len
        return this
    }

    fun conjugate(): Quaternion {
        x = -x
        y = -y
        z = -z
        return this
    }

    val gimbalPole: Int
        get() {
            val t = x * y + z * w
            return if (t > 0.499) 1 else if (t < -0.499) -1 else 0
        }
    val rollRad: Float
        get() {
            val pole = gimbalPole
            return if (pole == 0) Mathc.atan2(
                (2 * (w * z + y * x)).toDouble(),
                (1 - 2 * (x * x + z * z)).toDouble()
            ) else pole * 2 * Mathc.atan2(y.toDouble(), w.toDouble())
        }
    val roll: Float
        get() = rollRad * Maths.ToDeg
    val pitchRad: Float
        get() {
            val pole = gimbalPole
            return if (pole == 0) Mathc.asin(
                Maths.clamp(2 * (w * x - z * y), -1f, 1f).toDouble()
            ) else pole * Maths.PI * 0.5f
        }
    val pitch: Float
        get() = pitchRad * Maths.ToDeg
    val yawRad: Float
        get() = if (gimbalPole == 0) Mathc.atan2(
            (2 * (y * w + x * z)).toDouble(),
            (1 - 2 * (y * y + x * x)).toDouble()
        ) else 0
    val yaw: Float
        get() = yawRad * Maths.ToDeg

    fun toMatrix(): Matrix4f {
        val xx = x * x
        val xy = x * y
        val xz = x * z
        val xw = x * w
        val yy = y * y
        val yz = y * z
        val yw = y * w
        val zz = z * z
        val zw = z * w
        val result = Matrix4f()
        result.`val`[Matrix4.Companion.m00.toInt()] = 1 - 2 * (yy + zz)
        result.`val`[Matrix4.Companion.m01.toInt()] = 2 * (xy - zw)
        result.`val`[Matrix4.Companion.m02.toInt()] = 2 * (xz + yw)
        result.`val`[Matrix4.Companion.m03.toInt()] = 0f
        result.`val`[Matrix4.Companion.m10.toInt()] = 2 * (xy + zw)
        result.`val`[Matrix4.Companion.m11.toInt()] = 1 - 2 * (xx + zz)
        result.`val`[Matrix4.Companion.m12.toInt()] = 2 * (yz - xw)
        result.`val`[Matrix4.Companion.m13.toInt()] = 0f
        result.`val`[Matrix4.Companion.m20.toInt()] = 2 * (xz - yw)
        result.`val`[Matrix4.Companion.m21.toInt()] = 2 * (yz + xw)
        result.`val`[Matrix4.Companion.m22.toInt()] = 1 - 2 * (xx + yy)
        result.`val`[Matrix4.Companion.m23.toInt()] = 0f
        result.`val`[Matrix4.Companion.m30.toInt()] = 0f
        result.`val`[Matrix4.Companion.m31.toInt()] = 0f
        result.`val`[Matrix4.Companion.m32.toInt()] = 0f
        result.`val`[Matrix4.Companion.m33.toInt()] = 1f
        return result
    }

    fun mul(quat: Quaternion): Quaternion {
        x = w * quat.x + x * quat.w + y * quat.z - z * quat.y
        y = w * quat.y - x * quat.z + y * quat.w + z * quat.x
        z = w * quat.z + x * quat.y - y * quat.x + z * quat.w
        w = w * quat.w - x * quat.x - y * quat.y - z * quat.z
        return this
    }

    fun mul(x: Float, y: Float, z: Float, w: Float): Quaternion {
        this.x = this.w * x + this.x * w + this.y * z - this.z * y
        this.y = this.w * y + this.y * w + this.z * x - this.x * z
        this.z = this.w * z + this.z * w + this.x * y - this.y * x
        this.w = this.w * w - this.x * x - this.y * y - this.z * z
        return this
    }

    fun add(quat: Quaternion): Quaternion {
        x += quat.x
        y += quat.y
        z += quat.z
        w += quat.w
        return this
    }

    fun add(x: Float, y: Float, z: Float, w: Float): Quaternion {
        this.x += x
        this.y += y
        this.z += z
        this.w += w
        return this
    }

    fun copy(): Quaternion {
        return Quaternion(this)
    }

    override fun toString(): String {
        return "$x, $y, $z, $w"
    }

    companion object {
        fun fromRotation(x: Float, y: Float, z: Float): Quaternion {
            val quaternion = Quaternion()
            quaternion.mul(Quaternion(Mathc.sin((x / 2).toDouble()), 0f, 0f, Mathc.cos((x / 2).toDouble())))
            quaternion.mul(Quaternion(0f, Mathc.sin((y / 2).toDouble()), 0f, Mathc.cos((y / 2).toDouble())))
            quaternion.mul(Quaternion(0f, 0f, Mathc.sin((z / 2).toDouble()), Mathc.cos((z / 2).toDouble())))
            return quaternion
        }
    }
}
