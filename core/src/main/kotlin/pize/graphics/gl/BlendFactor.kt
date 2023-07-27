package pize.graphics.gl

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL33

enum class BlendFactor(val GL: Int) {
    ZERO(GL11.GL_ZERO),
    ONE(GL11.GL_ONE),
    SRC_COLOR(GL11.GL_SRC_COLOR),
    ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
    DST_COLOR(GL11.GL_DST_COLOR),
    ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR),
    SRC_ALPHA(GL11.GL_SRC_ALPHA),
    ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
    DST_ALPHA(GL11.GL_DST_ALPHA),
    ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA),
    CONSTANT_COLOR(GL14.GL_CONSTANT_COLOR),
    ONE_MINUS_CONSTANT_COLOR(GL14.GL_ONE_MINUS_CONSTANT_COLOR),
    CONSTANT_ALPHA(GL14.GL_CONSTANT_ALPHA),
    ONE_MINUS_CONSTANT_ALPHA(GL14.GL_ONE_MINUS_CONSTANT_ALPHA),
    SRC1_COLOR(GL33.GL_SRC1_COLOR),
    ONE_MINUS_SRC1_COLOR(GL33.GL_ONE_MINUS_SRC1_COLOR),
    SRC1_ALPHA(GL15.GL_SRC1_ALPHA),
    ONE_MINUS_SRC1_ALPHA(GL33.GL_ONE_MINUS_SRC1_ALPHA),
    SRC_ALPHA_SATURATE(GL11.GL_SRC_ALPHA_SATURATE)
}
