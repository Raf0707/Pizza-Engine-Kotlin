package pize.math.vecmath.vector

import pize.math.Mathc
import java.util.*
import kotlin.math.sqrt

class Vec2i {
    constructor()
    constructor(x: Int, y: Int) {
        set(x, y)
    }

    constructor(xy: Int) {
        set(xy)
    }

    constructor(vector: Vec2i) {
        set(vector)
    }

    constructor(vector: Vec3i) {
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
    fun len(): Double {
        return sqrt((x * x + y * y).toDouble())
    }

    fun abs(): Vec2i {
        if (x < 0) x *= -1
        if (y < 0) y *= -1
        return this
    }

    fun zero(): Vec2i {
        set(0, 0)
        return this
    }

    val isZero: Boolean
        get() = x == 0 && y == 0

    fun dot(x: Float, y: Float): Double {
        return (this.x * x + this.y * y).toDouble()
    }

    fun dot(x: Double, y: Double): Double {
        return this.x * x + this.y * y
    }

    fun dot(vector: Vec2i): Double {
        return (x * vector.x + y * vector.y).toDouble()
    }

    fun dot(vector: Vec2f): Double {
        return (x * vector.x + y * vector.y).toDouble()
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

    fun crs(vector: Vec2i): Float {
        return (x * vector.y - y * vector.x).toFloat()
    }

    fun crs(vector: Vec2f): Float {
        return x * vector.y - y * vector.x
    }

    fun crs(vector: Vec2d): Double {
        return x * vector.y - y * vector.x
    }

    fun crs(): Vec2i {
        return Vec2i(y, -x)
    }

    fun copy(): Vec2i {
        return Vec2i(this)
    }

    /**             TUPLE              */
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    operator fun set(x: Int, y: Int): Vec2i {
        this.x = x
        this.y = y
        return this
    }

    fun set(xy: Int): Vec2i {
        x = xy
        y = xy
        return this
    }

    fun set(vector: Vec2i): Vec2i {
        x = vector.x
        y = vector.y
        return this
    }

    fun set(vector: Vec3i): Vec2i {
        x = vector.x
        y = vector.y
        return this
    }

    fun add(x: Int, y: Int): Vec2i {
        this.x += x
        this.y += y
        return this
    }

    fun add(xy: Int): Vec2i {
        x += xy
        y += xy
        return this
    }

    fun add(vector: Vec2d): Vec2i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun add(vector: Vec2f): Vec2i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun add(vector: Vec3d): Vec2i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun add(vector: Vec3f): Vec2i {
        x = (x + vector.x).toInt()
        y = (y + vector.y).toInt()
        return this
    }

    fun sub(x: Int, y: Int): Vec2i {
        this.x -= x
        this.y -= y
        return this
    }

    fun sub(xy: Int): Vec2i {
        x -= xy
        y -= xy
        return this
    }

    fun sub(vector: Vec2d): Vec2i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun sub(vector: Vec2f): Vec2i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun sub(vector: Vec3d): Vec2i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun sub(vector: Vec3f): Vec2i {
        x = (x - vector.x).toInt()
        y = (y - vector.y).toInt()
        return this
    }

    fun mul(x: Int, y: Int): Vec2i {
        this.x *= x
        this.y *= y
        return this
    }

    fun mul(xy: Int): Vec2i {
        x *= xy
        y *= xy
        return this
    }

    fun mul(vector: Vec2d): Vec2i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun mul(vector: Vec2f): Vec2i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun mul(vector: Vec3d): Vec2i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun mul(vector: Vec3f): Vec2i {
        x = (x * vector.x).toInt()
        y = (y * vector.y).toInt()
        return this
    }

    fun div(x: Int, y: Int): Vec2i {
        this.x /= x
        this.y /= y
        return this
    }

    operator fun div(xy: Int): Vec2i {
        x /= xy
        y /= xy
        return this
    }

    operator fun div(vector: Vec2d): Vec2i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    operator fun div(vector: Vec2f): Vec2i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    operator fun div(vector: Vec3d): Vec2i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    operator fun div(vector: Vec3f): Vec2i {
        x = (x / vector.x).toInt()
        y = (y / vector.y).toInt()
        return this
    }

    override fun toString(): String {
        return "$x, $y"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` == null || javaClass != `object`.javaClass) return false
        val tuple = `object` as Vec2i
        return x == tuple.x && y == tuple.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    companion object {
        fun crs(a: Vec2i, b: Vec2i): Float {
            return (a.x * b.y - a.y * b.x).toFloat()
        }

        fun crs(x1: Int, y1: Int, x2: Int, y2: Int): Float {
            return (x1 * y2 - y1 * x2).toFloat()
        }

        fun dot(a: Vec2i, b: Vec2i): Float {
            return (a.x * b.x + a.y * b.y).toFloat()
        }

        fun dot(x1: Int, y1: Int, x2: Int, y2: Int): Float {
            return (x1 * x2 + y1 * y2).toFloat()
        }

        fun len(x: Int, y: Int): Double {
            return sqrt((x * x + y * y).toDouble())
        }
    }
}
