package pize.tests.voxelgame.server.command.vanilla

import pize.physic.BoxBody.position
import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.command.CommandDispatcher

object CommandTeleport {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(Commands.literal("tp")
            .then(
                Commands.argument("position", CommandArg.Companion.position())
                    .requiresPlayer()
                    .executes { obj: CommandContext? -> teleportToPosition() }
            )
            .then(Commands.argument("player", CommandArg.Companion.player())
                .requiresPlayer()
                .executes { obj: CommandContext? -> teleportToPlayer() }
                .then(
                    Commands.argument("targetPlayer", CommandArg.Companion.player())
                        .executes { obj: CommandContext? -> teleportPlayerToPlayer() }
                )
                .then(
                    Commands.argument("position", CommandArg.Companion.position())
                        .executes { obj: CommandContext? -> teleportPlayerToPosition() }
                )
            )
        )
    }

    private fun teleportToPosition(context: CommandContext) {
        // Arguments
        val argPosition = context.getArg(0)!!.asPosition()

        // Sender, Position
        val sender = context.source.asPlayer()
        val position = argPosition.position
        // Teleport
        sender!!.teleport(position)
        sender.sendMessage(Component().text("You teleported to $position"))
    }

    private fun teleportToPlayer(context: CommandContext) {
        // Arguments
        val argPlayer = context.getArg(0)!!.asPlayer()

        // Players
        val sender = context.source.asPlayer()
        val targetPlayer = argPlayer.player
        // Teleport
        sender!!.teleport(targetPlayer)
        sender.sendMessage(Component().text("You teleported to " + targetPlayer.name))
        targetPlayer!!.sendMessage(Component().text("Player " + sender.name + " teleported to you"))
    }

    private fun teleportPlayerToPlayer(context: CommandContext) {
        // Arguments
        val argPlayer = context.getArg(0)!!.asPlayer()
        val argTargetPlayer = context.getArg(1)!!.asPlayer()

        // Players
        val player = argPlayer.player
        val targetPlayer = argTargetPlayer.player

        // Teleport
        player!!.teleport(targetPlayer)
        player.sendMessage(Component().text("You teleported to " + targetPlayer.name))
        targetPlayer!!.sendMessage(Component().text("Player " + player.name + " teleported to you"))
    }

    private fun teleportPlayerToPosition(context: CommandContext) {
        // Arguments
        val argPlayer = context.getArg(0)!!.asPlayer()
        val argPosition = context.getArg(1)!!.asPosition()

        // Player, Position
        val player = argPlayer.player
        val position = argPosition.position
        // Teleport
        player!!.teleport(position)
        player.sendMessage(Component().text("You teleported to $position"))
    }
}
