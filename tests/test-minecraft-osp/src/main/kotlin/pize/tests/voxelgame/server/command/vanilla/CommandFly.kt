package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandFly {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("fly")
                .requiresPlayer()
                .executes { obj: CommandContext? -> toggleFly() }
        )
    }

    private fun toggleFly(context: CommandContext) {
        // Player
        val sender = context.source.asPlayer()
        // Fly
        if (sender!!.isFlyEnabled) {
            sender.setFlyEnabled(false)
            sender.sendMessage(Component().text("Fly disabled"))
        } else {
            sender.setFlyEnabled(true)
            sender.sendMessage(Component().text("Fly enabled"))
        }
    }
}
