package pize.math.function

import pize.math.Maths
import kotlin.math.floor

object ImprovedNoise {
    fun noise(x: Double, y: Double, z: Double): Double {
        // Fine unit cube that contains point
        var x = x
        var y = y
        var z = z
        val X = floor(x).toInt() and 255
        val Y = floor(y).toInt() and 255
        val Z = floor(z).toInt() and 255

        // Hash coordinates of the 8 cube corners
        val A = p[X] + Y
        val AA = p[A] + Z
        val AB = p[A + 1] + Z
        val B = p[X + 1] + Y
        val BA = p[B] + Z
        val BB = p[B + 1] + Z
        x -= floor(x) // Find relative x ,y , z
        y -= floor(y) // of point in cube
        z -= floor(z)
        val u = Maths.quintic(x.toFloat()).toDouble() // Compute fade curves for each of x, y, z
        val v = Maths.quintic(y.toFloat()).toDouble()
        val w = Maths.quintic(z.toFloat()).toDouble()

        // ... and add blended results from 8 corners of cube
        return Maths.lerp(
            w,
            Maths.lerp(
                v,
                Maths.lerp(
                    u,
                    grad(p[AA], x, y, z),
                    grad(p[BA], x - 1, y, z)
                ),
                Maths.lerp(
                    u,
                    grad(p[AB], x, y - 1, z),
                    grad(p[BB], x - 1, y - 1, z)
                )
            ),
            Maths.lerp(
                v,
                Maths.lerp(
                    u,
                    grad(p[AA + 1], x, y, z - 1),
                    grad(p[BA + 1], x - 1, y, z - 1)
                ),
                Maths.lerp(
                    u,
                    grad(p[AB + 1], x, y - 1, z - 1),
                    grad(p[BB + 1], x - 1, y - 1, z - 1)
                )
            )
        )
    }

    fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        // Convert low 4 bits of hash code into 12 gradient directions
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if (h and 1 == 0) u else -u) + if (h and 2 == 0) v else -v
    }

    val p = IntArray(512)
    val permutation = intArrayOf(151, 160, 137, 91, 90, 15)

    init {
        for (i in 0..255) {
            p[i] = permutation[i]
            p[256 + i] = p[i]
        }
    }
}