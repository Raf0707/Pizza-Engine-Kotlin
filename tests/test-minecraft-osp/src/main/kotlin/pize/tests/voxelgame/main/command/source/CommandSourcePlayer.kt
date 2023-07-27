package pize.tests.voxelgame.main.command.source

import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.player.ServerPlayer

class CommandSourcePlayer(val player: ServerPlayer) : CommandSource() {

    override val position: Vec3f
        get() = player.position

    override fun sendMessage(message: Component?) {
        player.sendMessage(message)
    }
}
