package pize.physic

import pize.math.vecmath.vector.Vec2f

class Velocity2f : Vec2f {
    var max: Float
        private set

    constructor() : super() {
        max = 1f
    }

    constructor(vector: Vec2f?) : super(vector) {
        max = 1f
    }

    fun collidedAxesToZero(collidedVelocity: Vec2f): Velocity2f {
        if (x != 0f && collidedVelocity.x == 0f) x = 0f
        if (y != 0f && collidedVelocity.y == 0f) y = 0f
        return this
    }

    fun clampToMax(): Velocity2f {
        val normalized = copy().nor().abs()
        if (x > max * normalized.x) x = max * normalized.x else if (x < -max * normalized.x) x = -max * normalized.x
        if (y > max * normalized.y) y = max * normalized.y else if (y < -max * normalized.y) y = -max * normalized.y
        return this
    }

    fun reduce(reduce: Double): Velocity2f {
        return reduce(reduce, reduce)
    }

    fun reduce(reduceX: Double, reduceY: Double): Velocity2f {
        val normalized = copy().nor().abs()
        var r: Double
        if (reduceX != 0.0) {
            r = reduceX * normalized.x
            if (x > 0) {
                if (x >= r) x -= r.toFloat() else x = 0f
            } else if (x < 0) {
                if (x <= -r) x += r.toFloat() else x = 0f
            }
        }
        if (reduceY != 0.0) {
            r = reduceY * normalized.y
            if (y > 0) {
                if (y >= r) y -= r.toFloat() else y = 0f
            } else if (y < 0) {
                if (y <= -r) y += r.toFloat() else y = 0f
            }
        }
        return this
    }

    fun setMax(max: Float): Velocity2f {
        this.max = max
        return this
    }

    override fun copy(): Velocity2f {
        return Velocity2f(this)
    }
}