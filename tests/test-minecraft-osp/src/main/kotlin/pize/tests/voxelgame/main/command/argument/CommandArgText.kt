package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandArgText : CommandArg() {
    // Результат парсинга
    var text: String? = null
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        text = remainingChars
        return remainingChars.length
    }
}