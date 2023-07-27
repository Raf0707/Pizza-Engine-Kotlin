package pize.graphics.gl

import org.lwjgl.opengl.GL11

enum class Mode(val GL: Int) {
    FASTEST(GL11.GL_FASTEST),
    NICEST(GL11.GL_NICEST),
    DONT_CARE(GL11.GL_DONT_CARE)
}
