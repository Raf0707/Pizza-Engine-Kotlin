package pize.graphics.vertex

import org.lwjgl.opengl.GL15
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.GlObject

class ElementBuffer : GlObject(GL15.glGenBuffers()) {
    private var dataSize: Long = 0

    init {
        bind()
    }

    val indicesNum: Int
        get() = dataSize.toInt()

    fun setData(size: Long, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, size, usage.GL)
        dataSize = size
    }

    fun setData(data: IntArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: ShortArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: LongArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setSubData(offset: Long, data: IntArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offset, data)
        dataSize = data.size.toLong()
    }

    fun setSubData(offset: Long, data: ShortArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offset, data)
        dataSize = data.size.toLong()
    }

    fun setSubData(offset: Long, data: LongArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, offset, data)
        dataSize = data.size.toLong()
    }

    fun bind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ID)
    }

    override fun dispose() {
        GL15.glDeleteBuffers(ID)
    }

    companion object {
        fun unbind() {
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
        }
    }
}