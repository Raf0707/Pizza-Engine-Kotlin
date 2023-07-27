package pize.graphics.util.color

class ImmutableColor : IColor {
    var r: Float
    var g: Float
    var b: Float
    var a: Float

    constructor(red: Float, green: Float, blue: Float, alpha: Float) {
        r = red
        g = green
        b = blue
        a = alpha
    }

    constructor(red: Double, green: Double, blue: Double, alpha: Double) {
        r = red.toFloat()
        g = green.toFloat()
        b = blue.toFloat()
        a = alpha.toFloat()
    }

    constructor(red: Int, green: Int, blue: Int, alpha: Int) {
        r = red / 255f
        g = green / 255f
        b = blue / 255f
        a = alpha / 255f
    }

    constructor(color: IColor) {
        r = color.r()
        g = color.g()
        b = color.b()
        a = color.a()
    }

    constructor(color: FloatArray) {
        r = color[0]
        g = color[1]
        b = color[3]
        a = color[3]
    }

    constructor() {
        r = 1f
        g = 1f
        b = 1f
        a = 1f
    }

    fun copy(): ImmutableColor {
        return ImmutableColor(this)
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
}
