package pize.tests.voxelgame.client.entity.model

import pize.math.util.EulerAngles
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.control.camera.GameCamera

class Pose {
    val position: Vec3f
    val scale: Vec3f
    val rotation: EulerAngles
    private val translateMatrix: Matrix4f
    private val scaleMatrix: Matrix4f
    private val poseModelMatrix: Matrix4f
    val modelMatrix: Matrix4f

    init {
        position = Vec3f()
        scale = Vec3f(1, 1, 1)
        rotation = EulerAngles()
        translateMatrix = Matrix4f()
        scaleMatrix = Matrix4f()
        poseModelMatrix = Matrix4f()
        modelMatrix = Matrix4f()
    }

    fun updateMatrices(camera: GameCamera?, initial: Pose) {
        translateMatrix.toTranslated(position)
        scaleMatrix.toScaled(scale)
        poseModelMatrix
            .identity()
            .mul(initial.poseModelMatrix)
            .mul(translateMatrix).mul(scaleMatrix).mul(rotation.toMatrix())
        modelMatrix
            .set(Matrix4f().toTranslated(-camera!!.x, 0f, -camera.z))
            .mul(poseModelMatrix)
    }

    fun updateMatrices(camera: GameCamera?, initial: Pose, parent: Pose) {
        translateMatrix.toTranslated(position)
        scaleMatrix.toScaled(scale)
        poseModelMatrix
            .identity()
            .mul(parent.poseModelMatrix)
            .mul(initial.poseModelMatrix)
            .mul(translateMatrix).mul(scaleMatrix).mul(rotation.toMatrix())
        modelMatrix
            .set(Matrix4f().toTranslated(-camera!!.x, 0f, -camera.z))
            .mul(poseModelMatrix)
    }

    operator fun set(position: Vec3f?, rotation: EulerAngles?) {
        this.position.set(position!!)
        this.rotation.set(rotation!!)
    }

    fun set(pose: Pose) {
        set(pose.position, pose.rotation)
    }
}
