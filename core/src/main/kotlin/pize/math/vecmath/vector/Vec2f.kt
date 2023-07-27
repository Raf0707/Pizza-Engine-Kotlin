package pize.math.vecmath.vector

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix3
import pize.math.vecmath.matrix.Matrix3f
import java.util.*

open class Vec2f {
    constructor()
    constructor(x: Double, y: Double) {
        set(x, y)
    }

    constructor(x: Float, y: Float) {
        set(x, y)
    }

    constructor(xy: Double) {
        set(xy)
    }

    constructor(xy: Float) {
        set(xy)
    }

    constructor(vector: Vec2d) {
        set(vector)
    }

    constructor(vector: Vec2f?) {
        set(vector)
    }

    constructor(vector: Vec3d) {
        set(vector)
    }

    constructor(vector: Vec3f) {
        set(vector)
    }

    /**             POINT              */
    fun dst(x: Int, y: Int): Float {
        val dx = (this.x - x).toDouble()
        val dy = (this.y - y).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    fun dst(x: Double, y: Double): Float {
        val dx = this.x - x
        val dy = this.y - y
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    fun dst(x: Float, y: Float): Float {
        val dx = (this.x - x).toDouble()
        val dy = (this.y - y).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2i): Float {
        val dx = (x - vector.x).toDouble()
        val dy = (y - vector.y).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2d): Float {
        val dx = x - vector.x
        val dy = y - vector.y
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2f): Float {
        val dx = (x - vector.x).toDouble()
        val dy = (y - vector.y).toDouble()
        return Mathc.sqrt(dx * dx + dy * dy)
    }

    /**             VECTOR              */
    fun len2(): Float {
        return x * x + y * y
    }

    fun len(): Float {
        return Mathc.sqrt(len2().toDouble())
    }

    fun nor(): Vec2f {
        var len = len2().toDouble()
        if (len == 0.0 || len == 1.0) return this
        len = Maths.invSqrt(len)
        return mul(len)
    }

    fun abs(): Vec2f {
        if (x < 0) x *= -1f
        if (y < 0) y *= -1f
        return this
    }

    fun zero(): Vec2f {
        set(0f, 0f)
        return this
    }

    val isZero: Boolean
        get() = x == 0f && y == 0f

    fun dot(x: Float, y: Float): Float {
        return this.x * x + this.y * y
    }

    fun dot(x: Double, y: Double): Double {
        return this.x * x + this.y * y
    }

    fun dot(vector: Vec2f): Float {
        return x * vector.x + y * vector.y
    }

    fun dot(vector: Vec2d): Double {
        return x * vector.x + y * vector.y
    }

    fun crs(x: Float, y: Float): Float {
        return this.x * y - this.y * x
    }

    fun crs(x: Double, y: Double): Double {
        return this.x * y - this.y * x
    }

    fun crs(vector: Vec2f): Float {
        return x * vector.y - y * vector.x
    }

    fun crs(vector: Vec2d): Double {
        return x * vector.y - y * vector.x
    }

    fun crs(): Vec2f {
        return Vec2f(y, -x)
    }

    fun frac(): Vec2f {
        x = Maths.frac(x)
        y = Maths.frac(y)
        return this
    }

    fun floor(): Vec2f {
        x = Maths.floor(x.toDouble()).toFloat()
        y = Maths.floor(y.toDouble()).toFloat()
        return this
    }

    fun round(): Vec2f {
        x = Maths.round(x.toDouble()).toFloat()
        y = Maths.round(y.toDouble()).toFloat()
        return this
    }

    fun ceil(): Vec2f {
        x = Maths.ceil(x.toDouble()).toFloat()
        y = Maths.ceil(y.toDouble()).toFloat()
        return this
    }

    fun deg(vector: Vec2f): Float {
        return rad(vector) * Maths.ToDeg
    }

    fun rad(vector: Vec2f): Float {
        val cos = (dot(vector) / (len() * vector.len())).toDouble()
        return Mathc.acos(Maths.clamp(cos, -1.0, 1.0))
    }

    fun rotDeg(degrees: Double): Vec2f {
        return rotRad(degrees * Maths.ToRad)
    }

    fun rotRad(radians: Double): Vec2f {
        val cos = Mathc.cos(radians)
        val sin = Mathc.sin(radians)
        set(x * cos - y * sin, x * sin + y * cos)
        return this
    }

    open fun copy(): Vec2f {
        return Vec2f(this)
    }

    /**             TUPLE              */
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f
    operator fun set(x: Double, y: Double): Vec2f {
        this.x = x.toFloat()
        this.y = y.toFloat()
        return this
    }

    operator fun set(x: Float, y: Float): Vec2f {
        this.x = x
        this.y = y
        return this
    }

    fun set(xy: Double): Vec2f {
        x = xy.toFloat()
        y = xy.toFloat()
        return this
    }

    fun set(xy: Float): Vec2f {
        x = xy
        y = xy
        return this
    }

    fun set(vector: Vec2d): Vec2f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        return this
    }

    fun set(vector: Vec2f): Vec2f {
        x = vector.x
        y = vector.y
        return this
    }

    fun set(vector: Vec2i): Vec2f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        return this
    }

    fun set(vector: Vec3d): Vec2f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        return this
    }

    fun set(vector: Vec3f): Vec2f {
        x = vector.x
        y = vector.y
        return this
    }

    fun set(vector: Vec3i): Vec2f {
        x = vector.x.toFloat()
        y = vector.y.toFloat()
        return this
    }

    fun add(x: Double, y: Double): Vec2f {
        this.x += x.toFloat()
        this.y += y.toFloat()
        return this
    }

    fun add(x: Float, y: Float): Vec2f {
        this.x += x
        this.y += y
        return this
    }

    fun add(xyz: Double): Vec2f {
        x += xyz.toFloat()
        y += xyz.toFloat()
        return this
    }

    fun add(xy: Float): Vec2f {
        x += xy
        y += xy
        return this
    }

    fun add(vector: Vec2d): Vec2f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        return this
    }

    fun add(vector: Vec2f): Vec2f {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec3d): Vec2f {
        x += vector.x.toFloat()
        y += vector.y.toFloat()
        return this
    }

    fun add(vector: Vec3f): Vec2f {
        x += vector.x
        y += vector.y
        return this
    }

    fun sub(x: Double, y: Double): Vec2f {
        this.x -= x.toFloat()
        this.y -= y.toFloat()
        return this
    }

    fun sub(x: Float, y: Float): Vec2f {
        this.x -= x
        this.y -= y
        return this
    }

    fun sub(xy: Double): Vec2f {
        x -= xy.toFloat()
        y -= xy.toFloat()
        return this
    }

    fun sub(xy: Float): Vec2f {
        x -= xy
        y -= xy
        return this
    }

    fun sub(vector: Vec2d): Vec2f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        return this
    }

    fun sub(vector: Vec2f): Vec2f {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec3d): Vec2f {
        x -= vector.x.toFloat()
        y -= vector.y.toFloat()
        return this
    }

    fun sub(vector: Vec3f): Vec2f {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun mul(x: Double, y: Double): Vec2f {
        this.x *= x.toFloat()
        this.y *= y.toFloat()
        return this
    }

    fun mul(x: Float, y: Float): Vec2f {
        this.x *= x
        this.y *= y
        return this
    }

    fun mul(xy: Double): Vec2f {
        x *= xy.toFloat()
        y *= xy.toFloat()
        return this
    }

    fun mul(xy: Float): Vec2f {
        x *= xy
        y *= xy
        return this
    }

    fun mul(vector: Vec2d): Vec2f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        return this
    }

    fun mul(vector: Vec2f): Vec2f {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec3d): Vec2f {
        x *= vector.x.toFloat()
        y *= vector.y.toFloat()
        return this
    }

    fun mul(vector: Vec3f): Vec2f {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun div(x: Double, y: Double): Vec2f {
        this.x /= x.toFloat()
        this.y /= y.toFloat()
        return this
    }

    fun div(x: Float, y: Float): Vec2f {
        this.x /= x
        this.y /= y
        return this
    }

    operator fun div(xy: Double): Vec2f {
        x /= xy.toFloat()
        y /= xy.toFloat()
        return this
    }

    operator fun div(xy: Float): Vec2f {
        x /= xy
        y /= xy
        return this
    }

    operator fun div(vector: Vec2d): Vec2f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        return this
    }

    operator fun div(vector: Vec2f): Vec2f {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec3d): Vec2f {
        x /= vector.x.toFloat()
        y /= vector.y.toFloat()
        return this
    }

    operator fun div(vector: Vec3f): Vec2f {
        x /= vector.x
        y /= vector.y
        return this
    }

    fun mul(matrix: Matrix3f): Vec2f {
        set(
            x * matrix.`val`[Matrix3.Companion.m00.toInt()] + y * matrix.`val`[Matrix3.Companion.m01.toInt()],
            x * matrix.`val`[Matrix3.Companion.m10.toInt()] + y * matrix.`val`[Matrix3.Companion.m11.toInt()]
        )
        return this
    }

    fun x(): Int {
        return Maths.round(x.toDouble())
    }

    fun y(): Int {
        return Maths.round(y.toDouble())
    }

    fun xf(): Int {
        return Maths.floor(x.toDouble())
    }

    fun yf(): Int {
        return Maths.floor(y.toDouble())
    }

    fun xc(): Int {
        return Maths.ceil(x.toDouble())
    }

    fun yc(): Int {
        return Maths.ceil(y.toDouble())
    }

    override fun toString(): String {
        return "$x, $y"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec2f
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    companion object {
        fun crs(a: Vec2f, b: Vec2f): Float {
            return a.x * b.y - a.y * b.x
        }

        fun crs(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return x1 * y2 - y1 * x2
        }

        fun dot(a: Vec2f, b: Vec2f): Float {
            return a.x * b.x + a.y * b.y
        }

        fun dot(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return x1 * x2 + y1 * y2
        }

        @JvmStatic
        fun len(x: Double, y: Double): Float {
            return Mathc.sqrt(x * x + y * y)
        }
    }
}
