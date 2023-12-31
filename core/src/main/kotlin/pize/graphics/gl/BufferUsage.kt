package pize.graphics.gl

import org.lwjgl.opengl.GL15

enum class BufferUsage(val GL: Int) {
    STATIC_DRAW(GL15.GL_STATIC_DRAW),
    DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
    STREAM_DRAW(GL15.GL_STREAM_DRAW),
    STATIC_READ(GL15.GL_STATIC_READ),
    DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
    STREAM_READ(GL15.GL_STREAM_READ),
    STATIC_COPY(GL15.GL_STATIC_COPY),
    DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY),
    STREAM_COPY(GL15.GL_STREAM_COPY)
}
