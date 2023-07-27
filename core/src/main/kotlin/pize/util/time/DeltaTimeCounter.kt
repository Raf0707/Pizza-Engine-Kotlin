package pize.util.time

import pize.math.Maths

class DeltaTimeCounter {
    private var lastTime: Long
    private var deltaTime = 0f

    init {
        lastTime = System.nanoTime()
    }

    fun update() {
        val currentTime = System.nanoTime()
        deltaTime = (currentTime - lastTime) / Maths.NanosInSecond
        lastTime = currentTime
    }

    fun get(): Float {
        return deltaTime
    }
}
