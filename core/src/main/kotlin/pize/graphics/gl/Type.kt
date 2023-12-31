package pize.graphics.gl

import org.lwjgl.opengl.*

enum class Type(val GL: Int, val size: Int) {
    FLOAT(GL11.GL_FLOAT, 4),
    INT(GL11.GL_INT, 4),
    UNSIGNED_INT(GL11.GL_UNSIGNED_INT, 4),
    HALF_FLOAT(GL30.GL_HALF_FLOAT, 2),
    SHORT(GL11.GL_SHORT, 2),
    UNSIGNED_SHORT(GL11.GL_UNSIGNED_SHORT, 2),
    BOOL(GL20.GL_BOOL, 1),
    BYTE(GL11.GL_BYTE, 1),
    UNSIGNED_BYTE(GL11.GL_UNSIGNED_BYTE, 1),
    DOUBLE(GL11.GL_DOUBLE, 8),
    FIXED(GL41.GL_FIXED, 4),
    INT_2_10_10_10_REV(GL33.GL_INT_2_10_10_10_REV, 4),
    UNSIGNED_INT_2_10_10_10_REV(GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4),
    UNSIGNED_INT_10F_11F_11F_REV(GL30.GL_UNSIGNED_INT_10F_11F_11F_REV, 4)

}
