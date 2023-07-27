package pize.tests.voxelgame.client.block.shape

import pize.app.Disposable
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.Primitive
import pize.graphics.gl.Type
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr

open class BlockCursor(vertices: FloatArray?, indices: IntArray?) : Disposable {
    val mesh: Mesh

    init {
        mesh = Mesh(VertexAttr(3, Type.FLOAT))
        mesh.setRenderMode(Primitive.LINES)
        mesh.setVertices(vertices!!, BufferUsage.STATIC_DRAW)
        mesh.setIndices(indices!!, BufferUsage.STATIC_DRAW)
    }

    fun render() {
        mesh.render()
    }

    override fun dispose() {
        mesh.dispose()
    }

    companion object {
        val SOLID: BlockCursor = SolidBlockCursor()
        val GRASS: BlockCursor = GrassBlockCursor()
    }
}
