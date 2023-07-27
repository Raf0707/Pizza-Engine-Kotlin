package pize.tests.voxelgame.client.control.camera

import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.control.PlayerController
import pize.tests.voxelgame.client.options.KeyMapping

class HorizontalMoveController(private val playerController: PlayerController) {
    val motion: Vec3f

    init {
        motion = Vec3f()
    }

    fun update() {
        motion.zero()
        val session = playerController.session
        val options = session.options
        val player = session.game.player
        val dir = player.rotation.directionHorizontal
        if (options!!.getKey(KeyMapping.FORWARD)!!.isPressed) motion.add(dir)
        if (options.getKey(KeyMapping.BACK)!!.isPressed) motion.sub(dir)
        if (options.getKey(KeyMapping.LEFT)!!.isPressed) motion.sub(dir.z, 0f, -dir.x)
        if (options.getKey(KeyMapping.RIGHT)!!.isPressed) motion.add(dir.z, 0f, -dir.x)
        motion.nor()
    }
}
