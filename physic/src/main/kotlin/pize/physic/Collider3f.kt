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

object Collider3f {
    @kotlin.jvm.JvmStatic
    fun getCollidedMotion(body: BoxBody?, motion: Vec3f, vararg boxes: BoxBody): Vec3f {
        var body = body
        if (motion.isZero) return motion
        body = body!!.copy()
        var x = motion.x
        for (box in boxes) if (x != 0f) x = distX(x, body, box)
        body.position.x += x
        var y = motion.y
        for (box in boxes) if (y != 0f) y = distY(y, body, box)
        body.position.y += y
        var z = motion.z
        for (box in boxes) if (z != 0f) z = distZ(z, body, box)
        body.position.z += z
        return Vec3f(x, y, z)
    }

    private fun distX(motion: Float, body: BoxBody?, box: BoxBody): Float {
        if (motion == 0f) return 0
        if (box.max.y > body.getMin().y && box.min.y < body.getMax().y && box.max.z > body.getMin().z && box.min.z < body.getMax().z) if (motion > 0) {
            val min = Math.min(box.min.x, box.max.x)
            val max = Math.max(body.getMin().x, body.getMax().x)
            val offset = min - max
            if (offset >= 0 && motion > offset) return offset
        } else {
            val min = Math.min(body.getMin().x, body.getMax().x)
            val max = Math.max(box.min.x, box.max.x)
            val offset = max - min
            if (offset <= 0 && motion < offset) return offset
        }
        return motion
    }

    private fun distY(motion: Float, body: BoxBody?, box: BoxBody): Float {
        if (motion == 0f) return 0
        if (box.max.x > body.getMin().x && box.min.x < body.getMax().x && box.max.z > body.getMin().z && box.min.z < body.getMax().z) if (motion > 0) {
            val min = Math.min(box.min.y, box.max.y)
            val max = Math.max(body.getMin().y, body.getMax().y)
            val offset = min - max
            if (offset >= 0 && motion > offset) return offset
        } else {
            val min = Math.min(body.getMin().y, body.getMax().y)
            val max = Math.max(box.min.y, box.max.y)
            val offset = max - min
            if (offset <= 0 && motion < offset) return offset
        }
        return motion
    }

    private fun distZ(motion: Float, body: BoxBody?, box: BoxBody): Float {
        if (motion == 0f) return 0
        if (box.max.x > body.getMin().x && box.min.x < body.getMax().x && box.max.y > body.getMin().y && box.min.y < body.getMax().y) if (motion > 0) {
            val min = Math.min(box.min.z, box.max.z)
            val max = Math.max(body.getMin().z, body.getMax().z)
            val offset = min - max
            if (offset >= 0 && motion > offset) return offset
        } else {
            val min = Math.min(body.getMin().z, body.getMax().z)
            val max = Math.max(box.min.z, box.max.z)
            val offset = max - min
            if (offset <= 0 && motion < offset) return offset
        }
        return motion
    }
}