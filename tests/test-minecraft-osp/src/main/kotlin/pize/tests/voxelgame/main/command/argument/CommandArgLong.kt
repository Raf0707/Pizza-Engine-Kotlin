package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandArgLong : CommandArg() {
    // Результат парсинга
    var long: Long = 0
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Если количество частей меньше 1 (число), завершить парсинг
        if (args.size < 1) return 0

        // устанавливаем число
        long = args[0].toLong()
        return args[0].length
    }
}
