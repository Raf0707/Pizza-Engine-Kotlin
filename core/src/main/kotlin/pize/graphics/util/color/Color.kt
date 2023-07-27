package pize.graphics.util.color

import kotlin.math.max

class Color : IColor {
    private var r = 0f
    private var g = 0f
    private var b = 0f
    private var a = 0f

    constructor(red: Float, green: Float, blue: Float, alpha: Float) {
        set(red, green, blue, alpha)
    }

    constructor(red: Double, green: Double, blue: Double, alpha: Double) {
        set(red, green, blue, alpha)
    }

    constructor(red: Int, green: Int, blue: Int, alpha: Int) {
        set(red, green, blue, alpha)
    }

    constructor(color: IColor) {
        set(color)
    }

    constructor(color: FloatArray) {
        set(color)
    }

    constructor() {
        reset()
    }

    override fun r(): Float {
        return r
    }

    override fun g(): Float {
        return g
    }

    override fun b(): Float {
        return b
    }

    override fun a(): Float {
        return a
    }

    operator fun set(red: Float, green: Float, blue: Float, alpha: Float) {
        r = red
        g = green
        b = blue
        a = alpha
    }

    operator fun set(red: Double, green: Double, blue: Double, alpha: Double) {
        r = red.toFloat()
        g = green.toFloat()
        b = blue.toFloat()
        a = alpha.toFloat()
    }

    operator fun set(red: Int, green: Int, blue: Int, alpha: Int) {
        r = red / 255f
        g = green / 255f
        b = blue / 255f
        a = alpha / 255f
    }

    fun set(color: FloatArray) {
        set(color[0], color[1], color[2], color[3])
    }

    fun set(color: IColor) {
        set(color.r(), color.g(), color.b(), color.a())
    }

    operator fun set(red: Float, green: Float, blue: Float) {
        r = red
        g = green
        b = blue
    }

    operator fun set(red: Double, green: Double, blue: Double) {
        r = red.toFloat()
        g = green.toFloat()
        b = blue.toFloat()
    }

    operator fun set(red: Int, green: Int, blue: Int) {
        r = red / 255f
        g = green / 255f
        b = blue / 255f
    }

    fun setR(r: Float): Color {
        this.r = r
        return this
    }

    fun setG(g: Float): Color {
        this.g = g
        return this
    }

    fun setB(b: Float): Color {
        this.b = b
        return this
    }

    fun setA(a: Float): Color {
        this.a = a
        return this
    }

    fun mul(r: Double, g: Double, b: Double, a: Double): Color {
        set(
            r() * r,
            g() * g,
            b() * b,
            a() * a
        )
        return this
    }

    fun mul(value: Double): Color {
        set(
            r() * value,
            g() * value,
            b() * value,
            a()
                .toDouble()
        )
        return this
    }

    fun blend(color: IColor): Color {
        val totalAlpha = a + color.a()
        val w1 = a / totalAlpha
        val w2 = color.a() / totalAlpha
        r = r * w1 + color.r() * w2
        g = g * w1 + color.g() * w2
        b = b * w1 + color.b() * w2
        a = max(a.toDouble(), color.a().toDouble()).toFloat()
        return this
    }

    fun reset() {
        set(1f, 1f, 1f, 1f)
    }

    fun inverse(): Color {
        r = 1 - r
        g = 1 - g
        b = 1 - b
        return this
    }

    fun copy(): Color {
        return Color(this)
    }

    companion object {
        @JvmField
        var WHITE = ImmutableColor(1f, 1f, 1f, 1f)
        var BLACK = ImmutableColor(0f, 0f, 0f, 1f)
    }
}
