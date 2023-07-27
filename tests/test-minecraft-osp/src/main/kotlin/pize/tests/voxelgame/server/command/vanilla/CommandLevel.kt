package pize.tests.voxelgame.server.command.vanilla

import pize.tests.voxelgame.main.audio.Sound
import pize.tests.voxelgame.main.command.CommandContext
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.builder.Commands
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.chunk.gen.Generators
import pize.tests.voxelgame.server.command.CommandDispatcher
import java.util.*

object CommandLevel {
    fun registerTo(dispatcher: CommandDispatcher) {
        dispatcher.newCommand(Commands.literal("level")
            .then(
                Commands.literal("create")
                    .then(
                        Commands.argument("levelName", CommandArg.Companion.word())
                            .then(
                                Commands.argument("seed", CommandArg.Companion.word())
                                    .then(
                                        Commands.argument("generatorID", CommandArg.Companion.word())
                                            .executes { obj: CommandContext? -> createLevel() }
                                    )
                            )
                    )
            )
            .then(
                Commands.literal("goto")
                    .then(
                        Commands.argument("levelName", CommandArg.Companion.word())
                            .requiresPlayer()
                            .executes { obj: CommandContext? -> goToLevel() }
                    )
            )
            .then(
                Commands.literal("list")
                    .executes { obj: CommandContext? -> sendLevelList() }
            )
        )
    }

    private fun createLevel(context: CommandContext) {
        // Level name, seed, generator Type
        val levelName = context.getArg(0)!!.asWord().word
        val seedLiteral = context.getArg(1)!!.asWord().word
        val generatorID = context.getArg(2)!!.asWord().word
        val generator = Generators.fromID(generatorID)
        // Create level
        val sender = context.source.asPlayer()
        val levelManager = context.server.levelManager
        if (levelManager!!.isLevelLoaded(levelName)) sender!!.sendMessage(
            Component().color(TextColor.DARK_RED).text(
                "Level $levelName already loaded"
            )
        ) else {
            // Parse seed
            var seed = seedLiteral.hashCode()
            try {
                seed = seedLiteral!!.toInt()
            } catch (ignored: Exception) {
            }
            levelManager.createLevel(levelName, seed, generator)
            context.server.playerList.broadcastServerMessage(
                Component().color(TextColor.YELLOW).text(
                    "Level '$levelName' loaded"
                )
            )
        }
    }

    fun goToLevel(context: CommandContext) {
        // Level name
        val levelName = context.getArg(0)!!.asWord().word
        // Go to level
        val sender = context.source.asPlayer()
        val levelManager = context.server.levelManager
        if (!levelManager!!.isLevelLoaded(levelName)) sender!!.sendMessage(
            Component().color(TextColor.DARK_RED).text(
                "Level $levelName is not loaded"
            )
        ) else {
            val level = levelManager.getLevel(levelName)
            sender!!.teleport(level, level.getSpawnPosition())
            sender.sendMessage(Component().text("You teleported to level $levelName"))
            sender.playSound(Sound.LEVEL_UP, 1f, 1f)
        }
    }

    private fun sendLevelList(context: CommandContext) {
        // Levels
        val levelManager = context.server.levelManager
        val levels = levelManager!!.getLoadedLevels()

        // Create list
        val joiner = StringJoiner(", ")
        for (level in levels!!) joiner.add(level.configuration.name)

        // Send levels
        context.source.sendMessage(Component().color(TextColor.YELLOW).text("Levels: ").reset().text(joiner.toString()))
    }
}
