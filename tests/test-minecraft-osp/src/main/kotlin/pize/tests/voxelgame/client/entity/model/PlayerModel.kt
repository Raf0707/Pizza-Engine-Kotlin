package pize.tests.voxelgame.client.entity.model

import pize.Pize.dt
import pize.math.Mathc.cos
import pize.math.Mathc.sin
import pize.math.Maths
import pize.math.vecmath.vector.Vec3f.len2
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.control.camera.PerspectiveType
import pize.tests.voxelgame.client.entity.AbstractClientPlayer
import pize.tests.voxelgame.client.level.ClientLevel

class PlayerModel(player: AbstractClientPlayer) : HumanoidModel(player) {
    private val jacket: ModelPart
    private val hat: ModelPart
    private val leftPants: ModelPart
    private val rightPants: ModelPart
    private val leftSleeve: ModelPart
    private val rightSleeve: ModelPart

    init {
        val scale = 1 + t
        hat = ModelPart(
            BoxBuilder(-4 * w, 0 * w, -4 * w, 4 * w, 8 * w, 4 * w)
                .nx(1f, 1f, 1f, 1f, 56 * t, 8 * t, 64 * t, 16 * t)
                .px(1f, 1f, 1f, 1f, 40 * t, 8 * t, 48 * t, 16 * t)
                .ny(1f, 1f, 1f, 1f, 48 * t, 0 * t, 56 * t, 8 * t)
                .py(1f, 1f, 1f, 1f, 40 * t, 0 * t, 48 * t, 8 * t)
                .pz(1f, 1f, 1f, 1f, 48 * t, 8 * t, 56 * t, 16 * t)
                .nz(1f, 1f, 1f, 1f, 32 * t, 8 * t, 40 * t, 16 * t)
                .end()
        )
        hat.setParent(head)
        hat.pose.scale.set(scale * 1.05)
        jacket = ModelPart(
            BoxBuilder(-2 * w, -6 * w, -4 * w, 2 * w, 6 * w, 4 * w)
                .nx(1f, 1f, 1f, 1f, 32 * t, 36 * t, 40 * t, 48 * t)
                .px(1f, 1f, 1f, 1f, 20 * t, 36 * t, 28 * t, 48 * t)
                .ny(1f, 1f, 1f, 1f, 28 * t, 32 * t, 36 * t, 36 * t)
                .py(1f, 1f, 1f, 1f, 20 * t, 32 * t, 28 * t, 36 * t)
                .pz(1f, 1f, 1f, 1f, 28 * t, 36 * t, 32 * t, 48 * t)
                .nz(1f, 1f, 1f, 1f, 16 * t, 36 * t, 20 * t, 48 * t)
                .end()
        )
        jacket.setParent(torso)
        jacket.pose.scale.set(scale * 1.04)
        leftPants = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 12 * t, 52 * t, 16 * t, 64 * t)
                .px(1f, 1f, 1f, 1f, 4 * t, 52 * t, 8 * t, 64 * t)
                .ny(1f, 1f, 1f, 1f, 8 * t, 48 * t, 12 * t, 52 * t)
                .py(1f, 1f, 1f, 1f, 4 * t, 48 * t, 8 * t, 52 * t)
                .pz(1f, 1f, 1f, 1f, 8 * t, 52 * t, 12 * t, 64 * t)
                .nz(1f, 1f, 1f, 1f, 0 * t, 52 * t, 4 * t, 64 * t)
                .end()
        )
        leftPants.setParent(leftLeg)
        leftPants.pose.scale.set(scale * 1.03)
        rightPants = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 12 * t, 36 * t, 16 * t, 48 * t)
                .px(1f, 1f, 1f, 1f, 4 * t, 36 * t, 8 * t, 48 * t)
                .ny(1f, 1f, 1f, 1f, 8 * t, 32 * t, 12 * t, 36 * t)
                .py(1f, 1f, 1f, 1f, 4 * t, 32 * t, 8 * t, 36 * t)
                .pz(1f, 1f, 1f, 1f, 8 * t, 36 * t, 12 * t, 48 * t)
                .nz(1f, 1f, 1f, 1f, 0 * t, 36 * t, 4 * t, 48 * t)
                .end()
        )
        rightPants.setParent(rightLeg)
        rightPants.pose.scale.set(scale * 1.02)
        leftSleeve = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 60 * t, 52 * t, 64 * t, 64 * t)
                .px(1f, 1f, 1f, 1f, 52 * t, 52 * t, 56 * t, 64 * t)
                .ny(1f, 1f, 1f, 1f, 56 * t, 48 * t, 60 * t, 52 * t)
                .py(1f, 1f, 1f, 1f, 52 * t, 48 * t, 56 * t, 52 * t)
                .pz(1f, 1f, 1f, 1f, 56 * t, 52 * t, 60 * t, 64 * t)
                .nz(1f, 1f, 1f, 1f, 48 * t, 52 * t, 52 * t, 64 * t)
                .end()
        )
        leftSleeve.setParent(leftArm)
        leftSleeve.pose.scale.set(scale * 1.01)
        rightSleeve = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 52 * t, 36 * t, 56 * t, 48 * t)
                .px(1f, 1f, 1f, 1f, 44 * t, 36 * t, 48 * t, 48 * t)
                .ny(1f, 1f, 1f, 1f, 48 * t, 32 * t, 52 * t, 36 * t)
                .py(1f, 1f, 1f, 1f, 44 * t, 32 * t, 48 * t, 36 * t)
                .pz(1f, 1f, 1f, 1f, 48 * t, 36 * t, 52 * t, 48 * t)
                .nz(1f, 1f, 1f, 1f, 40 * t, 36 * t, 44 * t, 48 * t)
                .end()
        )
        rightSleeve.setParent(rightArm)
        rightSleeve.pose.scale.set(scale * 1.01)
    }

    override fun render(camera: GameCamera?) {
        super.render(camera)
        jacket.render(camera, shader, "u_model")
        hat.render(camera, shader, "u_model")
        leftPants.render(camera, shader, "u_model")
        rightPants.render(camera, shader, "u_model")
        leftSleeve.render(camera, shader, "u_model")
        rightSleeve.render(camera, shader, "u_model")
    }

    fun animate() {
        // Position & Rotation
        torso.position.set(player.lerpPosition)
        head.position.set(player.lerpPosition)
        val session = (player.level as ClientLevel).session
        val options = session.options
        val camera = session.game.camera
        if (options!!.isFirstPersonModel && camera!!.perspective == PerspectiveType.FIRST_PERSON) {
            val offset = player.rotation.directionHorizontal.mul(-4 * w)
            torso.position.add(offset)
            head.isShow = false
        } else head.isShow = true
        torso.rotation.yaw += (-player.lerpRotation.yaw - torso.rotation.yaw) * dt * 4
        head.rotation.yaw = -player.lerpRotation.yaw
        head.rotation.pitch = player.lerpRotation.pitch

        // Sneaking
        if (player.isSneaking) {
            leftLeg.rotation.pitch = 45f
            rightLeg.rotation.pitch = 45f
            torso.rotation.pitch = -30f
            torso.position.add(0f, -w * 2, 0f)
            head.position.add(
                3 * w * cos((-torso.rotation.yaw * Maths.ToRad).toDouble()),
                -w * 3,
                3 * w * sin((-torso.rotation.yaw * Maths.ToRad).toDouble())
            )
        } else {
            torso.rotation.pitch = 0f
            leftLeg.rotation.pitch = 0f
            rightLeg.rotation.pitch = 0f
        }
        val gameTime = (player.level as ClientLevel).session.game.time
        val animationTime = gameTime.seconds * 2

        // Animation
        val velocity = player.velocity
        if (velocity.len2() > 10E-5) {
            val animationSpeed: Double
            animationSpeed = if (player.isSprinting) 3.0 else if (player.isSneaking) 0.5 else 2.0
            rightArm.rotation.pitch = 60 * sin(animationTime.toDouble())
            leftArm.rotation.pitch = -60 * sin(animationTime.toDouble())
            rightLeg.rotation.pitch = -60 * sin(animationTime.toDouble())
            leftLeg.rotation.pitch = 60 * sin(animationTime.toDouble())
        } else {
            rightArm.rotation.pitch -= rightArm.rotation.pitch / 10
            leftArm.rotation.pitch -= leftArm.rotation.pitch / 10
            rightLeg.rotation.pitch -= rightLeg.rotation.pitch / 10
            leftLeg.rotation.pitch -= leftLeg.rotation.pitch / 10
        }
    }

    override fun dispose() {
        super.dispose()
    }

    companion object {
        private const val t = 1 / 64f // Pixel size on texture
        private const val w = 1.8f / 32 // Pixel size in world
    }
}
