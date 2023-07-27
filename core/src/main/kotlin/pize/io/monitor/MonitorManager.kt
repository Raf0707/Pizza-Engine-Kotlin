package pize.io.monitor

import org.lwjgl.glfw.GLFW

class MonitorManager private constructor() {
    private val monitors: MutableMap<Long, Monitor>

    init {
        monitors = HashMap(2)
        val primaryMonitorID = GLFW.glfwGetPrimaryMonitor()
        monitors[primaryMonitorID] = Monitor(primaryMonitorID)
        GLFW.glfwSetMonitorCallback { monitorID: Long, event: Int ->
            when (event) {
                GLFW.GLFW_CONNECTED -> monitors[monitorID] = Monitor(monitorID)
                GLFW.GLFW_DISCONNECTED -> monitors.remove(monitorID)
            }
        }
    }

    companion object {
        private var instance: MonitorManager? = null
        fun init() {
            instance = MonitorManager()
        }

        fun getMonitor(id: Long): Monitor? {
            return instance!!.monitors[id]
        }

        val primary: Monitor?
            get() = instance!!.monitors[GLFW.glfwGetPrimaryMonitor()]

        fun getMonitors(): Collection<Monitor> {
            return instance!!.monitors.values
        }
    }
}
