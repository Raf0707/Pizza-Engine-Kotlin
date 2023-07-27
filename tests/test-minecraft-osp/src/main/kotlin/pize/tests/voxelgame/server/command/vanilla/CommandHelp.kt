package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandHelp {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("help")
                .executes { obj: CommandContext? -> sendHelp() }
        )
    }

    private fun sendHelp(context: CommandContext) {
        val source = context.source
        for (command in context.server.commandDispatcher.commands) {
            source!!.sendMessage(Component().text("/" + command.literal))
        }
    }
}
