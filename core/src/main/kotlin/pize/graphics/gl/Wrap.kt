package pize.graphics.gl

import org.lwjgl.opengl.*

enum class Wrap(val GL: Int) {
    REPEAT(GL11.GL_REPEAT),
    MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER),

    // ARB
    MIRROR_CLAMP_TO_EDGE(ARBTextureMirrorClampToEdge.GL_MIRROR_CLAMP_TO_EDGE)
}
