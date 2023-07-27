package pize.util

object ArrayUtils {
    fun contains(arr: IntArray, e: Int): Boolean {
        for (i in arr) if (i == e) return true
        return false
    }

    fun add(a: FloatArray, b: FloatArray): FloatArray {
        val c = FloatArray(a.size + b.size)
        System.arraycopy(a, 0, c, 0, a.size)
        System.arraycopy(b, 0, c, a.size, b.size)
        return c
    }

    fun add(a: FloatArray, vararg b: FloatArray): FloatArray {
        var c = add(a, b[0])
        for (i in 1 until b.size) c = add(c, b[i])
        return c
    }
}
