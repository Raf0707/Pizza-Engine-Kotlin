package pize.io.monitor

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWVidMode

class Monitor(val ID: Long) {
    private val videoMode: GLFWVidMode?

    init {
        videoMode = GLFW.glfwGetVideoMode(ID)
    }

    val refreshRate: Int
        get() = videoMode!!.refreshRate()
    val width: Int
        get() = videoMode!!.width()
    val height: Int
        get() = videoMode!!.height()
    val aspect: Float
        get() = width.toFloat() / height
    val redBits: Int
        get() = videoMode!!.redBits()
    val greenBits: Int
        get() = videoMode!!.greenBits()
    val blueBits: Int
        get() = videoMode!!.blueBits()

    fun bitsPerPixel(): Int {
        return videoMode!!.sizeof()
    }

    val name: String?
        get() = GLFW.glfwGetMonitorName(ID)
}
