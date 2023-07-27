package pize.graphics.util

import pize.graphics.gl.Type
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr

class ScreenQuad private constructor() {
    private val mesh: Mesh

    init {
        mesh = Mesh(VertexAttr(2, Type.FLOAT), VertexAttr(2, Type.FLOAT)) // pos, uv
        mesh.setVertices(
            floatArrayOf(
                -1f, +1f, 0f, 1f,  // 0
                -1f, -1f, 0f, 0f,  // 1
                +1f, -1f, 1f, 0f,  // 2
                +1f, +1f, 1f, 1f
            )
        )
        mesh.setIndices(
            intArrayOf(
                0, 1, 2,
                2, 3, 0
            )
        )
    }

    companion object {
        private var instance: ScreenQuad? = null
        @JvmStatic
        fun render() {
            if (instance == null) instance = ScreenQuad()
            instance!!.mesh.render()
        }

        private fun dispose() { // Invoked from Context
            if (instance != null) instance!!.mesh.dispose()
        }
    }
}
