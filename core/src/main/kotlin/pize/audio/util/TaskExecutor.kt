package pize.audio.util

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer

class TaskExecutor {
    private val taskQueue: ConcurrentLinkedQueue<Runnable>
    private var stopped = false

    init {
        taskQueue = ConcurrentLinkedQueue()
    }

    fun newTask(runnable: Runnable) {
        if (!stopped) taskQueue.add(runnable)
    }

    fun executeTasks() {
        if (stopped) return
        taskQueue.forEach(Consumer { task: Runnable? -> Objects.requireNonNull(taskQueue.poll()).run() })
    }

    fun dispose() {
        stopped = true
        taskQueue.clear()
    }
}
