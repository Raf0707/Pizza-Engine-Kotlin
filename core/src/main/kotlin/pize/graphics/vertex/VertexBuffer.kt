package pize.graphics.vertex

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.GlObject
import java.nio.*
import kotlin.math.max

class VertexBuffer : GlObject(GL15.glGenBuffers()) {
    var vertexSize = 0
        private set
    var vertexBytes = 0
        private set
    var dataSize: Long = 0
        private set

    init {
        bind()
    }

    fun enableAttributes(vararg attributes: VertexAttr) {
        for (attribute in attributes) {
            vertexSize += attribute.count
            vertexBytes += attribute.count * attribute.type.size
        }
        var pointer = 0
        for (i in attributes.indices) {
            val attribute = attributes[i.toInt()]
            val count = attribute.count
            val type = attribute.type
            GL20.glVertexAttribPointer(
                i.toInt(),
                count,
                type!!.GL,
                attribute.isNormalize,
                vertexSize * type.size,
                pointer.toLong()
            )
            GL20.glEnableVertexAttribArray(i.toInt())
            pointer += count * type.size
        }
    }

    val verticesNum: Int
        get() = (dataSize / vertexSize).toInt()

    fun setData(size: Long, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, usage.GL)
        dataSize = size
    }

    fun setData(data: FloatArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: DoubleArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: IntArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: ShortArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(data: LongArray, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage.GL)
        dataSize = data.size.toLong()
    }

    fun setData(buffer: IntBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setData(buffer: ByteBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setData(buffer: FloatBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setData(buffer: LongBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setData(buffer: ShortBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setData(buffer: DoubleBuffer, usage: BufferUsage) {
        bind()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage.GL)
        dataSize = buffer.limit().toLong()
    }

    fun setSubData(offset: Long, data: FloatArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data)
        dataSize = max(dataSize.toDouble(), (offset + data.size).toDouble()).toLong()
    }

    fun setSubData(offset: Long, data: DoubleArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data)
        dataSize = max(dataSize.toDouble(), (offset + data.size).toDouble()).toLong()
    }

    fun setSubData(offset: Long, data: IntArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data)
        dataSize = max(dataSize.toDouble(), (offset + data.size).toDouble()).toLong()
    }

    fun setSubData(offset: Long, data: ShortArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data)
        dataSize = max(dataSize.toDouble(), (offset + data.size).toDouble()).toLong()
    }

    fun setSubData(offset: Long, data: LongArray) {
        bind()
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, data)
        dataSize = max(dataSize.toDouble(), (offset + data.size).toDouble()).toLong()
    }

    fun bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID)
    }

    override fun dispose() {
        GL15.glDeleteBuffers(ID)
    }

    companion object {
        fun unbind() {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        }
    }
}