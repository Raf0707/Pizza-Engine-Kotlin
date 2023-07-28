package pize.graphics.camera

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f

abstract class Camera3D(width: Int, height: Int) : Camera(width, height) {

    val position: Vec3f
    val rotation: EulerAngles

    abstract var far: Float
    abstract var near: Float
    abstract var fieldOfView: Float

    init {
        position = Vec3f()
        rotation = EulerAngles()
    }

    val x: Float; get() = position.x
    val y: Float; get() = position.y
    val z: Float; get() = position.z

}
