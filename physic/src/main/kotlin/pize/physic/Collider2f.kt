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

object Collider2f {
    @kotlin.jvm.JvmStatic
    fun getCollidedMotion(body: RectBody?, motion: Vec2f, vararg rects: RectBody): Vec2f {
        var body = body
        if (motion.isZero) return motion
        body = body!!.copy()
        var x = motion.x
        for (rect in rects) if (x != 0f) x = distX(x, body, rect)
        body.pos().x += x
        var y = motion.y
        for (rect in rects) if (y != 0f) y = distY(y, body, rect)
        body.pos().y += y
        return Vec2f(x, y)
    }

    private fun distX(motion: Float, body: RectBody?, rect: RectBody): Float {
        if (motion == 0f) return 0F
        if (rect.max.y > body?.min!!.y && rect.min.y < body?.max!!.y) if (motion > 0) {
            val min = Math.min(rect.min.x, rect.max.x)
            val max = Math.max(body?.min!!.x, body?.max!!.x)
            val offset = min - max
            if (offset >= 0 && motion > offset) return offset
        } else {
            val min = Math.min(body?.min!!.x, body?.max!!.x)
            val max = Math.max(rect.min.x, rect.max.x)
            val offset = max - min
            if (offset <= 0 && motion < offset) return offset
        }
        return motion
    }

    private fun distY(motion: Float, body: RectBody?, rect: RectBody): Float {
        if (motion == 0f) return 0F
        if (rect.max.x > body?.min!!.x && rect.min.x < body?.max!!.x) if (motion > 0) {
            val min = Math.min(rect.min.y, rect.max.y)
            val max = Math.max(body?.min!!.y, body?.max!!.y)
            val offset = min - max
            if (offset >= 0 && motion > offset) return offset
        } else {
            val min = Math.min(body?.min!!.y, body?.max!!.y)
            val max = Math.max(rect.min.y, rect.max.y)
            val offset = max - min
            if (offset <= 0 && motion < offset) return offset
        }
        return motion
    }
}