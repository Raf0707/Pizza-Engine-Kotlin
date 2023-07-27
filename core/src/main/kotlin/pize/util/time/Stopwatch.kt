package pize.util.time

import pize.math.Maths

class Stopwatch {
    private var lastNanos: Long = 0
    private var nanos: Long = 0
    private var started = false
    private var paused = false
    fun start(): Stopwatch {
        if (started) return this
        started = true
        lastNanos = System.nanoTime()
        nanos = lastNanos
        return this
    }

    fun reset(): Stopwatch {
        lastNanos = System.nanoTime()
        nanos = lastNanos
        return this
    }

    fun stop(): Stopwatch {
        if (started) started = false
        return this
    }

    fun pause(): Stopwatch {
        paused = true
        return this
    }

    fun resume(): Stopwatch {
        paused = false
        return this
    }

    fun setNanos(nanos: Long): Stopwatch {
        lastNanos = this.nanos - nanos
        return this
    }

    fun setMillis(millis: Double): Stopwatch {
        return setNanos(Maths.round(millis * 1000000).toLong())
    }

    fun setSeconds(seconds: Double): Stopwatch {
        return setMillis(seconds * 1000)
    }

    fun setMinutes(minutes: Double): Stopwatch {
        return setSeconds(minutes * 60)
    }

    fun setHours(hours: Double): Stopwatch {
        return setMinutes(hours * 60)
    }

    fun getNanos(): Long {
        if (!started) return 0
        if (!paused) nanos = System.nanoTime()
        return nanos - lastNanos
    }

    val millis: Double
        get() = getNanos() / 1000000.0
    val seconds: Double
        get() = millis / 1000
    val minutes: Double
        get() = seconds / 60
    val hours: Double
        get() = minutes / 60
}
