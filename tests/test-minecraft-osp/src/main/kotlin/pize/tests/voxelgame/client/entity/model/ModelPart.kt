package pize.tests.voxelgame.client.entity.model

import pize.graphics.util.Shader
import pize.graphics.vertex.Mesh
import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.control.camera.GameCamera

class ModelPart(val mesh: Mesh?) {
    private var parent: ModelPart? = null
    val pose: Pose
    val initialPose: Pose
    var isShow: Boolean

    init {
        pose = Pose()
        initialPose = Pose()
        isShow = true
    }

    fun render(camera: GameCamera?, shader: Shader?, modelMatrixUniform: String?) {
        initialPose.updateMatrices(camera, initialPose)
        if (parent == null) pose.updateMatrices(camera, initialPose) else pose.updateMatrices(
            camera,
            initialPose,
            parent!!.pose
        )
        if (shader == null || !isShow) return
        shader.setUniform(modelMatrixUniform, pose.modelMatrix)
        mesh!!.render()
    }

    fun setParent(parent: ModelPart?) {
        this.parent = parent
    }

    fun setInitialPose(x: Float, y: Float, z: Float, yaw: Float, pitch: Float, roll: Float) {
        initialPose.position[x, y] = z
        initialPose.rotation[yaw, pitch] = roll
    }

    fun setInitialPose(position: Vec3f, rotation: EulerAngles) {
        setInitialPose(position.x, position.y, position.z, rotation.yaw, rotation.pitch, rotation.roll)
    }

    fun setInitialPose(x: Double, y: Double, z: Double) {
        initialPose.position[x, y] = z
    }

    fun setInitialPose(position: Vec3f) {
        setInitialPose(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
    }

    val position: Vec3f?
        get() = pose.position
    val rotation: EulerAngles?
        get() = pose.rotation
}
