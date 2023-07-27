package pize.tests.voxelgame.main.command.source

import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.player.ServerPlayer

abstract class CommandSource {
    abstract val position: Vec3f
    abstract fun sendMessage(message: Component?)
    fun asPlayer(): ServerPlayer? {
        return (this as CommandSourcePlayer).player
    }
}
