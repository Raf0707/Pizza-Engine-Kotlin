package pize.graphics.gl

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30

enum class Format(val GL: Int, val channels: Int) {
    RED(GL11.GL_RED, 1),
    RED_INTEGER(GL30.GL_RED_INTEGER, 1),
    RG(GL30.GL_RG, 2),
    RG_INTEGER(GL30.GL_RG_INTEGER, 2),
    RGB(GL11.GL_RGB, 3),
    RGB_INTEGER(GL30.GL_RGB_INTEGER, 3),
    BGR(GL12.GL_BGR, 3),
    BGR_INTEGER(GL30.GL_BGR_INTEGER, 3),
    RGBA(GL11.GL_RGBA, 4),
    RGBA_INTEGER(GL30.GL_RGBA_INTEGER, 4),
    BGRA(GL12.GL_BGRA, 4),
    BGRA_INTEGER(GL30.GL_BGRA_INTEGER, 4),
    STENCIL_INDEX(GL11.GL_STENCIL_INDEX, 1),
    DEPTH_COMPONENT(GL11.GL_DEPTH_COMPONENT, 1),
    DEPTH_STENCIL(GL30.GL_DEPTH_STENCIL, 1)

}