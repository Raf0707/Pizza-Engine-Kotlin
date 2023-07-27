package pize.tests.voxelgame.main.command.node

import pize.tests.voxelgame.main.command.Command
import pize.tests.voxelgame.main.command.argument.CommandArg

class CommandNodeArg(literal: String, val argument: CommandArg) : CommandNodeLiteral(literal) {

    override fun then(node: CommandNodeLiteral?): CommandNodeArg {
        super.then(node)
        return this
    }

    override fun executes(command: Command?): CommandNodeArg {
        super.executes(command)
        return this
    }
}
