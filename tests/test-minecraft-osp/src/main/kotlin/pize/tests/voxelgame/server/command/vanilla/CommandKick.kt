package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.net.packet.CBPacketDisconnect
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandKick {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(
            Commands.literal("kick")
                .then(
                    Commands.argument("player", CommandArg.Companion.player())
                        .then(
                            Commands.argument("reason", CommandArg.Companion.text())
                                .requiresPlayer()
                                .executes { obj: CommandContext? -> kick() }
                        )
                )
        )
    }

    private fun kick(context: CommandContext) {
        // Players
        val sender = context.source.asPlayer()
        val targetPlayer = context.getArg(0)!!.asPlayer().player
        // Reason
        val reason = context.getArg(1)!!.asText().text
        // Kick
        targetPlayer!!.sendPacket(CBPacketDisconnect("You been kicked by " + sender.name + ", reason: " + reason))
        context.server.playerList.broadcastServerMessage(
            Component().color(TextColor.DARK_RED)
                .text(targetPlayer.name + " was kicked by " + sender.name + ", reason: " + reason)
        )
    }
}
