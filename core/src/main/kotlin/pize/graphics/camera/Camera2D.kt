package pize.graphics.camera

import pize.math.vecmath.vector.Vec2f

abstract class Camera2D(width: Int, height: Int) : Camera(width, height) {
    @JvmField
    val position: Vec2f

    init {
        position = Vec2f()
    }

    val x: Float
        get() = position.x
    val y: Float
        get() = position.y
    abstract val rotation: Float
    abstract val scale: Float
}
