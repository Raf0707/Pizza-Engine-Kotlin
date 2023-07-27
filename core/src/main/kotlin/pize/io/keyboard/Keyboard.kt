package pize.io.keyboard

import org.lwjgl.glfw.GLFW
import pize.io.glfw.Key
import pize.io.glfw.KeyAction
import pize.io.window.Window
import java.util.*

class Keyboard(window: Window) {
    private val pressed: BooleanArray
    private val down: BooleanArray
    private val released: BooleanArray
    private val charCallbackList: MutableList<CharCallback>
    private val keyCallbackList: MutableList<KeyCallback>

    init {
        pressed = BooleanArray(GLFW.GLFW_KEY_LAST + 1)
        down = BooleanArray(GLFW.GLFW_KEY_LAST + 1)
        released = BooleanArray(GLFW.GLFW_KEY_LAST + 1)
        keyCallbackList = ArrayList()
        GLFW.glfwSetKeyCallback(window.ID) { windowID: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == -1) return@glfwSetKeyCallback
            for (keyCallback in keyCallbackList) keyCallback.invoke(key, KeyAction.entries[action])
            when (action) {
                GLFW.GLFW_PRESS -> {
                    down[key] = true
                    pressed[key] = true
                }

                GLFW.GLFW_RELEASE -> {
                    released[key] = true
                    pressed[key] = false
                }
            }
        }
        charCallbackList = ArrayList()
        GLFW.glfwSetCharCallback(window.ID) { windowID: Long, character: Int ->
            for (charCallback in charCallbackList) charCallback.invoke(
                character.toChar()
            )
        }
    }

    fun reset() {
        Arrays.fill(released, false)
        Arrays.fill(down, false)
    }

    fun isDown(key: Key): Boolean {
        return down[key.GLFW]
    }

    fun isPressed(key: Key): Boolean {
        return pressed[key.GLFW]
    }

    fun isReleased(key: Key): Boolean {
        return released[key.GLFW]
    }

    fun isDown(vararg keys: Key): Boolean {
        for (key in keys) if (down[key.GLFW]) return true
        return false
    }

    fun isPressed(vararg keys: Key): Boolean {
        for (key in keys) if (pressed[key.GLFW]) return true
        return false
    }

    fun isReleased(vararg keys: Key): Boolean {
        for (key in keys) if (released[key.GLFW]) return true
        return false
    }

    fun isDownAll(vararg keys: Key): Boolean {
        for (key in keys) if (!down[key.GLFW]) return false
        return true
    }

    fun isPressedAll(vararg keys: Key): Boolean {
        for (key in keys) if (!pressed[key.GLFW]) return false
        return true
    }

    fun isReleasedAll(vararg keys: Key): Boolean {
        for (key in keys) if (!released[key.GLFW]) return false
        return true
    }

    fun addKeyCallback(keyCallback: KeyCallback) {
        keyCallbackList.add(keyCallback)
    }

    fun removeKeyCallback(keyCallback: KeyCallback) {
        keyCallbackList.remove(keyCallback)
    }

    fun addCharCallback(charCallback: CharCallback) {
        charCallbackList.add(charCallback)
    }

    fun removeCharCallback(charCallback: CharCallback) {
        charCallbackList.remove(charCallback)
    }
}
