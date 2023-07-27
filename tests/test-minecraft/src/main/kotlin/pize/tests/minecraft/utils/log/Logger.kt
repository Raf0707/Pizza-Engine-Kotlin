package pize.tests.minecraft.utils.log

import java.io.PrintStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Logger private constructor(private val out: PrintStream = System.out, private val err: PrintStream = System.err) {
    private val dateTimeFormatter: DateTimeFormatter

    private constructor(out: PrintStream) : this(out, out)

    fun trace(trace: String) {
        out.println(getLogPrefix(LogLevel.TRACE) + trace)
    }

    fun debug(debug: String) {
        out.println(getLogPrefix(LogLevel.DEBUG) + debug)
    }

    fun info(info: String) {
        out.println(getLogPrefix(LogLevel.INFO) + info)
    }

    fun warn(warn: String) {
        out.println(getLogPrefix(LogLevel.WARN) + warn)
    }

    fun error(error: String) {
        err.println(getLogPrefix(LogLevel.ERROR) + error)
    }

    fun trace(trace: String, `object`: Any) {
        out.println(getLogPrefix(LogLevel.TRACE) + trace.replace("\\{}".toRegex(), `object`.toString()))
    }

    fun debug(debug: String, `object`: Any) {
        out.println(getLogPrefix(LogLevel.DEBUG) + debug.replace("\\{}".toRegex(), `object`.toString()))
    }

    fun info(info: String, `object`: Any) {
        out.println(getLogPrefix(LogLevel.INFO) + info.replace("\\{}".toRegex(), `object`.toString()))
    }

    fun warn(warn: String, `object`: Any) {
        out.println(getLogPrefix(LogLevel.WARN) + warn.replace("\\{}".toRegex(), `object`.toString()))
    }

    fun error(error: String, `object`: Any) {
        err.println(getLogPrefix(LogLevel.ERROR) + error.replace("\\{}".toRegex(), `object`.toString()))
    }

    private fun getLogPrefix(level: LogLevel): String {
        return "[" + ZonedDateTime.now()
            .format(dateTimeFormatter) + "] [" + Thread.currentThread().name + "/" + level.name + "]: "
    }

    init {
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    }

    companion object {
        private val instance = Logger()
        fun instance(): Logger {
            return instance
        }
    }
}
