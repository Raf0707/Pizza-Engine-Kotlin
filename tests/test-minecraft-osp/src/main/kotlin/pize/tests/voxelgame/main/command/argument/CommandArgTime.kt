package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.main.time.TimeUnit
import pize.tests.voxelgame.server.Server

class CommandArgTime : CommandArg() {
    // Результат парсинга
    var unit: TimeUnit? = null
        private set
    var time = 0f
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Если количество частей меньше 1 (время), завершить парсинг
        if (args.size < 1) return 0
        var timeString = args[0]

        // Устанавливаем еденицу измерения
        unit = null
        if (timeString.endsWith("d")) unit = TimeUnit.DAYS
        if (timeString.endsWith("m")) unit = TimeUnit.MINUTES
        if (timeString.endsWith("s")) unit = TimeUnit.SECONDS
        if (timeString.endsWith("t")) unit = TimeUnit.TICKS
        if (unit != null) timeString = timeString.substring(0, timeString.length - 1) else unit =
            TimeUnit.TICKS // Ticks - default

        // Устанавливаем время
        time = try {
            timeString.toFloat()
        } catch (ignored: Exception) {
            return 0
        }
        return args[0].length
    }
}
