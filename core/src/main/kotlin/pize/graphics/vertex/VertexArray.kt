package pize.graphics.vertex

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import pize.graphics.gl.GlObject
import pize.graphics.gl.Primitive
import pize.graphics.gl.Type

class VertexArray : GlObject(GL30.glGenVertexArrays()) {
    init {
        bind()
    }

    @JvmOverloads
    fun drawArrays(verticesNum: Int, mode: Primitive = Primitive.TRIANGLES) {
        bind()
        GL11.glDrawArrays(mode.GL, 0, verticesNum)
    }

    @JvmOverloads
    fun drawElements(indicesNum: Int, mode: Primitive = Primitive.TRIANGLES, indicesType: Type = Type.UNSIGNED_INT) {
        bind()
        GL11.glDrawElements(mode.GL, indicesNum, indicesType.GL, 0)
    }

    fun bind() {
        GL30.glBindVertexArray(ID)
    }

    override fun dispose() {
        GL30.glDeleteVertexArrays(ID)
    }

    companion object {
        fun unbind() {
            GL30.glBindVertexArray(0)
        }
    }
}