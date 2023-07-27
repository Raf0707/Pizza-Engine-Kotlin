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

open class BoxBody @kotlin.jvm.JvmOverloads constructor(
    val boundingBox: BoundingBox3? = BoundingBox3(0, 0, 0, 0, 0, 0),
    val position: Vec3f = Vec3f()
) {

    constructor(position: Vec3f) : this(BoundingBox3(0, 0, 0, 0, 0, 0), position) {}
    constructor(body: BoxBody) : this(body.boundingBox!!.copy(), body.position.copy()) {}

    val min: Vec3f
        get() = position.copy().add(boundingBox.getMin())
    val max: Vec3f
        get() = position.copy().add(boundingBox.getMax())

    fun intersects(body: BoxBody): Boolean {
        return min.x < body.max.x && max.x > body.min.x &&
                min.y < body.max.y && max.y > body.min.y &&
                min.z < body.max.z && max.z > body.min.z
    }

    fun copy(): BoxBody {
        return BoxBody(this)
    }
}