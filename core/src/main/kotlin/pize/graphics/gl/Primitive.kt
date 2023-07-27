package pize.graphics.gl

import org.lwjgl.opengl.GL11

enum class Primitive(val GL: Int) {
    POINTS(GL11.GL_POINTS),
    LINES(GL11.GL_LINES),
    LINE_STRIP(GL11.GL_LINE_STRIP),
    LINE_LOOP(GL11.GL_LINE_LOOP),
    POLYGON(GL11.GL_POLYGON),
    QUADS(GL11.GL_QUADS),
    QUAD_STRIP(GL11.GL_QUAD_STRIP),
    TRIANGLES(GL11.GL_TRIANGLES),
    TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN)
}
