package pize.tests.voxelgame.client.control

import pize.graphics.camera.controller.Rotation3DController
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.control.camera.HorizontalMoveController
import pize.tests.voxelgame.client.control.camera.PerspectiveType
import pize.tests.voxelgame.client.entity.LocalPlayer
import pize.tests.voxelgame.client.options.KeyMapping
import pize.tests.voxelgame.main.net.packet.SBPacketPlayerSneaking
import pize.util.time.Stopwatch

class PlayerController(val session: Minecraft) {
    private var player: LocalPlayer? = null
    val rotationController: Rotation3DController
    private val horizontalMoveController: HorizontalMoveController
    private val prevJumpTime: Stopwatch

    init {
        rotationController = Rotation3DController()
        horizontalMoveController = HorizontalMoveController(this)
        prevJumpTime = Stopwatch()
    }

    fun update() {
        if (player == null) return
        val options = session.options

        // Rotation
        rotationController.update()
        player.getRotation().set(rotationController.rotation)

        // Horizontal motion
        horizontalMoveController.update()
        val motion = horizontalMoveController.motion
        player!!.moveControl(motion)

        // Jump, Sprint, Sneak
        if (options!!.getKey(KeyMapping.JUMP)!!.isDown) {
            player.setJumping(true)

            // Activate Flying
            if (player!!.isFlyEnabled) {
                if (prevJumpTime.millis < 350) player.setFlying(!player!!.isFlying)
                prevJumpTime.stop().reset().start()
            }
        } else if (options.getKey(KeyMapping.JUMP)!!.isReleased) player.setJumping(false)
        if (options.getKey(KeyMapping.SPRINT)!!.isPressed && options.getKey(KeyMapping.FORWARD)!!.isPressed ||
            options.getKey(KeyMapping.SPRINT)!!.isPressed && options.getKey(KeyMapping.FORWARD)!!.isDown
        ) player.setSprinting(true) else if (options.getKey(
                KeyMapping.FORWARD
            )!!.isReleased
        ) player.setSprinting(false)
        if (options.getKey(KeyMapping.SNEAK)!!.isDown) {
            player!!.setSneaking(true)
            session.game.sendPacket(SBPacketPlayerSneaking(player!!))
        } else if (options.getKey(KeyMapping.SNEAK)!!.isReleased) {
            player!!.setSneaking(false)
            session.game.sendPacket(SBPacketPlayerSneaking(player!!))
        }

        // Toggle perspective
        val camera = session.game.camera
        if (options.getKey(KeyMapping.TOGGLE_PERSPECTIVE)!!.isDown) {
            when (camera!!.perspective) {
                PerspectiveType.FIRST_PERSON -> camera.setPerspective(PerspectiveType.THIRD_PERSON_BACK)
                PerspectiveType.THIRD_PERSON_BACK -> camera.setPerspective(PerspectiveType.THIRD_PERSON_FRONT)
                PerspectiveType.THIRD_PERSON_FRONT -> camera.setPerspective(PerspectiveType.FIRST_PERSON)
            }
        }
    }

    fun setTargetPlayer(player: LocalPlayer?) {
        this.player = player
    }
}
