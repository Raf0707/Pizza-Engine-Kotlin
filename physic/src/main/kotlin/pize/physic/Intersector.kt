package pize.physic

import pize.math.vecmath.vector.Vec3f
import pize.physic.Ray3f
import pize.physic.BoxBody
import pize.math.Maths
import pize.physic.BoundingBox3
import pize.physic.BoundingBox2
import pize.math.vecmath.vector.Vec2f
import pize.physic.RectBody
import pize.physic.Collider2f
import pize.physic.Collider3f
import pize.physic.Velocity2f
import pize.physic.Velocity3f
import pize.physic.Intersector

object Intersector {
    fun lineQuad(q1: Vec3f, q2: Vec3f, p1: Vec3f, p2: Vec3f, p3: Vec3f, p4: Vec3f): Vec3f? {
        val i1 = lineTriangle(q1, q2, p1, p2, p3)
        val i2 = lineTriangle(q1, q2, p1, p4, p3)
        return if (i1 == null && i2 == null) null else i2 ?: i1
    }

    fun lineQuad(
        q1: Vec3f,
        q2: Vec3f,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        x3: Double,
        y3: Double,
        z3: Double,
        x4: Double,
        y4: Double,
        z4: Double
    ): Vec3f? {
        val p1 = Vec3f(x1, y1, z1)
        val p2 = Vec3f(x2, y2, z2)
        val p3 = Vec3f(x3, y3, z3)
        val p4 = Vec3f(x4, y4, z4)
        val i1 = lineTriangle(q1, q2, p1, p2, p3)
        val i2 = lineTriangle(q1, q2, p1, p4, p3)
        return if (i1 == null && i2 == null) null else i2 ?: i1
    }

    fun lineTriangle(q1: Vec3f, q2: Vec3f, p1: Vec3f, p2: Vec3f, p3: Vec3f): Vec3f? {
        val s1 = signedTetraVolume(q1, p1, p2, p3)
        val s2 = signedTetraVolume(q2, p1, p2, p3)
        if (s1 != s2) {
            val s3 = signedTetraVolume(q1, q2, p1, p2)
            val s4 = signedTetraVolume(q1, q2, p2, p3)
            val s5 = signedTetraVolume(q1, q2, p3, p1)
            if (s3 == s4 && s4 == s5) {
                val n = Vec3f.crs(p2.copy().sub(p1), p3.copy().sub(p1))
                val t = (-Vec3f.dot(q1, n.copy().sub(p1)) / Vec3f.dot(q1, q2.copy().sub(q1))).toDouble()
                return q1.add(q2.copy().sub(q1).mul(t))
            }
        }
        return null
    }

    private fun signedTetraVolume(a: Vec3f, b: Vec3f, c: Vec3f, d: Vec3f): Double {
        return Math.signum(Vec3f.dot(Vec3f.crs(b.copy().sub(a), c.copy().sub(a)), d.copy().sub(a)) / 6f).toDouble()
    }
}