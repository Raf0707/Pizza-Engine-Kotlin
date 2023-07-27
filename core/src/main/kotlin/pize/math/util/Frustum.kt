package pize.math.util

import pize.math.Mathc
import pize.math.vecmath.matrix.Matrix4f

class Frustum(view: Matrix4f?, proj: Matrix4f?) {
    private lateinit var frustum: Array<FloatArray>

    init {
        setFrustum(view, proj)
    }

    fun setFrustum(view: Matrix4f?, proj: Matrix4f?) {
        val clip: FloatArray = Matrix4f.Companion.mul(proj!!.`val`, view!!.`val`)
        frustum = arrayOf(
            floatArrayOf(clip[3] - clip[0], clip[7] - clip[4], clip[11] - clip[8], clip[15] - clip[12]),
            floatArrayOf(
                clip[3] + clip[0], clip[7] + clip[4], clip[11] + clip[8], clip[15] + clip[12]
            ),
            floatArrayOf(clip[3] + clip[1], clip[7] + clip[5], clip[11] + clip[9], clip[15] + clip[13]),
            floatArrayOf(
                clip[3] - clip[1], clip[7] - clip[5], clip[11] - clip[9], clip[15] - clip[13]
            ),
            floatArrayOf(clip[3] - clip[2], clip[7] - clip[6], clip[11] - clip[10], clip[15] - clip[14]),
            floatArrayOf(
                clip[3] + clip[2], clip[7] + clip[6], clip[11] + clip[10], clip[15] + clip[14]
            )
        )
        for (i in 0..5) divide(i)
    }

    private fun divide(index: Int) {
        val f = Mathc.sqrt(
            (
                    frustum[index][0] * frustum[index][0] + frustum[index][1] * frustum[index][1] + frustum[index][2] * frustum[index][2]
                    ).toDouble()
        )
        frustum[index][0] /= f
        frustum[index][1] /= f
        frustum[index][2] /= f
        frustum[index][3] /= f
    }

    private fun multiply(index: Int, x: Double, y: Double, z: Double): Double {
        return frustum[index][0] * x + frustum[index][1] * y + frustum[index][2] * z +
                frustum[index][3]
    }

    fun isBoxInFrustum(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Boolean {
        for (i in 0..5) if (multiply(i, x1, y1, z1) <= 0 && multiply(i, x2, y1, z1) <= 0 && multiply(
                i,
                x1,
                y2,
                z1
            ) <= 0 && multiply(i, x2, y2, z1) <= 0 && multiply(i, x1, y1, z2) <= 0 && multiply(
                i,
                x2,
                y1,
                z2
            ) <= 0 && multiply(i, x1, y2, z2) <= 0 && multiply(i, x2, y2, z2) <= 0
        ) return false
        return true
    }
}