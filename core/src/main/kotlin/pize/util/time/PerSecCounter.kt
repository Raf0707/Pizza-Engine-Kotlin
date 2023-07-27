package pize.util.time

class PerSecCounter {
    private var prevTime: Long = 0
    private var counter = 0
    private var count = 0
    fun count() {
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - prevTime
        if (difference > 1000) {
            prevTime = currentTime
            count = counter
            counter = 0
        } else counter++
    }

    fun get(): Int {
        return count
    }
}
