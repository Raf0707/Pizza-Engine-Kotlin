package pize.util.time

import pize.util.Utils

class PizeRunnable(private val runnable: Runnable) {
    private var interrupt = false
    fun runLater(delayMillis: Long) {
        Utils.delayMillis(delayMillis)
        runnable.run()
    }

    fun runTimer(delayMillis: Long, periodMillis: Long) {
        runLater(delayMillis)
        while (!Thread.interrupted() && !interrupt) runLater(periodMillis)
    }

    fun runLaterAsync(delayMillis: Long) {
        val thread = Thread({ runLater(delayMillis) }, "PizeRunnable-Thread")
        thread.setDaemon(true)
        thread.start()
    }

    fun runTimerAsync(delayMillis: Long, periodMillis: Long) {
        val thread = Thread({ runTimer(delayMillis, periodMillis) }, "PizeRunnable-Thread")
        thread.setDaemon(true)
        thread.start()
    }

    fun stop() {
        interrupt = true
    }
}