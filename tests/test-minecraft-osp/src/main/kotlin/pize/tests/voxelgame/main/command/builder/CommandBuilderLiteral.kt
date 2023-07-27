package pize.tests.voxelgame.main.command.builder

import pize.tests.voxelgame.main.command.Command
import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.node.CommandNodeArg
import pize.tests.voxelgame.main.command.node.CommandNodeLiteral

class CommandBuilderLiteral(command: String) {
    private val commandRoot: CommandNodeLiteral

    init {
        commandRoot = CommandNodeLiteral(command)
    }

    fun then(literal: String, argumentType: CommandArg): CommandBuilderLiteral {
        commandRoot.addChild(CommandNodeArg(literal, argumentType))
        return this
    }

    fun executes(command: Command?): CommandBuilderLiteral {
        return this
    }

    fun buildNode(): CommandNodeLiteral {
        return commandRoot
    }
}
