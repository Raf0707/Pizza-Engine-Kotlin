package pize.tests.terraria.entity

import pize.graphics.util.batch.TextureBatch
import pize.physic.BoundingBox2
import pize.physic.RectBody
import pize.physic.Velocity2f

abstract class Entity(rect: BoundingBox2?) : RectBody(rect) {
    val velocity: Velocity2f

    init {
        velocity = Velocity2f()
    }

    abstract fun render(batch: TextureBatch)
}
