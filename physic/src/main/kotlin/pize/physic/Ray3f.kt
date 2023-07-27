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

class Ray3f {
    val origin: Vec3f
    val direction: Vec3f

    constructor() {
        origin = Vec3f()
        direction = Vec3f()
    }

    constructor(ray: Ray3f) {
        origin = ray.origin.copy()
        direction = ray.direction.copy()
    }

    operator fun set(position: Vec3f, direction: Vec3f) {
        position.set(position)
        direction.set(direction)
    }

    operator fun set(position: Vec3f, direction: Vec3f, length: Float) {
        position.set(position)
        direction.set(direction).mul(length)
    }

    fun set(ray: Ray3f) {
        set(ray.origin, ray.direction)
    }

    fun set(direction: Vec3f) {
        direction.set(direction)
    }

    fun intersects(box: BoxBody): Boolean {
        val min = box.min
        val max = box.max
        return intersects(
            floatArrayOf(
                max!!.x, max.y, max.z,
                min!!.x, max.y, max.z,
                max.x, min.y, max.z,
                max.x, max.y, min.z,
                min.x, min.y, max.z,
                max.x, min.y, min.z,
                min.x, max.y, min.z,
                min.x, min.y, min.z
            )
        )
    }

    fun intersect(v0: Vec3f?, v1: Vec3f, v2: Vec3f): Float {
        val edge10 = v1.sub(v0!!)
        val edge20 = v2.sub(v0!!)
        val normal = direction.copy().crs(edge20)
        val det = edge10.copy().dot(normal)
        if (det < Maths.Epsilon) return (-1).toFloat()
        val invDet = 1 / det
        val tvec = origin.copy().sub(v0)
        val u = tvec.copy().dot(normal) * invDet
        if (u < 0 || u > 1) return (-1).toFloat()
        val qvec = tvec.copy().crs(edge10)
        val v = direction.copy().dot(qvec) * invDet
        return if (v < 0 || u + v > 1) (-1).toFloat() else edge20.copy().dot(qvec) * invDet
    }

    fun intersects(vertices: FloatArray): Boolean {
        val size = vertices.size / 3
        var i = 0
        while (i < size) {
            val vertexOffset = i * 3
            val v0 = Vec3f(
                vertices[vertexOffset],
                vertices[vertexOffset + 1],
                vertices[vertexOffset + 2]
            )
            val v1 = Vec3f(
                vertices[vertexOffset + 3],
                vertices[vertexOffset + 4],
                vertices[vertexOffset + 5]
            )
            val v2 = Vec3f(
                vertices[vertexOffset + 6],
                vertices[vertexOffset + 7],
                vertices[vertexOffset + 8]
            )
            if (intersect(v0, v1, v2) != -1f) return true
            i += 3
        }
        return false
    }

    fun copy(): Ray3f {
        return Ray3f(this)
    }
}