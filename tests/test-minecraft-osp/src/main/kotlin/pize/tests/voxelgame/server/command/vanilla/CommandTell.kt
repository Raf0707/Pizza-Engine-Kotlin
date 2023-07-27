package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandTell {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("tell")
                .then(
                    Commands.argument("player", CommandArg.Companion.player())
                        .then(
                            Commands.argument("text", CommandArg.Companion.text())
                                .requiresPlayer()
                                .executes { obj: CommandContext? -> tell() }
                        )
                )
        )
    }

    private fun tell(context: CommandContext) {
        // Players
        val sender = context.source.asPlayer()
        val targetPlayer = context.getArg(0)!!.asPlayer().player
        val text = context.getArg(1)!!.asText().text
        // Tell
        targetPlayer!!.sendMessage(
            Component().color(TextColor.YELLOW).text("<" + sender.name + "> tells you: \"").reset().text(text)
                .color(TextColor.YELLOW).text("\"")
        )
    }
}
