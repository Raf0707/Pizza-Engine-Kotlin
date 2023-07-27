package pize.graphics.gl

import org.lwjgl.opengl.GL11

enum class PolygonMode(val GL: Int) {
    POINT(GL11.GL_POINT),
    LINE(GL11.GL_LINE),
    FILL(GL11.GL_FILL)
}
