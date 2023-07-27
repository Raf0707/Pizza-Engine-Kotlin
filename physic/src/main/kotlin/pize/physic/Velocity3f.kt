package pize.physic

import pize.math.vecmath.vector.Vec3f

class Velocity3f : Vec3f {
    var max: Float
        private set

    constructor() : super() {
        max = 1f
    }

    constructor(vector: Vec3f?) : super(vector) {
        max = 1f
    }

    fun collidedAxesToZero(collidedVelocity: Vec3f?): Velocity3f {
        if (collidedVelocity == null) return this
        if (x != 0f && collidedVelocity.x == 0f) x = 0f
        if (y != 0f && collidedVelocity.y == 0f) y = 0f
        if (z != 0f && collidedVelocity.z == 0f) z = 0f
        return this
    }

    @kotlin.jvm.JvmOverloads
    fun clampToMax(clampX: Boolean = true, clampY: Boolean = true, clampZ: Boolean = true): Velocity3f {
        val normalized = copy().nor().abs()
        if (clampX) {
            if (x > max * normalized.x) x = max * normalized.x else if (x < -max * normalized.x) x = -max * normalized.x
        }
        if (clampY) {
            if (y > max * normalized.y) y = max * normalized.y else if (y < -max * normalized.y) y = -max * normalized.y
        }
        if (clampZ) {
            if (z > max * normalized.z) z = max * normalized.z else if (z < -max * normalized.z) z = -max * normalized.z
        }
        return this
    }

    fun reduce(reduce: Double): Velocity3f {
        return reduce(reduce, reduce, reduce)
    }

    fun reduceXZ(reduce: Double): Velocity3f {
        return reduce(reduce, 0.0, reduce)
    }

    fun reduce(reduceX: Double, reduceY: Double, reduceZ: Double): Velocity3f {
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
        if (reduceZ != 0.0) {
            r = reduceZ * normalized.z
            if (z > 0) {
                if (z >= r) z -= r.toFloat() else z = 0f
            } else if (z < 0) {
                if (z <= -r) z += r.toFloat() else z = 0f
            }
        }
        return this
    }

    fun setMax(max: Float): Velocity3f {
        this.max = max
        return this
    }

    override fun copy(): Velocity3f {
        return Velocity3f(this)
    }
}