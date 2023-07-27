package pize.util.time

class TickGenerator(ticksPerSecond: Float) {
    private val sync: Sync
    private var interrupt = false

    init {
        sync = Sync(ticksPerSecond.toDouble())
    }

    fun setTPS(ticksPerSecond: Float) {
        sync.setFps(ticksPerSecond.toDouble())
    }

    fun start(tickable: Tickable) {
        interrupt = false
        while (!Thread.interrupted() && !interrupt) {
            tickable.tick()
            sync.sync()
        }
    }

    fun startAsync(tickable: Tickable) {
        val thread = Thread { start(tickable) }
        thread.setDaemon(true)
        thread.start()
    }

    fun stop() {
        interrupt = true
    }
}
