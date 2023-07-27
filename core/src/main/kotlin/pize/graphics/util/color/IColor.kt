package pize.graphics.util.color

import pize.math.Mathc
import pize.math.Maths

abstract class IColor {
    abstract fun r(): Float
    abstract fun g(): Float
    abstract fun b(): Float
    abstract fun a(): Float
    val hex: String
        get() {
            val red = Integer.toHexString(Maths.round((Maths.clamp(r(), 0f, 1f) * 255).toDouble()))
            val green = Integer.toHexString(Maths.round((Maths.clamp(g(), 0f, 1f) * 255).toDouble()))
            val blue = Integer.toHexString(Maths.round((Maths.clamp(b(), 0f, 1f) * 255).toDouble()))
            val alpha = Integer.toHexString(Maths.round((Maths.clamp(a(), 0f, 1f) * 255).toDouble()))
            val sb = StringBuilder("#")
            if (red.length < 2) sb.append('0')
            sb.append(red)
            if (green.length < 2) sb.append('0')
            sb.append(green)
            if (blue.length < 2) sb.append('0')
            sb.append(blue)
            if (alpha.length < 2) sb.append('0')
            sb.append(alpha)
            return sb.toString()
        }

    fun toArray(): FloatArray {
        return floatArrayOf(r(), g(), b(), a())
    }

    override fun toString(): String {
        return r().toString() + ", " + g() + ", " + b() + ", " + a()
    }

    companion object {
        fun floatToIntColor(value: Float): Int {
            var intBits = java.lang.Float.floatToRawIntBits(value)
            intBits = intBits or (((intBits ushr 24) * (255f / 254f)).toInt() shl 24)
            return intBits
        }

        fun intToFloatColor(value: Int): Float {
            return java.lang.Float.intBitsToFloat(value and -0x1000001)
        }

        fun random(): Color {
            return Color(
                Mathc.random(),
                Mathc.random(),
                Mathc.random(),
                1f
            )
        }
    }
}
