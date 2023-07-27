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

class BoundingBox2 {
    val min: Vec2f
    val max: Vec2f

    constructor(minX: Double, minY: Double, maxX: Double, maxY: Double) {
        min = Vec2f(minX, minY)
        max = Vec2f(maxX, maxY)
    }

    constructor(min: Vec2f, max: Vec2f) {
        this.min = min
        this.max = max
    }

    constructor(box: BoundingBox2) {
        min = box.min.copy()
        max = box.max.copy()
    }

    fun resize(minX: Double, minY: Double, maxX: Double, maxY: Double) {
        min[minX] = minY
        max[maxX] = maxY
    }

    fun resize(min: Vec2f?, max: Vec2f?) {
        this.min.set(min)
        this.max.set(max)
    }

    fun resize(box: BoundingBox2) {
        resize(box.min, box.max)
    }

    fun expand(negativeX: Double, negativeY: Double, positiveX: Double, positiveY: Double) {
        resize(min.x - negativeX, min.y - negativeY, max.x + positiveX, max.y + positiveY)
    }

    fun expand(negative: Vec2f, positive: Vec2f) {
        expand(negative.x.toDouble(), negative.y.toDouble(), positive.x.toDouble(), positive.y.toDouble())
    }

    fun expand(expandX: Double, expandY: Double) {
        expand(expandX, expandY, expandX, expandY)
    }

    fun expand(expand: Double) {
        expand(expand, expand)
    }

    fun shift(shiftX: Double, shiftY: Double) {
        resize(min.x + shiftX, min.y + shiftY, max.x + shiftX, max.y + shiftY)
    }

    fun shift(shift: Vec2f) {
        shift(shift.x.toDouble(), shift.y.toDouble())
    }

    val width: Float
        get() = max.x - min.x
    val height: Float
        get() = max.y - min.y
    val area: Float
        get() = height * width
    val centerX: Float
        get() = min.x + width * 0.5f
    val centerY: Float
        get() = min.y + height * 0.5f
    val center: Vec2f
        get() = Vec2f(centerX, centerY)

    fun copy(): BoundingBox2 {
        return BoundingBox2(this)
    }
}