package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.main.net.PlayerProfile
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.player.ServerPlayer

class CommandArgPlayer : CommandArg() {
    // Результат парсинга
    var player: ServerPlayer? = null
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // Если количество частей меньше 1 (имя игрока), завершить парсинг
        if (args.size < 1) return 0

        // Находим игрока
        val playerName = args[0]
        if (PlayerProfile.Companion.isNameInvalid(playerName)) return 0
        player = server.playerList.getPlayer(playerName)
        return if (player == null) 0 else playerName.length
    }
}
