package pize.io.keyboard

import pize.io.glfw.KeyAction

fun interface KeyCallback {
    operator fun invoke(keyCode: Int, action: KeyAction)
}
