package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandArgFloat : CommandArg() {
    // Результат парсинга
    var float = 0f
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Если количество частей меньше 1 (число), завершить парсинг
        if (args.size < 1) return 0

        // устанавливаем число
        float = args[0].toFloat()
        return args[0].length
    }
}
