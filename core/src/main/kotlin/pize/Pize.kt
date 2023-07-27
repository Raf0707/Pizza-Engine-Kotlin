package pize

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import pize.app.*
import pize.audio.Audio
import pize.graphics.gl.BlendFactor
import pize.graphics.gl.Gl
import pize.graphics.gl.Target
import pize.io.joystick.JoystickManager
import pize.io.keyboard.Keyboard
import pize.io.monitor.Monitor
import pize.io.monitor.MonitorManager
import pize.io.mouse.Mouse
import pize.io.window.Window
import pize.math.vecmath.vector.Vec2f

object Pize {
    private var context: Context? = null
    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        GLFW.glfwInit()
        MonitorManager.Companion.init()
        JoystickManager.Companion.init()
    }

    @JvmOverloads
    fun create(
        title: String?,
        width: Int,
        height: Int,
        resizable: Boolean = true,
        vsync: Boolean = true,
        samples: Int = 4
    ) {
        init()
        val window = Window(title, width, height, resizable, vsync, samples)
        context = Context(window, Keyboard(window), Mouse(window))
        Gl.enable(Target.BLEND, Target.CULL_FACE, Target.MULTISAMPLE)
        Gl.blendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)
    }

    @JvmStatic
    fun run(listener: AppAdapter) {
        context!!.begin(listener)
    }

    fun setScreen(screen: Screen?) {
        context!!.setScreen(screen)
    }

    fun context(): Context? {
        return context
    }

    @JvmStatic
    fun window(): Window? {
        return context.getWindow()
    }

    @JvmStatic
    fun keyboard(): Keyboard? {
        return context.getKeyboard()
    }

    @JvmStatic
    fun mouse(): Mouse? {
        return context.getMouse()
    }

    @JvmStatic
    fun monitor(): Monitor? {
        return MonitorManager.Companion.getPrimary()
    }

    @JvmStatic
    fun audio(): Audio? {
        return context.getAudio()
    }

    val isTouchDown: Boolean
        get() = mouse()!!.isLeftDown || mouse()!!.isRightDown
    @JvmStatic
    val isTouched: Boolean
        get() = mouse()!!.isLeftPressed || mouse()!!.isMiddlePressed || mouse()!!.isRightPressed
    @JvmStatic
    val isTouchReleased: Boolean
        get() = mouse()!!.isLeftReleased || mouse()!!.isRightReleased
    @JvmStatic
    val width: Int
        get() = window().getWidth()
    @JvmStatic
    val height: Int
        get() = window().getHeight()
    val aspect: Float
        get() = window()!!.aspect()
    @JvmStatic
    val x: Int
        get() = mouse().getX()
    @JvmStatic
    val y: Int
        get() = window().getHeight() - mouse().getY()
    val invY: Int
        get() = mouse().getY()
    @JvmStatic
    val cursorPos: Vec2f
        get() = Vec2f(x.toFloat(), y.toFloat())
    @JvmStatic
    val fPS: Int
        get() = context.getFps()
    @JvmStatic
    val dt: Float
        get() = context!!.renderDeltaTime
    @JvmStatic
    val updateDt: Float
        get() = context!!.fixedUpdateDeltaTime

    @JvmStatic
    fun setFixedUpdateTPS(updateTPS: Float) {
        context!!.setFixedUpdateTPS(updateTPS)
    }

    @JvmStatic
    fun execSync(runnable: Runnable) {
        context!!.execSync(runnable)
    }

    @JvmStatic
    var clipboardString: String?
        get() = window().getClipboardString()
        set(charSequence) {
            window()!!.setClipboardString(charSequence)
        }

    @JvmStatic
    fun exit() {
        context!!.exit()
    }
}
