package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandArgWord : CommandArg() {
    // Результат парсинга
    var word: String? = null
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Если количество частей меньше 1 (слово), завершить парсинг
        if (args.size < 1) return 0

        // устанавливаем слово
        word = args[0]
        return word!!.length
    }
}
