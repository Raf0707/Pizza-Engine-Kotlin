package pize.math

import kotlin.math.sign

object Mathc {
    @JvmStatic
    fun sin(a: Double): Float {
        return kotlin.math.sin(a).toFloat()
    }

    @JvmStatic
    fun cos(a: Double): Float {
        return kotlin.math.cos(a).toFloat()
    }

    fun tan(a: Double): Float {
        return kotlin.math.tan(a).toFloat()
    }

    @JvmStatic
    fun asin(a: Double): Float {
        return kotlin.math.asin(a).toFloat()
    }

    fun acos(a: Double): Float {
        return kotlin.math.acos(a).toFloat()
    }

    fun atan(a: Double): Float {
        return kotlin.math.atan(a).toFloat()
    }

    fun atan2(y: Double, x: Double): Float {
        return kotlin.math.atan2(y, x).toFloat()
    }

    @JvmStatic
    fun hypot(x: Double, y: Double): Float {
        return kotlin.math.hypot(x, y).toFloat()
    }

    fun ceil(a: Double): Int {
        return kotlin.math.ceil(a).toInt()
    }

    fun round(a: Double): Int {
        return Math.round(a).toInt()
    }

    fun floor(a: Double): Int {
        return kotlin.math.floor(a).toInt()
    }

    @JvmStatic
    fun sqrt(a: Double): Float {
        return kotlin.math.sqrt(a).toFloat()
    }

    fun cbrt(a: Double): Float {
        return kotlin.math.cbrt(a).toFloat()
    }

    fun exp(a: Double): Float {
        return kotlin.math.exp(a).toFloat()
    }

    @JvmStatic
    fun pow(a: Double, b: Double): Float {
        return a.pow(b) as Float
    }

    fun random(): Float {
        return Math.random().toFloat()
    }

    fun min(a: Short, b: Short): Short {
        return kotlin.math.min(a.toDouble(), b.toDouble()).toShort()
    }

    fun max(a: Short, b: Short): Short {
        return kotlin.math.max(a.toDouble(), b.toDouble()).toShort()
    }

    @JvmStatic
    fun signum(a: Float): Int {
        return sign(a.toDouble()).toInt()
    }

    @JvmStatic
    fun signum(a: Double): Int {
        return sign(a).toInt()
    }
}
