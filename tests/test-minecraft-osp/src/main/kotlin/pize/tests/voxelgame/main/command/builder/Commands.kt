package pize.tests.voxelgame.main.command.builder

import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.node.CommandNodeArg
import pize.tests.voxelgame.main.command.node.CommandNodeLiteral

object Commands {
    fun literal(literal: String): CommandNodeLiteral? {
        return CommandBuilderLiteral(literal).buildNode()
    }

    fun argument(name: String, type: CommandArg): CommandNodeArg? {
        return CommandBuilderArg(name, type).buildArg()
    }
}
