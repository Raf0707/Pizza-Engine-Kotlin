package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher
import pize.tests.voxelgame.server.level.ServerLevel

object CommandSeed {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("seed")
                .requiresPlayer()
                .executes { obj: CommandContext? -> sendSeed() }
        )
    }

    private fun sendSeed(context: CommandContext) {
        // Player
        val sender = context.source.asPlayer()
        sender!!.sendMessage(
            Component().color(TextColor.GREEN).text("World seed: " + (sender.level as ServerLevel).configuration.seed)
        )
    }
}
