package pize.math

import kotlin.math.max
import kotlin.math.min

object Maths {
    const val Epsilon = 1E-19f
    const val PI = Math.PI.toFloat()
    const val PI2 = PI * 2
    const val HalfPI = PI / 2
    const val ToDeg = 180 / PI
    const val ToRad = PI / 180
    @JvmField
    val Sqrt2 = Mathc.sqrt(2.0)
    @JvmField
    val Sqrt3 = Mathc.sqrt(3.0)
    val Sqrt4 = Mathc.sqrt(4.0)
    const val NanosInSecond = 1000000000f
    const val NanosInMs = 1000000f
    fun sinFromCos(cos: Float): Float {
        return Mathc.sqrt((1 - cos * cos).toDouble())
    }

    fun cosFromSin(sin: Float): Float {
        return Mathc.sqrt((1 - sin * sin).toDouble())
    }

    @JvmStatic
    fun abs(a: Int): Int {
        return if (a < 0) -a else a
    }

    @JvmStatic
    fun abs(a: Float): Float {
        return if (a < 0) -a else a
    }

    fun abs(a: Double): Double {
        return if (a < 0) -a else a
    }

    @JvmStatic
    fun floor(a: Double): Int {
        return (if (a < 0) a - 1 else a).toInt()
    }

    @JvmStatic
    fun round(a: Double): Int {
        return floor(a + 0.5)
    }

    @JvmStatic
    fun ceil(a: Double): Int {
        return (if (a > 0) a + 1 else a).toInt()
    }

    @JvmStatic
    fun frac(a: Float): Float {
        return a - floor(a.toDouble())
    }

    fun frac(a: Double): Double {
        return a - floor(a)
    }

    @JvmStatic
    fun frac(value: Double, min: Double, max: Double): Double {
        val interval = max - min
        return frac((value - min) / interval) * interval + min
    }

    fun clamp(a: Int, min: Int, max: Int): Int {
        return max(min.toDouble(), min(a.toDouble(), max.toDouble())).toInt()
    }

    @JvmStatic
    fun clamp(a: Float, min: Float, max: Float): Float {
        return max(min.toDouble(), min(a.toDouble(), max.toDouble())).toFloat()
    }

    fun clamp(a: Double, min: Double, max: Double): Double {
        return max(min, min(a, max))
    }

    @JvmStatic
    fun random(min: Float, max: Float): Float {
        return lerp(min, max, Mathc.random())
    }

    @JvmStatic
    fun random(min: Double, max: Double): Double {
        return lerp(min, max, Mathc.random().toDouble())
    }

    @JvmStatic
    fun random(min: Int, max: Int): Int {
        return round(lerp(min.toFloat(), max.toFloat(), Mathc.random()).toDouble())
    }

    @JvmStatic
    fun random(max: Float): Float {
        return Mathc.random() * max
    }

    @JvmStatic
    fun random(max: Int): Int {
        return round(Math.random() * max)
    }

    @JvmOverloads
    fun randomBoolean(chance: Float = 0.5f): Boolean {
        return Math.random() < chance
    }

    @JvmStatic
    fun randomSeed(length: Int): Long {
        if (length <= 0) return 0
        val seed = StringBuilder()
        for (i in 0 until length) seed.append(random(if (i == 0) 1 else 0, 9))
        return seed.toString().toLong()
    }

    fun randomInts(array: IntArray, min: Int, max: Int) {
        val range = max - min
        for (i in array.indices) array[i] = round(Math.random() * range + min)
    }

    fun randomShorts(array: Array<Short?>, min: Int, max: Int) {
        val range = max - min
        for (i in array.indices) {
            array[i] = round(Math.random() * range + min).toShort()
        }
    }

    fun randomBytes(array: ByteArray, min: Int, max: Int) {
        val range = max - min
        for (i in array.indices) array[i] = round(Math.random() * range + min).toByte()
    }

    fun lerp(start: Float, end: Float, t: Float): Float {
        return start + (end - start) * t
    }

    fun lerp(start: Double, end: Double, t: Double): Double {
        return start + (end - start) * t
    }

    fun lerp(start: Int, end: Int, t: Int): Int {
        return start + (end - start) * t
    }

    fun cerp(a: Float, b: Float, c: Float, d: Float, t: Float): Float {
        val p = d - c - (a - b)
        val q = a - b - p
        val r = c - a
        return t * (t * (t * p + q) + r) + b // pt^3 + qt^2 + rt^1 + b
    }

    fun cubic(t: Float): Float {
        return -2 * t * t * t + 3 * t * t
    }

    fun cosine(t: Float): Float {
        return (1 - Mathc.cos((t / PI).toDouble())) / 2
    }

    @JvmStatic
    fun quintic(t: Float): Float {
        return t * t * t * (t * (t * 6 - 15) + 10)
    }

    fun hermite(t: Float): Float {
        return t * t * (3 - 2 * t)
    }

    @JvmStatic
    fun sigmoid(x: Float): Float {
        return 1 / (1 + Mathc.exp(-x.toDouble()))
    }

    fun relu(x: Float): Float {
        return max(0.0, x.toDouble()).toFloat()
    }

    fun leakyRelu(x: Float): Float {
        return max((0.1f * x).toDouble(), x.toDouble()).toFloat()
    }

    @JvmStatic
    fun map(value: Float, fromLow: Float, fromHigh: Float, toLow: Float, toHigh: Float): Float {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow
    }

    @JvmStatic
    fun map(value: Double, fromLow: Double, fromHigh: Double, toLow: Double, toHigh: Double): Double {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow
    }

    @JvmStatic
    fun sinDeg(a: Double): Float {
        return Mathc.sin(a * ToRad)
    }

    @JvmStatic
    fun cosDeg(a: Double): Float {
        return Mathc.cos(a * ToRad)
    }

    fun tanDeg(a: Double): Float {
        return Mathc.tan(a * ToRad)
    }

    fun nonZeroSignum(a: Int): Int {
        return if (a >= 0) 1 else -1
    }

    fun nonZeroSignum(a: Float): Int {
        return if (a >= 0) 1 else -1
    }

    fun nonZeroSignum(a: Double): Int {
        return if (a >= 0) 1 else -1
    }

    fun invSqrt(a: Float): Float {
        var a = a
        val aHalf = a * 0.5f
        var i = java.lang.Float.floatToIntBits(a)
        i = 0x5f3759df - (i shr 1)
        a = java.lang.Float.intBitsToFloat(i)
        a *= 1.5f - aHalf * a * a
        return a
    }

    fun invSqrt(a: Double): Double {
        var a = a
        val aHalf = a * 0.5
        var i = java.lang.Double.doubleToLongBits(a)
        i = 0x5fe6ec85e7de30daL - (i shr 1)
        a = java.lang.Double.longBitsToDouble(i)
        a *= 1.5 - aHalf * a * a
        return a
    }

    fun dot(a: FloatArray, b: FloatArray): Float {
        var result = 0f
        for (i in a.indices) result += a[i] * b[i]
        return result
    }

    @JvmStatic
    fun mul(`in`: FloatArray, w: FloatArray, out: FloatArray) {
        for (o in out.indices) for (i in `in`.indices) out[o] += `in`[i] * w[i]
    }
}
