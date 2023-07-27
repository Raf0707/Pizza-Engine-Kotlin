package pize.tests.voxelgame.main.command.builder

import pize.tests.voxelgame.main.command.Command
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.node.CommandNodeArg

class CommandBuilderArg(name: String, argument: CommandArg) {
    private val commandRoot: CommandNodeArg

    init {
        commandRoot = CommandNodeArg(name, argument)
    }

    fun then(literal: String, argument: CommandArg): CommandBuilderArg {
        commandRoot.addChild(CommandNodeArg(literal, argument))
        return this
    }

    fun executes(command: Command?): CommandBuilderArg {
        return this
    }

    fun buildArg(): CommandNodeArg {
        return commandRoot
    }
}
