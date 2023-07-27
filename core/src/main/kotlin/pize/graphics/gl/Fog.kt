package pize.graphics.gl

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15

enum class Fog(val GL: Int) {
    MODE(GL11.GL_FOG_MODE),
    DENSITY(GL11.GL_FOG_DENSITY),
    START(GL11.GL_FOG_START),
    END(GL11.GL_FOG_END),
    INDEX(GL11.GL_FOG_INDEX),
    COORD_SRC(GL15.GL_FOG_COORD_SRC),
    COLOR(GL11.GL_FOG_COLOR)
}
