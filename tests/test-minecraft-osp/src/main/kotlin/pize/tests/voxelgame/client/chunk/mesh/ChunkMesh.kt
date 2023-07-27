package pize.tests.voxelgame.client.chunk.mesh

import pize.Pize.execSync
import pize.app.Disposable
import pize.graphics.vertex.VertexArray
import pize.graphics.vertex.VertexAttr
import pize.graphics.vertex.VertexBuffer

abstract class ChunkMesh(vararg attributes: VertexAttr?) : Disposable {
    private var vao: VertexArray? = null
    protected var vbo: VertexBuffer? = null

    init {
        execSync {
            vao = VertexArray()
            vbo = VertexBuffer()
            vbo!!.enableAttributes(*attributes)
        }
    }

    fun render() {
        if (vbo == null) return
        vao!!.drawArrays(vbo!!.verticesNum)
    }

    override fun dispose() {
        if (vbo != null) vbo!!.dispose()
        if (vao != null) vao.dispose()
    }

    abstract fun updateVertices(): Int
    abstract val type: ChunkMeshType?
}
