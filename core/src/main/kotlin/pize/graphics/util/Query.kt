package pize.graphics.util

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import pize.app.Disposable
import pize.graphics.gl.GlObject
import pize.graphics.gl.QueryTarget

class Query(var type: QueryTarget) : GlObject(GL15.glGenQueries()), Disposable {

    fun begin() {
        GL15.glBeginQuery(type.GL, ID)
    }

    fun end() {
        GL15.glEndQuery(type.GL)
    }

    val isResultAvailable: Boolean
        get() = GL15.glGetQueryObjecti(ID, GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE
    val result: Int
        get() = GL15.glGetQueryObjecti(ID, GL15.GL_QUERY_RESULT)

    fun waitForResult(): Int {
        while (!isResultAvailable);
        return result
    }

    override fun dispose() {
        GL15.glDeleteQueries(ID)
    }
}
