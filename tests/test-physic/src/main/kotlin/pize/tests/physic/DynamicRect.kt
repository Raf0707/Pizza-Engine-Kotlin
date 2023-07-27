package pize.tests.physic

import pize.physic.BoundingBox2
import pize.physic.RectBody
import pize.physic.Velocity2f

class DynamicRect(rect: BoundingBox2?) : RectBody(rect) {
    private val velocity: Velocity2f

    init {
        velocity = Velocity2f()
    }

    fun motion(): Velocity2f {
        return velocity
    }
}
