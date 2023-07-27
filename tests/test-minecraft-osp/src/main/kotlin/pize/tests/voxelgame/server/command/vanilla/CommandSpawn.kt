package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.command.CommandDispatcher
import pize.tests.voxelgame.server.level.ServerLevel

object CommandSpawn {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("spawn")
                .requiresPlayer()
                .executes { obj: CommandContext? -> teleportToSpawn() }
        )
    }

    private fun teleportToSpawn(context: CommandContext) {
        // Player
        val sender = context.source.asPlayer()
        // Spawn position
        val level = sender.level as ServerLevel
        val spawnPosition = level.getSpawnPosition()
        // Teleport
        sender!!.teleport(spawnPosition)
        sender.sendMessage(Component().text("You teleported to spawn"))
    }
}
