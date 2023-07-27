package pize.tests.voxelgame.server

import java.util.*

object CommandUtils {
    fun joinArgs(args: Array<String?>, beginIndex: Int): String {
        val joiner = StringJoiner(" ")
        for (i in beginIndex until args.size) joiner.add(args[i])
        return joiner.toString()
    }
}
