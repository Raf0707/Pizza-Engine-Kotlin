package pize.tests.voxelgame.client.options

import pize.io.glfw.Key

enum class KeyMapping(override val default: Key) : Option<Key> {
    FORWARD(Key.W),
    LEFT(Key.A),
    BACK(Key.S),
    RIGHT(Key.D),
    JUMP(Key.SPACE),
    SNEAK(Key.LEFT_SHIFT),
    SPRINT(Key.LEFT_CONTROL),
    CHAT(Key.T),
    COMMAND(Key.SLASH),
    ZOOM(Key.C),
    FULLSCREEN(Key.F11),
    TOGGLE_PERSPECTIVE(Key.F5),
    SCREENSHOT(Key.F2)

}
