package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher
import java.util.*

object CommandList {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("list")
                .executes { obj: CommandContext? -> sendList() }
        )
    }

    private fun sendList(context: CommandContext) {
        val source = context.source
        val joiner = StringJoiner(", ")
        val players = context.server.playerList.players
        for (player in players!!) joiner.add(player.name)
        source!!.sendMessage(Component().color(TextColor.YELLOW).text("Players: ").reset().text(joiner.toString()))
    }
}
