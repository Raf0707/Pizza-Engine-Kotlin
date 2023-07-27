package pize.io.mouse

import org.lwjgl.glfw.GLFW
import pize.Pize
import pize.io.glfw.Key
import pize.io.keyboard.Keyboard
import pize.io.window.Window
import java.util.*

class Mouse(window: Window) {
    val windowId: Long
    var isInWindow = false
    var isVisible = true
        private set
    var scroll = 0
        private set
    var touchDownX = 0
    var touchDownY = 0
    private val down: BooleanArray
    private val pressed: BooleanArray
    private val released: BooleanArray

    init {
        windowId = window.ID
        down = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST + 1)
        pressed = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST + 1)
        released = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST + 1)
        GLFW.glfwSetCursorEnterCallback(windowId) { windowHandle: Long, entered: Boolean -> isInWindow = entered }
        GLFW.glfwSetScrollCallback(windowId) { windowHandle: Long, x: Double, y: Double ->
            scroll = (scroll + y).toInt()
        }
        GLFW.glfwSetMouseButtonCallback(windowId) { windowHandle: Long, button: Int, action: Int, mode: Int ->
            if (action == GLFW.GLFW_PRESS) {
                down[button] = true
                pressed[button] = true
                touchDownX = x
                touchDownY = y
            } else if (action == GLFW.GLFW_RELEASE) {
                released[button] = true
                pressed[button] = false
            }
        }
    }

    fun reset() {
        scroll = 0
        Arrays.fill(released, false)
        Arrays.fill(down, false)
    }

    fun show(show: Boolean) {
        if (show == isVisible) return
        GLFW.glfwSetInputMode(
            windowId,
            GLFW.GLFW_CURSOR,
            if (show) GLFW.GLFW_CURSOR_NORMAL else GLFW.GLFW_CURSOR_HIDDEN
        )
        isVisible = show
    }

    fun setPos(x: Int, y: Int) {
        GLFW.glfwSetCursorPos(windowId, x.toDouble(), y.toDouble())
    }

    fun setPosCenter(window: Window) {
        GLFW.glfwSetCursorPos(windowId, window.width * 0.5, window.height * 0.5)
    }

    val pos: IntArray
        get() {
            val x = DoubleArray(1)
            val y = DoubleArray(1)
            GLFW.glfwGetCursorPos(windowId, x, y)
            return intArrayOf(x[0].toInt(), y[0].toInt())
        }
    val x: Int
        get() {
            val x = DoubleArray(1)
            GLFW.glfwGetCursorPos(windowId, x, null)
            return x[0].toInt()
        }
    val y: Int
        get() {
            val y = DoubleArray(1)
            GLFW.glfwGetCursorPos(windowId, null, y)
            return y[0].toInt()
        }

    fun isInBounds(x: Double, y: Double, width: Double, height: Double): Boolean {
        val cursorX = this.x
        val cursorY = Pize.y
        return !(cursorX < x || cursorY < y || cursorX >= x + width || cursorY >= y + height)
    }

    val isLeftDown: Boolean
        get() = down[Key.MOUSE_LEFT.GLFW]
    val isLeftPressed: Boolean
        get() = pressed[Key.MOUSE_LEFT.GLFW]
    val isLeftReleased: Boolean
        get() = released[Key.MOUSE_LEFT.GLFW]
    val isMiddleDown: Boolean
        get() = down[Key.MOUSE_MIDDLE.GLFW]
    val isMiddlePressed: Boolean
        get() = pressed[Key.MOUSE_MIDDLE.GLFW]
    val isMiddleReleased: Boolean
        get() = released[Key.MOUSE_MIDDLE.GLFW]
    val isRightDown: Boolean
        get() = down[Key.MOUSE_RIGHT.GLFW]
    val isRightPressed: Boolean
        get() = pressed[Key.MOUSE_RIGHT.GLFW]
    val isRightReleased: Boolean
        get() = released[Key.MOUSE_RIGHT.GLFW]

    fun isButtonDown(button: Key): Boolean {
        return down[button.GLFW]
    }

    fun isButtonPressed(button: Key): Boolean {
        return pressed[button.GLFW]
    }

    fun isButtonReleased(button: Key): Boolean {
        return released[button.GLFW]
    }

    fun getScrollX(keyboard: Keyboard): Int {
        return if (keyboard.isPressed(Key.LEFT_SHIFT, Key.RIGHT_SHIFT)) scroll else 0
    }

    fun getScrollY(keyboard: Keyboard): Int {
        return if (keyboard.isPressed(Key.LEFT_SHIFT, Key.RIGHT_SHIFT)) 0 else scroll
    }
}
