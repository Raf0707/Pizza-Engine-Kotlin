package pize.tests.voxelgame.main.command.argument

import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

class CommandArgPosition : CommandArg() {
    // Результат парсинга
    var position: Vec3f? = null
        private set

    override fun parse(remainingChars: String, source: CommandSource, server: Server): Int {
        // Разделяем оставшуюся часть команды на части
        val args = remainingChars.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        // Если количество частей меньше трех (X Y Z), завершить парсинг
        return if (args.size < 3) 0 else try {
            var parsedLength = 2 // Кол-во пробелов между X Y Z

            // X
            var x = 0f
            if (args[0].startsWith("~")) { // Если координата относительная
                parsedLength++
                if (args[0].length != 1) // Если в координате есть не только '~', но и число
                    x = args[0].substring(1).toFloat()
                x += source.getPosition().x
            } else x = args[0].toFloat()

            // Y
            var y = 0f
            if (args[1].startsWith("~")) {
                parsedLength++
                if (args[1].length != 1) y = args[1].substring(1).toFloat()
                y += source.getPosition().y
            } else y = args[1].toFloat()

            // Z
            var z = 0f
            if (args[2].startsWith("~")) {
                parsedLength++
                if (args[2].length != 1) z = args[2].substring(1).toFloat()
                z += source.getPosition().z
            } else z = args[2].toFloat()

            // Если парсинг удался - устанавливаем позицию
            position = Vec3f(x, y, z)
            // возвращаем количество символом, которые удалось преобразовать
            args[0].length + args[1].length + args[2].length + parsedLength
        } catch (e: Exception) {
            0
        }
    }
}
