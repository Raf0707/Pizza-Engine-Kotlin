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

open class RectBody {
    private val rect: BoundingBox2?
    private val position: Vec2f

    constructor(rect: BoundingBox2?) {
        this.rect = rect
        position = Vec2f()
    }

    constructor(body: RectBody) {
        rect = body.rect!!.copy()
        position = body.pos().copy()
    }

    val min: Vec2f
        get() = position.copy().add(rect?.min!!)
    val max: Vec2f
        get() = position.copy().add(rect?.max!!)

    fun rect(): BoundingBox2? {
        return rect
    }

    fun pos(): Vec2f {
        return position
    }

    fun copy(): RectBody {
        return RectBody(this)
    }
}