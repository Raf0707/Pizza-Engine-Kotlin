package pize.math.vecmath.vector

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix4
import pize.math.vecmath.matrix.Matrix4f
import java.util.*
import kotlin.math.sqrt

open class Vec3f {
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

    constructor(vector: Vec3f?) {
        vector?.let { set(it) }
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

    constructor(p0: Any, p1: Any, p2: Any)

    /**             POINT              */
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

    fun dst(x: Int, y: Int, z: Int): Float {
        val dx = (this.x - x).toDouble()
        val dy = (this.y - y).toDouble()
        val dz = (this.z - z).toDouble()
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

    fun dst(vector: Vec3i): Float {
        val dx = (x - vector.x).toDouble()
        val dy = (y - vector.y).toDouble()
        val dz = (z - vector.z).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy + dz * dz)
    }

    /**             VECTOR              */
    fun len2(): Float {
        return x * x + y * y + z * z
    }

    fun len(): Float {
        return Mathc.sqrt(len2().toDouble())
    }

    fun nor(): Vec3f {
        var len = len2().toDouble()
        if (len == 0.0 || len == 1.0) return this
        len = Maths.invSqrt(len)
        return mul(len)
    }

    fun abs(): Vec3f {
        if (x < 0) x *= -1f
        if (y < 0) y *= -1f
        if (z < 0) z *= -1f
        return this
    }

    fun zero(): Vec3f {
        set(0, 0, 0)
        return this
    }

    val isZero: Boolean
        get() = x == 0f && y == 0f && z == 0f

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

    fun crs(x: Float, y: Float, z: Float): Vec3f {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(x: Double, y: Double, z: Double): Vec3f {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(x: Int, y: Int, z: Int): Vec3f {
        set(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x
        )
        return this
    }

    fun crs(vector: Vec3f): Vec3f {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun crs(vector: Vec3d): Vec3f {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun crs(vector: Vec3i): Vec3f {
        set(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x
        )
        return this
    }

    fun frac(): Vec3f {
        x = Maths.frac(x)
        y = Maths.frac(y)
        z = Maths.frac(z)
        return this
    }

    fun lerp(start: Vec3f, end: Vec3f, t: Float): Vec3f {
        x = Maths.lerp(start.x, end.x, t)
        y = Maths.lerp(start.y, end.y, t)
        z = Maths.lerp(start.z, end.z, t)
        return this
    }

    fun xy(): Vec2f {
        return Vec2f(x, y)
    }

    fun xz(): Vec2f {
        return Vec2f(x, z)
    }

    fun yz(): Vec2f {
        return Vec2f(y, z)
    }

    fun floor(): Vec3f {
        x = Maths.floor(x.toDouble()).toFloat()
        y = Maths.floor(y.toDouble()).toFloat()
        z = Maths.floor(z.toDouble()).toFloat()
        return this
    }

    fun round(): Vec3f {
        x = Maths.round(x.toDouble()).toFloat()
        y = Maths.round(y.toDouble()).toFloat()
        z = Maths.round(z.toDouble()).toFloat()
        return this
    }

    fun ceil(): Vec3f {
        x = Maths.ceil(x.toDouble()).toFloat()
        y = Maths.ceil(y.toDouble()).toFloat()
        z = Maths.ceil(z.toDouble()).toFloat()
        return this
    }

    open fun copy(): Vec3f {
        return Vec3f(this)
    }

    /**             TUPLE              */
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f
    @JvmField
    var z = 0f
    operator fun set(x: Double, y: Double, z: Double): Vec3f {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
        return this
    }

    operator fun set(x: Float, y: Float, z: Float): Vec3f {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    operator fun set(x: Int, y: Int, z: Int): Vec3f {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
        return this
    }

    fun set(xyz: Double): Vec3f {
        x = xyz.toFloat()
        y = xyz.toFloat()
        z = xyz.toFloat()
        return this
    }

    fun set(xyz: Float): Vec3f {
        x = xyz
        y = xyz
        z = xyz
        return this
    }

    fun set(xyz: Int): Vec3f {
        x = xyz.toFloat()
        y = xyz.toFloat()
        z = xyz.toFloat()
        return this
    }

    fun set(vector: Vec2d): Vec3f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        z = 0f
        return this
    }

    fun set(vector: Vec2f): Vec3f {
        x = vector.x
        y = vector.y
        z = 0f
        return this
    }

    fun set(vector: Vec2i): Vec3f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        z = 0f
        return this
    }

    fun set(vector: Vec3d): Vec3f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        z = vector.z.toFloat()
        return this
    }

    fun set(vector: Vec3f): Vec3f {
        x = vector.x
        y = vector.y
        z = vector.z
        return this
    }

    fun set(vector: Vec3i): Vec3f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        z = vector.z.toFloat()
        return this
    }

    fun add(x: Double, y: Double, z: Double): Vec3f {
        this.x += x.toFloat()
        this.y += y.toFloat()
        this.z += z.toFloat()
        return this
    }

    fun add(x: Float, y: Float, z: Float): Vec3f {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(x: Int, y: Int, z: Int): Vec3f {
        this.x += x.toFloat()
        this.y += y.toFloat()
        this.z += z.toFloat()
        return this
    }

    fun add(xyz: Double): Vec3f {
        x += xyz.toFloat()
        y += xyz.toFloat()
        z += xyz.toFloat()
        return this
    }

    fun add(xyz: Float): Vec3f {
        x += xyz
        y += xyz
        z += xyz
        return this
    }

    fun add(xyz: Int): Vec3f {
        x += xyz.toFloat()
        y += xyz.toFloat()
        z += xyz.toFloat()
        return this
    }

    fun add(vector: Vec2d): Vec3f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        return this
    }

    fun add(vector: Vec2f): Vec3f {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec2i): Vec3f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        return this
    }

    fun add(vector: Vec3d): Vec3f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        z += vector.z.toFloat()
        return this
    }

    fun add(vector: Vec3f): Vec3f {
        x += vector.x
        y += vector.y
        z += vector.z
        return this
    }

    fun add(vector: Vec3i): Vec3f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        z += vector.z.toFloat()
        return this
    }

    fun sub(x: Double, y: Double, z: Double): Vec3f {
        this.x -= x.toFloat()
        this.y -= y.toFloat()
        this.z -= z.toFloat()
        return this
    }

    fun sub(x: Float, y: Float, z: Float): Vec3f {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(x: Int, y: Int, z: Int): Vec3f {
        this.x -= x.toFloat()
        this.y -= y.toFloat()
        this.z -= z.toFloat()
        return this
    }

    fun sub(xyz: Double): Vec3f {
        x -= xyz.toFloat()
        y -= xyz.toFloat()
        z -= xyz.toFloat()
        return this
    }

    fun sub(xyz: Float): Vec3f {
        x -= xyz
        y -= xyz
        z -= xyz
        return this
    }

    fun sub(xyz: Int): Vec3f {
        x -= xyz.toFloat()
        y -= xyz.toFloat()
        z -= xyz.toFloat()
        return this
    }

    fun sub(vector: Vec2d): Vec3f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        return this
    }

    fun sub(vector: Vec2f): Vec3f {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec2i): Vec3f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        return this
    }

    fun sub(vector: Vec3d): Vec3f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        z -= vector.z.toFloat()
        return this
    }

    fun sub(vector: Vec3f): Vec3f {
        x -= vector.x
        y -= vector.y
        z -= vector.z
        return this
    }

    fun sub(vector: Vec3i): Vec3f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        z -= vector.z.toFloat()
        return this
    }

    fun mul(x: Double, y: Double, z: Double): Vec3f {
        this.x *= x.toFloat()
        this.y *= y.toFloat()
        this.z *= z.toFloat()
        return this
    }

    fun mul(x: Int, y: Int, z: Int): Vec3f {
        this.x *= x.toFloat()
        this.y *= y.toFloat()
        this.z *= z.toFloat()
        return this
    }

    fun mul(x: Float, y: Float, z: Float): Vec3f {
        this.x *= x
        this.y *= y
        this.z *= z
        return this
    }

    fun mul(xyz: Double): Vec3f {
        x *= xyz.toFloat()
        y *= xyz.toFloat()
        z *= xyz.toFloat()
        return this
    }

    fun mul(xyz: Float): Vec3f {
        x *= xyz
        y *= xyz
        z *= xyz
        return this
    }

    fun mul(xyz: Int): Vec3f {
        x *= xyz.toFloat()
        y *= xyz.toFloat()
        z *= xyz.toFloat()
        return this
    }

    fun mul(vector: Vec2d): Vec3f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        return this
    }

    fun mul(vector: Vec2f): Vec3f {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec2i): Vec3f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        return this
    }

    fun mul(vector: Vec3d): Vec3f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        z *= vector.z.toFloat()
        return this
    }

    fun mul(vector: Vec3f): Vec3f {
        x *= vector.x
        y *= vector.y
        z *= vector.z
        return this
    }

    fun mul(vector: Vec3i): Vec3f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        z *= vector.z.toFloat()
        return this
    }

    fun div(x: Double, y: Double, z: Double): Vec3f {
        this.x /= x.toFloat()
        this.y /= y.toFloat()
        this.z /= z.toFloat()
        return this
    }

    fun div(x: Float, y: Float, z: Float): Vec3f {
        this.x /= x
        this.y /= y
        this.z /= z
        return this
    }

    fun div(x: Int, y: Int, z: Int): Vec3f {
        this.x /= x.toFloat()
        this.y /= y.toFloat()
        this.z /= z.toFloat()
        return this
    }

    operator fun div(xyz: Double): Vec3f {
        x /= xyz.toFloat()
        y /= xyz.toFloat()
        z /= xyz.toFloat()
        return this
    }

    operator fun div(xyz: Float): Vec3f {
        x /= xyz
        y /= xyz
        z /= xyz
        return this
    }

    operator fun div(xyz: Int): Vec3f {
        x /= xyz.toFloat()
        y /= xyz.toFloat()
        z /= xyz.toFloat()
        return this
    }

    operator fun div(vector: Vec2d): Vec3f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        return this
    }

    operator fun div(vector: Vec2f): Vec3f {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec2i): Vec3f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        return this
    }

    operator fun div(vector: Vec3d): Vec3f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        z /= vector.z.toFloat()
        return this
    }

    operator fun div(vector: Vec3f): Vec3f {
        x /= vector.x
        y /= vector.y
        z /= vector.z
        return this
    }

    operator fun div(vector: Vec3i): Vec3f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        z /= vector.z.toFloat()
        return this
    }

    fun mul(matrix: Matrix4f): Vec3f {
        return mul(matrix.values)
    }

    fun mul(matrix: FloatArray?): Vec3f {
        set(
            x * matrix!![Matrix4.Companion.m00.toInt()] + y * matrix[Matrix4.Companion.m10.toInt()] + z * matrix[Matrix4.Companion.m20.toInt()] + matrix[Matrix4.Companion.m30.toInt()],
            x * matrix[Matrix4.Companion.m01.toInt()] + y * matrix[Matrix4.Companion.m11.toInt()] + z * matrix[Matrix4.Companion.m21.toInt()] + matrix[Matrix4.Companion.m31.toInt()],
            x * matrix[Matrix4.Companion.m02.toInt()] + y * matrix[Matrix4.Companion.m12.toInt()] + z * matrix[Matrix4.Companion.m22.toInt()] + matrix[Matrix4.Companion.m32.toInt()]
        )
        return this
    }

    fun x(): Int {
        return Maths.round(x.toDouble())
    }

    fun y(): Int {
        return Maths.round(y.toDouble())
    }

    fun z(): Int {
        return Maths.round(z.toDouble())
    }

    fun xf(): Int {
        return Maths.floor(x.toDouble())
    }

    fun yf(): Int {
        return Maths.floor(y.toDouble())
    }

    fun zf(): Int {
        return Maths.floor(z.toDouble())
    }

    fun xc(): Int {
        return Maths.ceil(x.toDouble())
    }

    fun yc(): Int {
        return Maths.ceil(y.toDouble())
    }

    fun zc(): Int {
        return Maths.ceil(z.toDouble())
    }

    override fun toString(): String {
        return "$x, $y, $z"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec3f
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z)
    }

    companion object {
        fun crs(a: Vec3f?, b: Vec3f?): Vec3f {
            return Vec3f(a!!.y * b!!.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)
        }

        fun crs(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Vec3f {
            return Vec3f(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2, x1 * y2 - y1 * x2)
        }

        fun dot(a: Vec3f, b: Vec3f): Float {
            return a.x * b.x + a.y * b.y + a.z * b.z
        }

        fun dot(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Float {
            return x1 * x2 + y1 * y2 + z1 * z2
        }

        fun len(x: Float, y: Float, z: Float): Float {
            return sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }
    }
}
