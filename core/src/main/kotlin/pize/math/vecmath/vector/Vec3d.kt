package pize.math.vecmath.vector

import pize.math.Maths
import pize.math.vecmath.matrix.Matrix3
import pize.math.vecmath.matrix.Matrix4f
import java.util.*
import kotlin.math.sqrt

class Vec3d {
    constructor()
    constructor(x: Double, y: Double, z: Double) {
        set(x, y, z)
    }

    constructor(x: Float, y: Float, z: Float) {
        set(x, y, z)
    }

    constructor(x: Int, y: Int, z: Int) {
        set(x, y, z)
    }

    constructor(xyz: Double) {
        set(xyz)
    }

    constructor(xyz: Float) {
        set(xyz)
    }

    constructor(xyz: Int) {
        set(xyz)
    }

    constructor(vector: Vec3d) {
        set(vector)
    }

    constructor(vector: Vec3f) {
        set(vector)
    }

    constructor(vector: Vec3i) {
        set(vector)
    }

    constructor(vector: Vec2d) {
        set(vector)
    }

    constructor(vector: Vec2f) {
        set(vector)
    }

    constructor(vector: Vec2i) {
        set(vector)
    }

    /**             POINT              */
    fun dst(x: Float, y: Float, z: Float): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(x: Double, y: Double, z: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(x: Int, y: Int, z: Int): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3f): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        val dz = z - vector.z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3d): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        val dz = z - vector.z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun dst(vector: Vec3i): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        val dz = z - vector.z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    /**             VECTOR              */
    fun len2(): Double {
        return x * x + y * y + z * z
    }

    fun len(): Double {
        return sqrt(len2())
    }

    fun nor(): Vec3d {
        var len = len2()
        if (len == 0.0 || len == 1.0) return this
        len = Maths.invSqrt(len)
        return mul(len)
    }

    fun abs(): Vec3d {
        if (x < 0) x *= -1.0
        if (y < 0) y *= -1.0
        if (z < 0) z *= -1.0
        return this
    }

    fun zero(): Vec3d {
        set(0, 0, 0)
        return this
    }

    val isZero: Boolean
        get() = x == 0.0 && y == 0.0 && z == 0.0

    fun dot(x: Float, y: Float, z: Float): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dot(x: Double, y: Double, z: Double): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dot(x: Int, y: Int, z: Int): Double {
        return this.x * x + this.y * y + this.z * z
    }

    fun dot(vector: Vec3f): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun dot(vector: Vec3d): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun dot(vector: Vec3i): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun crs(x: Float, y: Float, z: Float): Vec3d {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(x: Double, y: Double, z: Double): Vec3d {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(x: Int, y: Int, z: Int): Vec3d {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(vector: Vec3f): Vec3d {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun crs(vector: Vec3d): Vec3d {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun crs(vector: Vec3i): Vec3d {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun frac(): Vec3d {
        x = Maths.frac(x)
        y = Maths.frac(y)
        z = Maths.frac(z)
        return this
    }

    fun xy(): Vec2d {
        return Vec2d(x, y)
    }

    fun xz(): Vec2d {
        return Vec2d(x, z)
    }

    fun yz(): Vec2d {
        return Vec2d(y, z)
    }

    fun floor(): Vec3d {
        x = Maths.floor(x).toDouble()
        y = Maths.floor(y).toDouble()
        z = Maths.floor(z).toDouble()
        return this
    }

    fun round(): Vec3d {
        x = Maths.round(x).toDouble()
        y = Maths.round(y).toDouble()
        z = Maths.round(z).toDouble()
        return this
    }

    fun ceil(): Vec3d {
        x = Maths.ceil(x).toDouble()
        y = Maths.ceil(y).toDouble()
        z = Maths.ceil(z).toDouble()
        return this
    }

    fun copy(): Vec3d {
        return Vec3d(this)
    }

    /**             TUPLE              */
    var x = 0.0
    var y = 0.0
    var z = 0.0
    operator fun set(x: Double, y: Double, z: Double): Vec3d {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    operator fun set(x: Float, y: Float, z: Float): Vec3d {
        this.x = x.toDouble()
        this.y = y.toDouble()
        this.z = z.toDouble()
        return this
    }

    operator fun set(x: Int, y: Int, z: Int): Vec3d {
        this.x = x.toDouble()
        this.y = y.toDouble()
        this.z = z.toDouble()
        return this
    }

    fun set(xyz: Double): Vec3d {
        x = xyz
        y = xyz
        z = xyz
        return this
    }

    fun set(xyz: Float): Vec3d {
        x = xyz.toDouble()
        y = xyz.toDouble()
        z = xyz.toDouble()
        return this
    }

    fun set(xyz: Int): Vec3d {
        x = xyz.toDouble()
        y = xyz.toDouble()
        z = xyz.toDouble()
        return this
    }

    fun set(vector: Vec2d): Vec3d {
        x = vector.x
        y = vector.y
        z = 0.0
        return this
    }

    fun set(vector: Vec2f): Vec3d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        z = 0.0
        return this
    }

    fun set(vector: Vec2i): Vec3d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        z = 0.0
        return this
    }

    fun set(vector: Vec3d): Vec3d {
        x = vector.x
        y = vector.y
        z = vector.z
        return this
    }

    fun set(vector: Vec3f): Vec3d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        z = vector.z.toDouble()
        return this
    }

    fun set(vector: Vec3i): Vec3d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        z = vector.z.toDouble()
        return this
    }

    fun add(x: Double, y: Double, z: Double): Vec3d {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(x: Float, y: Float, z: Float): Vec3d {
        this.x += x.toDouble()
        this.y += y.toDouble()
        this.z += z.toDouble()
        return this
    }

    fun add(x: Int, y: Int, z: Int): Vec3d {
        this.x += x.toDouble()
        this.y += y.toDouble()
        this.z += z.toDouble()
        return this
    }

    fun add(xyz: Double): Vec3d {
        x += xyz
        y += xyz
        z += xyz
        return this
    }

    fun add(xyz: Float): Vec3d {
        x += xyz.toDouble()
        y += xyz.toDouble()
        z += xyz.toDouble()
        return this
    }

    fun add(xyz: Int): Vec3d {
        x += xyz.toDouble()
        y += xyz.toDouble()
        z += xyz.toDouble()
        return this
    }

    fun add(vector: Vec2d): Vec3d {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec2f): Vec3d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        return this
    }

    fun add(vector: Vec2i): Vec3d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        return this
    }

    fun add(vector: Vec3d): Vec3d {
        x += vector.x
        y += vector.y
        z += vector.z
        return this
    }

    fun add(vector: Vec3f): Vec3d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        z += vector.z.toDouble()
        return this
    }

    fun add(vector: Vec3i): Vec3d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        z += vector.z.toDouble()
        return this
    }

    fun sub(x: Double, y: Double, z: Double): Vec3d {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(x: Float, y: Float, z: Float): Vec3d {
        this.x -= x.toDouble()
        this.y -= y.toDouble()
        this.z -= z.toDouble()
        return this
    }

    fun sub(x: Int, y: Int, z: Int): Vec3d {
        this.x -= x.toDouble()
        this.y -= y.toDouble()
        this.z -= z.toDouble()
        return this
    }

    fun sub(xyz: Double): Vec3d {
        x -= xyz
        y -= xyz
        z -= xyz
        return this
    }

    fun sub(xyz: Float): Vec3d {
        x -= xyz.toDouble()
        y -= xyz.toDouble()
        z -= xyz.toDouble()
        return this
    }

    fun sub(xyz: Int): Vec3d {
        x -= xyz.toDouble()
        y -= xyz.toDouble()
        z -= xyz.toDouble()
        return this
    }

    fun sub(vector: Vec2d): Vec3d {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec2f): Vec3d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        return this
    }

    fun sub(vector: Vec2i): Vec3d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        return this
    }

    fun sub(vector: Vec3d): Vec3d {
        x -= vector.x
        y -= vector.y
        z -= vector.z
        return this
    }

    fun sub(vector: Vec3f): Vec3d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        z -= vector.z.toDouble()
        return this
    }

    fun sub(vector: Vec3i): Vec3d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        z -= vector.z.toDouble()
        return this
    }

    fun mul(x: Double, y: Double, z: Double): Vec3d {
        this.x *= x
        this.y *= y
        this.z *= z
        return this
    }

    fun mul(x: Float, y: Float, z: Float): Vec3d {
        this.x *= x.toDouble()
        this.y *= y.toDouble()
        this.z *= z.toDouble()
        return this
    }

    fun mul(x: Int, y: Int, z: Int): Vec3d {
        this.x *= x.toDouble()
        this.y *= y.toDouble()
        this.z *= z.toDouble()
        return this
    }

    fun mul(xyz: Double): Vec3d {
        x *= xyz
        y *= xyz
        z *= xyz
        return this
    }

    fun mul(xyz: Float): Vec3d {
        x *= xyz.toDouble()
        y *= xyz.toDouble()
        z *= xyz.toDouble()
        return this
    }

    fun mul(xyz: Int): Vec3d {
        x *= xyz.toDouble()
        y *= xyz.toDouble()
        z *= xyz.toDouble()
        return this
    }

    fun mul(vector: Vec2d): Vec3d {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec2f): Vec3d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        return this
    }

    fun mul(vector: Vec2i): Vec3d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        return this
    }

    fun mul(vector: Vec3d): Vec3d {
        x *= vector.x
        y *= vector.y
        z *= vector.z
        return this
    }

    fun mul(vector: Vec3f): Vec3d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        z *= vector.z.toDouble()
        return this
    }

    fun mul(vector: Vec3i): Vec3d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        z *= vector.z.toDouble()
        return this
    }

    fun div(x: Double, y: Double, z: Double): Vec3d {
        this.x /= x
        this.y /= y
        this.z /= z
        return this
    }

    fun div(x: Float, y: Float, z: Float): Vec3d {
        this.x /= x.toDouble()
        this.y /= y.toDouble()
        this.z /= z.toDouble()
        return this
    }

    fun div(x: Int, y: Int, z: Int): Vec3d {
        this.x /= x.toDouble()
        this.y /= y.toDouble()
        this.z /= z.toDouble()
        return this
    }

    operator fun div(xyz: Double): Vec3d {
        x /= xyz
        y /= xyz
        z /= xyz
        return this
    }

    operator fun div(xyz: Float): Vec3d {
        x /= xyz.toDouble()
        y /= xyz.toDouble()
        z /= xyz.toDouble()
        return this
    }

    operator fun div(xyz: Int): Vec3d {
        x /= xyz.toDouble()
        y /= xyz.toDouble()
        z /= xyz.toDouble()
        return this
    }

    operator fun div(vector: Vec2d): Vec3d {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec2f): Vec3d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        return this
    }

    operator fun div(vector: Vec2i): Vec3d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        return this
    }

    operator fun div(vector: Vec3d): Vec3d {
        x /= vector.x
        y /= vector.y
        z /= vector.z
        return this
    }

    operator fun div(vector: Vec3f): Vec3d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        z /= vector.z.toDouble()
        return this
    }

    operator fun div(vector: Vec3i): Vec3d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        z /= vector.z.toDouble()
        return this
    }

    fun mul(matrix: Matrix4f): Vec3d {
        set(
            x * matrix.`val`[Matrix3.Companion.m00.toInt()] + y * matrix.`val`[Matrix3.Companion.m01.toInt()] + z * matrix.`val`[Matrix3.Companion.m02.toInt()],
            x * matrix.`val`[Matrix3.Companion.m10.toInt()] + y * matrix.`val`[Matrix3.Companion.m11.toInt()] + z * matrix.`val`[Matrix3.Companion.m12.toInt()],
            x * matrix.`val`[Matrix3.Companion.m10.toInt()] + y * matrix.`val`[Matrix3.Companion.m11.toInt()] + z * matrix.`val`[Matrix3.Companion.m12.toInt()]
        )
        return this
    }

    fun x(): Int {
        return Maths.round(x)
    }

    fun y(): Int {
        return Maths.round(y)
    }

    fun z(): Int {
        return Maths.round(z)
    }

    fun xf(): Int {
        return Maths.floor(x)
    }

    fun yf(): Int {
        return Maths.floor(y)
    }

    fun zf(): Int {
        return Maths.floor(z)
    }

    fun xc(): Int {
        return Maths.ceil(x)
    }

    fun yc(): Int {
        return Maths.ceil(y)
    }

    fun zc(): Int {
        return Maths.ceil(z)
    }

    override fun toString(): String {
        return "$x, $y, $z"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec3d
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z)
    }

    companion object {
        fun crs(a: Vec3d, b: Vec3d): Vec3d {
            return Vec3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)
        }

        fun crs(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Vec3d {
            return Vec3d(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2)
        }

        fun dot(a: Vec3d, b: Vec3d): Double {
            return a.x * b.x + a.y * b.y + a.z * b.z
        }

        fun dot(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
            return x1 * x2 + y1 * y2 + z1 * z2
        }

        fun len(x: Double, y: Double, z: Double): Double {
            return sqrt(x * x + y * y + z * z)
        }
    }
}
