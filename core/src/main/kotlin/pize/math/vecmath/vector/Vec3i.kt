package pize.math.vecmath.vector

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix4
import pize.math.vecmath.matrix.Matrix4f
import java.util.*
import kotlin.math.sqrt

class Vec3i {
    constructor()
    constructor(x: Int, y: Int, z: Int) {
        set(x, y, z)
    }

    constructor(xyz: Int) {
        set(xyz)
    }

    constructor(vector: Vec3i) {
        set(vector)
    }

    constructor(vector: Vec2i) {
        set(vector)
    }

    /**             POINT              */
    fun dst(x: Int, y: Int, z: Int): Float {
        val dx = (this.x - x).toDouble()
        val dy = (this.y - y).toDouble()
        val dz = (this.z - z).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(x: Float, y: Float, z: Float): Float {
        val dx = (this.x - x).toDouble()
        val dy = (this.y - y).toDouble()
        val dz = (this.z - z).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(x: Double, y: Double, z: Double): Float {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3i): Float {
        val dx = (x - vector.x).toDouble()
        val dy = (y - vector.y).toDouble()
        val dz = (z - vector.z).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3f): Float {
        val dx = (x - vector.x).toDouble()
        val dy = (y - vector.y).toDouble()
        val dz = (z - vector.z).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3d): Float {
        val dx = x - vector.x
        val dy = y - vector.y
        val dz = z - vector.z
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    /**             VECTOR              */
    fun len(): Float {
        return Mathc.sqrt((x * x + y * y + z * z).toDouble())
    }

    fun abs(): Vec3i {
        if (x < 0) x *= -1
        if (y < 0) y *= -1
        if (z < 0) z *= -1
        return this
    }

    fun zero(): Vec3i {
        set(0, 0, 0)
        return this
    }

    val isZero: Boolean
        get() = x == 0 && y == 0 && z == 0

    fun dot(x: Float, y: Float, z: Float): Float {
        return this.x * x + this.y * y + this.z * z
    }

    fun dot(x: Double, y: Double, z: Double): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dot(x: Int, y: Int, z: Int): Double {
        return (this.x * x + this.y * y + this.z * z).toDouble()
    }

    fun dot(vector: Vec3f): Float {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun dot(vector: Vec3d): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun dot(vector: Vec3i): Double {
        return (x * vector.x + y * vector.y + z * vector.z).toDouble()
    }

    fun crs(x: Int, y: Int, z: Int): Vec3i {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(vector: Vec3i): Vec3i {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun xy(): Vec2i {
        return Vec2i(x, y)
    }

    fun xz(): Vec2i {
        return Vec2i(x, z)
    }

    fun yz(): Vec2i {
        return Vec2i(y, z)
    }

    fun copy(): Vec3i {
        return Vec3i(this)
    }

    /**             TUPLE              */
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    @JvmField
    var z = 0
    operator fun set(x: Int, y: Int, z: Int): Vec3i {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun set(xyz: Int): Vec3i {
        x = xyz
        y = xyz
        z = xyz
        return this
    }

    fun set(vector: Vec2i): Vec3i {
        x = vector.x
        y = vector.y
        z = 0
        return this
    }

    fun set(vector: Vec3i): Vec3i {
        x = vector.x
        y = vector.y
        z = vector.z
        return this
    }

    fun add(x: Double, y: Double, z: Double): Vec3i {
        this.x = (this.x + x).toInt()
        this.y = (this.y + y).toInt()
        this.z = (this.z + z).toInt()
        return this
    }

    fun add(x: Float, y: Float, z: Float): Vec3i {
        this.x = (this.x + x).toInt()
        this.y = (this.y + y).toInt()
        this.z = (this.z + z).toInt()
        return this
    }

    fun add(x: Int, y: Int, z: Int): Vec3i {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(xyz: Double): Vec3i {
        x = (x + xyz).toInt()
        y = (y + xyz).toInt()
        z = (z + xyz).toInt()
        return this
    }

    fun add(xyz: Float): Vec3i {
        x = (x + xyz).toInt()
        y = (y + xyz).toInt()
        z = (z + xyz).toInt()
        return this
    }

    fun add(xyz: Int): Vec3i {
        x += xyz
        y += xyz
        z += xyz
        return this
    }

    fun add(vector: Vec2d): Vec3i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun add(vector: Vec2f): Vec3i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun add(vector: Vec2i): Vec3i {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec3d): Vec3i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        z = (z + vector.z).toInt()
        return this
    }

    fun add(vector: Vec3f): Vec3i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        z = (z + vector.z).toInt()
        return this
    }

    fun add(vector: Vec3i): Vec3i {
        x += vector.x
        y += vector.y
        z += vector.z
        return this
    }

    fun sub(x: Double, y: Double, z: Double): Vec3i {
        this.x = (this.x - x).toInt()
        this.y = (this.y - y).toInt()
        this.z = (this.z - z).toInt()
        return this
    }

    fun sub(x: Float, y: Float, z: Float): Vec3i {
        this.x = (this.x - x).toInt()
        this.y = (this.y - y).toInt()
        this.z = (this.z - z).toInt()
        return this
    }

    fun sub(x: Int, y: Int, z: Int): Vec3i {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(xyz: Double): Vec3i {
        x = (x - xyz).toInt()
        y = (y - xyz).toInt()
        z = (z - xyz).toInt()
        return this
    }

    fun sub(xyz: Float): Vec3i {
        x = (x - xyz).toInt()
        y = (y - xyz).toInt()
        z = (z - xyz).toInt()
        return this
    }

    fun sub(xyz: Int): Vec3i {
        x -= xyz
        y -= xyz
        z -= xyz
        return this
    }

    fun sub(vector: Vec2d): Vec3i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun sub(vector: Vec2f): Vec3i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun sub(vector: Vec2i): Vec3i {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec3d): Vec3i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        z = (z - vector.z).toInt()
        return this
    }

    fun sub(vector: Vec3f): Vec3i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        z = (z - vector.z).toInt()
        return this
    }

    fun sub(vector: Vec3i): Vec3i {
        x -= vector.x
        y -= vector.y
        z -= vector.z
        return this
    }

    fun mul(x: Double, y: Double, z: Double): Vec3i {
        this.x = (this.x * x).toInt()
        this.y = (this.y * y).toInt()
        this.z = (this.z * z).toInt()
        return this
    }

    fun mul(x: Float, y: Float, z: Float): Vec3i {
        this.x = (this.x * x).toInt()
        this.y = (this.y * y).toInt()
        this.z = (this.z * z).toInt()
        return this
    }

    fun mul(x: Int, y: Int, z: Int): Vec3i {
        this.x *= x
        this.y *= y
        this.z *= z
        return this
    }

    fun mul(xyz: Double): Vec3i {
        x = (x * xyz).toInt()
        y = (y * xyz).toInt()
        z = (z * xyz).toInt()
        return this
    }

    fun mul(xyz: Float): Vec3i {
        x = (x * xyz).toInt()
        y = (y * xyz).toInt()
        z = (z * xyz).toInt()
        return this
    }

    fun mul(xyz: Int): Vec3i {
        x *= xyz
        y *= xyz
        z *= xyz
        return this
    }

    fun mul(vector: Vec2d): Vec3i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun mul(vector: Vec2f): Vec3i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun mul(vector: Vec2i): Vec3i {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec3d): Vec3i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        z = (z * vector.z).toInt()
        return this
    }

    fun mul(vector: Vec3f): Vec3i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        z = (z * vector.z).toInt()
        return this
    }

    fun mul(vector: Vec3i): Vec3i {
        x *= vector.x
        y *= vector.y
        z *= vector.z
        return this
    }

    fun div(x: Double, y: Double, z: Double): Vec3i {
        this.x = (this.x / x).toInt()
        this.y = (this.y / y).toInt()
        this.z = (this.z / z).toInt()
        return this
    }

    fun div(x: Float, y: Float, z: Float): Vec3i {
        this.x = (this.x / x).toInt()
        this.y = (this.y / y).toInt()
        this.z = (this.z / z).toInt()
        return this
    }

    fun div(x: Int, y: Int, z: Int): Vec3i {
        this.x /= x
        this.y /= y
        this.z /= z
        return this
    }

    operator fun div(xyz: Double): Vec3i {
        x = (x / xyz).toInt()
        y = (y / xyz).toInt()
        z = (z / xyz).toInt()
        return this
    }

    operator fun div(xyz: Float): Vec3i {
        x = (x / xyz).toInt()
        y = (y / xyz).toInt()
        z = (z / xyz).toInt()
        return this
    }

    operator fun div(xyz: Int): Vec3i {
        x /= xyz
        y /= xyz
        z /= xyz
        return this
    }

    operator fun div(vector: Vec2d): Vec3i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    operator fun div(vector: Vec2f): Vec3i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    operator fun div(vector: Vec2i): Vec3i {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec3d): Vec3i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        z = (z / vector.z).toInt()
        return this
    }

    operator fun div(vector: Vec3f): Vec3i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        z = (z / vector.z).toInt()
        return this
    }

    operator fun div(vector: Vec3i): Vec3i {
        x /= vector.x
        y /= vector.y
        z /= vector.z
        return this
    }

    fun mul(matrix: Matrix4f): Vec3i {
        return mul(matrix.values)
    }

    fun mul(matrix: FloatArray?): Vec3i {
        set(
            Maths.round((x * matrix!![Matrix4.Companion.m00.toInt()] + y * matrix[Matrix4.Companion.m10.toInt()] + z * matrix[Matrix4.Companion.m20.toInt()] + matrix[Matrix4.Companion.m30.toInt()]).toDouble()),
            Maths.round((x * matrix[Matrix4.Companion.m01.toInt()] + y * matrix[Matrix4.Companion.m11.toInt()] + z * matrix[Matrix4.Companion.m21.toInt()] + matrix[Matrix4.Companion.m31.toInt()]).toDouble()),
            Maths.round((x * matrix[Matrix4.Companion.m02.toInt()] + y * matrix[Matrix4.Companion.m12.toInt()] + z * matrix[Matrix4.Companion.m22.toInt()] + matrix[Matrix4.Companion.m32.toInt()]).toDouble())
        )
        return this
    }

    override fun toString(): String {
        return "$x, $y, $z"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec3i
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z)
    }

    companion object {
        fun crs(a: Vec3i, b: Vec3i): Vec3i {
            return Vec3i(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)
        }

        fun crs(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Vec3i {
            return Vec3i(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2)
        }

        fun dot(a: Vec3i, b: Vec3i): Float {
            return (a.x * b.x + a.y * b.y + a.z * b.z).toFloat()
        }

        fun dot(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Float {
            return (x1 * x2 + y1 * y2 + z1 * z2).toFloat()
        }

        fun len(x: Int, y: Int, z: Int): Float {
            return sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }
    }
}
