package pize.io.joystick

import org.lwjgl.glfw.GLFW

class JoystickManager private constructor() {
    private val joysticks: MutableMap<Int, Joystick>

    init {
        joysticks = HashMap(MAX_JOYSTICKS)
        GLFW.glfwSetJoystickCallback { joystickID: Int, event: Int ->
            when (event) {
                GLFW.GLFW_CONNECTED -> {
                    joysticks[joystickID] = Joystick(joystickID)
                    println("Connect Joystick")
                }

                GLFW.GLFW_DISCONNECTED -> {
                    joysticks.remove(joystickID)
                    println("Remove Joystick")
                }
            }
        }
    }

    companion object {
        const val MAX_JOYSTICKS = GLFW.GLFW_JOYSTICK_LAST + 1
        private var instance: JoystickManager? = null
        fun init() {
            if (instance == null) instance = JoystickManager()
        }

        fun getJoysticks(): Collection<Joystick> {
            return instance!!.joysticks.values
        }

        fun getMonitor(id: Int): Joystick? {
            return instance!!.joysticks[id]
        }

        fun isPresent(id: Int): Boolean {
            return GLFW.glfwJoystickPresent(id)
        }
    }
}
