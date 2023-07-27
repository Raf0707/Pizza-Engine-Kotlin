package pize.tests.voxelgame.client.control.camera

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.entity.LocalPlayer

class ThirdPersonFrontCameraTarget(private val targetPlayer: LocalPlayer?) : CameraTarget {
    private override val position: Vec3f
    private override val rotation: EulerAngles

    init {
        position = Vec3f()
        rotation = EulerAngles()
    }

    override fun getPosition(): Vec3f {
        val dist = 5f
        position.set(targetPlayer.getLerpPosition()).add(0f, targetPlayer.getEyeHeight(), 0f)
            .add(targetPlayer.getRotation().direction.mul(dist))
        return position
    }

    override fun getRotation(): EulerAngles {
        rotation.set(EulerAngles().setDirection(targetPlayer.getRotation().direction.mul(-1)))
        return rotation
    }
}
