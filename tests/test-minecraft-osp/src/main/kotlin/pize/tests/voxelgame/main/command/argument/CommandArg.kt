package pize.tests.voxelgame.main.command.argument

import pize.tests.voxelgame.main.command.source.CommandSource
import pize.tests.voxelgame.server.Server

abstract class CommandArg {
    // Должно возвращать длину строки, которую удалось разобрать, если не удалось - 0
    abstract fun parse(remainingChars: String, source: CommandSource, server: Server): Int
    fun asPosition(): CommandArgPosition {
        return this as CommandArgPosition
    }

    fun asPlayer(): CommandArgPlayer {
        return this as CommandArgPlayer
    }

    fun asText(): CommandArgText {
        return this as CommandArgText
    }

    fun asWord(): CommandArgWord {
        return this as CommandArgWord
    }

    fun asNumInt(): CommandArgInteger {
        return this as CommandArgInteger
    }

    fun asNumLong(): CommandArgLong {
        return this as CommandArgLong
    }

    fun asNumFloat(): CommandArgFloat {
        return this as CommandArgFloat
    }

    fun asTime(): CommandArgTime {
        return this as CommandArgTime
    }

    companion object {
        fun position(): CommandArgPosition {
            return CommandArgPosition()
        }

        fun player(): CommandArgPlayer {
            return CommandArgPlayer()
        }

        fun text(): CommandArgText {
            return CommandArgText()
        }

        fun word(): CommandArgWord {
            return CommandArgWord()
        }

        fun numInt(): CommandArgInteger {
            return CommandArgInteger()
        }

        fun numLong(): CommandArgLong {
            return CommandArgLong()
        }

        fun numFloat(): CommandArgFloat {
            return CommandArgFloat()
        }

        fun time(): CommandArgTime {
            return CommandArgTime()
        }
    }
}
