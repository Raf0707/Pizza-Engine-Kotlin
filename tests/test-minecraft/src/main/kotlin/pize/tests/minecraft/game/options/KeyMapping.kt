package pize.tests.minecraft.game.options

import pize.io.glfw.Key

enum class KeyMapping private constructor(val defaultKey: Key) {
    FORWARD(Key.W),
    LEFT(Key.A),
    BACK(Key.S),
    RIGHT(Key.F),
    JUMP(Key.SPACE),
    SNEAK(Key.LEFT_SHIFT),
    SPRINT(Key.LEFT_CONTROL),
    CHAT(Key.T),
    ZOOM(Key.C),
    FULLSCREEN(Key.F11),
    SCREENSHOT(Key.F2)

}
