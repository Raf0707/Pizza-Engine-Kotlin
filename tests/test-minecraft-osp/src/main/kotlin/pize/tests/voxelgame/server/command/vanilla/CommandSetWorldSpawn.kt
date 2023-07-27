package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandSetWorldSpawn {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("setworldspawn")
                .requiresPlayer()
                .executes { obj: CommandContext? -> setWorldSpawn() }
        )
    }

    private fun setWorldSpawn(context: CommandContext) {
        // Player
        val sender = context.source.asPlayer()
        // Spawn position
        val position = sender!!.position
        val level = sender.level
        // Set world spawn
        level.configuration.setWorldSpawn(position.x.toDouble(), position.z.toDouble())
        context.server.playerList.broadcastServerMessage(
            Component().color(TextColor.GREEN).text(
                "World spawn set in: $position"
            )
        )
    }
}
