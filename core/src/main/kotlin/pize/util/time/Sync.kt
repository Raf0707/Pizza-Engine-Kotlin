package pize.util.time

import pize.Pize

class Sync @JvmOverloads constructor(fps: Double = Pize.monitor().refreshRate.toDouble()) {
    private var prevTime: Long = 0
    private var frameNano: Long = 0
    var isEnabled = false
        private set

    init {
        setFps(fps)
    }

    fun setFps(fps: Double) {
        frameNano = (1000000000 / fps).toLong() // Время между кадрами, при [fps] количестве тпс
        prevTime = System.nanoTime() // Для подсчета времени между кадрами
        isEnabled = true
    }

    fun sync() {
        if (!isEnabled) return
        val deltaNano = System.nanoTime() - prevTime // Текущее время между кадрами
        val sleepNano = frameNano - deltaNano // Время для коррекции тпс
        if (sleepNano > 0L) {
            // Коррекция
            val startTime = System.nanoTime()
            var elapsed: Long
            do {
                elapsed = System.nanoTime() - startTime
            } while (elapsed < sleepNano)
        }
        prevTime = System.nanoTime()
    }

    fun enable(value: Boolean) {
        isEnabled = value
    }
}