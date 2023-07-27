package pize.tests.voxelgame.main.command

import pize.tests.voxelgame.main.command.argument.CommandArg
import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandContext(
    val server: Server,
    val source: CommandSource,
    val command: String,
    private val arguments: List<CommandArg?>
) {
    fun getArg(index: Int): CommandArg? {
        return arguments[index]
    }
}
