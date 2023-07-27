package pize.tests.voxelgame.client.control.camera

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.entity.LocalPlayer

class ThirdPersonBackCameraTarget(private val targetPlayer: LocalPlayer?) : CameraTarget {
    private override val position: Vec3f
    private val direction: EulerAngles

    init {
        position = Vec3f()
        direction = EulerAngles()
    }

    override fun getPosition(): Vec3f {
        val dist = 5f
        position.set(targetPlayer.getLerpPosition())
            .add(0f, targetPlayer.getEyeHeight(), 0f)
            .add(targetPlayer.getRotation().direction.mul(-dist))
        return position
    }

    override val rotation: EulerAngles
        get() {
            direction.set(targetPlayer.getRotation())
            return direction
        }
}
