package pize.math.vecmath.vector

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix3
import pize.math.vecmath.matrix.Matrix3f
import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

class Vec2d {
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

    constructor(vector: Vec2f) {
        set(vector)
    }

    constructor(vector: Vec3d) {
        set(vector)
    }

    constructor(vector: Vec3f) {
        set(vector)
    }

    /**             POINT              */
    fun dst(x: Int, y: Int): Double {
        val dx = this.x - x
        val dy = this.y - y
        return sqrt(dx * dx + dy * dy)
    }

    fun dst(x: Double, y: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        return sqrt(dx * dx + dy * dy)
    }

    fun dst(x: Float, y: Float): Double {
        val dx = this.x - x
        val dy = this.y - y
        return sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2i): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        return sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2d): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        return sqrt(dx * dx + dy * dy)
    }

    fun dst(vector: Vec2f): Double {
        val dx = x - vector.x
        val dy = y - vector.y
        return sqrt(dx * dx + dy * dy)
    }

    /**             VECTOR              */
    fun len2(): Double {
        return x * x + y * y
    }

    fun len(): Double {
        return sqrt(len2())
    }

    fun nor(): Vec2d {
        var len = len2()
        if (len == 0.0 || len == 1.0) return this
        len = Maths.invSqrt(len)
        return mul(len)
    }

    fun abs(): Vec2d {
        if (x < 0) x *= -1.0
        if (y < 0) y *= -1.0
        return this
    }

    fun zero(): Vec2d {
        set(0f, 0f)
        return this
    }

    val isZero: Boolean
        get() = x == 0.0 && y == 0.0

    fun dot(x: Float, y: Float): Double {
        return this.x * x + this.y * y
    }

    fun dot(x: Double, y: Double): Double {
        return this.x * x + this.y * y
    }

    fun dot(vector: Vec2f): Double {
        return x * vector.x + y * vector.y
    }

    fun dot(vector: Vec2d): Double {
        return x * vector.x + y * vector.y
    }

    fun crs(x: Float, y: Float): Double {
        return this.x * y - this.y * x
    }

    fun crs(x: Double, y: Double): Double {
        return this.x * y - this.y * x
    }

    fun crs(vector: Vec2f): Double {
        return x * vector.y - y * vector.x
    }

    fun crs(vector: Vec2d): Double {
        return x * vector.y - y * vector.x
    }

    fun crs(): Vec2d {
        return Vec2d(y, -x)
    }

    fun frac(): Vec2d {
        x = Maths.frac(x)
        y = Maths.frac(y)
        return this
    }

    fun floor(): Vec2d {
        x = Maths.floor(x).toDouble()
        y = Maths.floor(y).toDouble()
        return this
    }

    fun round(): Vec2d {
        x = Maths.round(x).toDouble()
        y = Maths.round(y).toDouble()
        return this
    }

    fun ceil(): Vec2d {
        x = Maths.ceil(x).toDouble()
        y = Maths.ceil(y).toDouble()
        return this
    }

    fun deg(vector: Vec2d): Double {
        return rad(vector) * Maths.ToDeg
    }

    fun rad(vector: Vec2d): Double {
        val cos = dot(vector) / (len() * vector.len())
        return acos(Maths.clamp(cos, -1.0, 1.0))
    }

    fun rotDeg(deg: Double): Vec2d {
        return rotRad(deg * Maths.ToRad)
    }

    fun rotRad(rad: Double): Vec2d {
        val cos = Mathc.cos(rad)
        val sin = Mathc.sin(rad)
        set(x * cos - y * sin, x * sin + y * cos)
        return this
    }

    fun copy(): Vec2d {
        return Vec2d(this)
    }

    /**             TUPLE              */
    var x = 0.0
    var y = 0.0
    operator fun set(x: Double, y: Double): Vec2d {
        this.x = x
        this.y = y
        return this
    }

    operator fun set(x: Float, y: Float): Vec2d {
        this.x = x.toDouble()
        this.y = y.toDouble()
        return this
    }

    fun set(xy: Double): Vec2d {
        x = xy
        y = xy
        return this
    }

    fun set(xy: Float): Vec2d {
        x = xy.toDouble()
        y = xy.toDouble()
        return this
    }

    fun set(vector: Vec2d): Vec2d {
        x = vector.x
        y = vector.y
        return this
    }

    fun set(vector: Vec2f): Vec2d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        return this
    }

    fun set(vector: Vec2i): Vec2d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        return this
    }

    fun set(vector: Vec3d): Vec2d {
        x = vector.x
        y = vector.y
        return this
    }

    fun set(vector: Vec3f): Vec2d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        return this
    }

    fun set(vector: Vec3i): Vec2d {
        x = vector.x.toDouble()
        y = vector.y.toDouble()
        return this
    }

    fun add(x: Double, y: Double): Vec2d {
        this.x += x
        this.y += y
        return this
    }

    fun add(x: Float, y: Float): Vec2d {
        this.x += x.toDouble()
        this.y += y.toDouble()
        return this
    }

    fun add(xyz: Double): Vec2d {
        x += xyz
        y += xyz
        return this
    }

    fun add(xy: Float): Vec2d {
        x += xy.toDouble()
        y += xy.toDouble()
        return this
    }

    fun add(vector: Vec2d): Vec2d {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec2f): Vec2d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        return this
    }

    fun add(vector: Vec3d): Vec2d {
        x += vector.x
        y += vector.y
        return this
    }

    fun add(vector: Vec3f): Vec2d {
        x += vector.x.toDouble()
        y += vector.y.toDouble()
        return this
    }

    fun sub(x: Double, y: Double): Vec2d {
        this.x -= x
        this.y -= y
        return this
    }

    fun sub(x: Float, y: Float): Vec2d {
        this.x -= x.toDouble()
        this.y -= y.toDouble()
        return this
    }

    fun sub(xy: Double): Vec2d {
        x -= xy
        y -= xy
        return this
    }

    fun sub(xy: Float): Vec2d {
        x -= xy.toDouble()
        y -= xy.toDouble()
        return this
    }

    fun sub(vector: Vec2d): Vec2d {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec2f): Vec2d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        return this
    }

    fun sub(vector: Vec3d): Vec2d {
        x -= vector.x
        y -= vector.y
        return this
    }

    fun sub(vector: Vec3f): Vec2d {
        x -= vector.x.toDouble()
        y -= vector.y.toDouble()
        return this
    }

    fun mul(x: Double, y: Double): Vec2d {
        this.x *= x
        this.y *= y
        return this
    }

    fun mul(x: Float, y: Float): Vec2d {
        this.x *= x.toDouble()
        this.y *= y.toDouble()
        return this
    }

    fun mul(xy: Double): Vec2d {
        x *= xy
        y *= xy
        return this
    }

    fun mul(xy: Float): Vec2d {
        x *= xy.toDouble()
        y *= xy.toDouble()
        return this
    }

    fun mul(vector: Vec2d): Vec2d {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec2f): Vec2d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        return this
    }

    fun mul(vector: Vec3d): Vec2d {
        x *= vector.x
        y *= vector.y
        return this
    }

    fun mul(vector: Vec3f): Vec2d {
        x *= vector.x.toDouble()
        y *= vector.y.toDouble()
        return this
    }

    fun div(x: Double, y: Double): Vec2d {
        this.x /= x
        this.y /= y
        return this
    }

    fun div(x: Float, y: Float): Vec2d {
        this.x /= x.toDouble()
        this.y /= y.toDouble()
        return this
    }

    operator fun div(xy: Double): Vec2d {
        x /= xy
        y /= xy
        return this
    }

    operator fun div(xy: Float): Vec2d {
        x /= xy.toDouble()
        y /= xy.toDouble()
        return this
    }

    operator fun div(vector: Vec2d): Vec2d {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec2f): Vec2d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        return this
    }

    operator fun div(vector: Vec3d): Vec2d {
        x /= vector.x
        y /= vector.y
        return this
    }

    operator fun div(vector: Vec3f): Vec2d {
        x /= vector.x.toDouble()
        y /= vector.y.toDouble()
        return this
    }

    fun mul(matrix: Matrix3f): Vec2d {
        set(
            x * matrix.`val`[Matrix3.Companion.m00.toInt()] + y * matrix.`val`[Matrix3.Companion.m01.toInt()],
            x * matrix.`val`[Matrix3.Companion.m10.toInt()] + y * matrix.`val`[Matrix3.Companion.m11.toInt()]
        )
        return this
    }

    fun x(): Int {
        return Maths.round(x)
    }

    fun y(): Int {
        return Maths.round(y)
    }

    fun xf(): Int {
        return Maths.floor(x)
    }

    fun yf(): Int {
        return Maths.floor(y)
    }

    fun xc(): Int {
        return Maths.ceil(x)
    }

    fun yc(): Int {
        return Maths.ceil(y)
    }

    override fun toString(): String {
        return "$x, $y"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec2d
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    companion object {
        fun crs(a: Vec2d, b: Vec2d): Double {
            return a.x * b.y - a.y * b.x
        }

        fun crs(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            return x1 * y2 - y1 * x2
        }

        fun dot(a: Vec2d, b: Vec2d): Double {
            return a.x * b.x + a.y * b.y
        }

        fun dot(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            return x1 * x2 + y1 * y2
        }

        fun len(x: Double, y: Double): Double {
            return sqrt(x * x + y * y)
        }
    }
}
