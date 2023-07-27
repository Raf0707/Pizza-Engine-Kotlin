package pize.tests.voxelgame.client.chunk.mesh

import pize.Pize.execSync
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.Type
import pize.graphics.vertex.VertexAttr

class ChunkMeshCullingOff : ChunkMesh(
    VertexAttr(3, Type.FLOAT),  // pos3
    VertexAttr(4, Type.FLOAT),  // col4
    VertexAttr(2, Type.FLOAT) // tex2
) {
    val verticesList: MutableList<Float>

    init {
        verticesList = ArrayList()
    }

    override fun updateVertices(): Int {
        if (verticesList.size == 0) return 0
        val verticesArray = FloatArray(verticesList.size)
        for (i in verticesList.indices) verticesArray[i] = verticesList[i]
        execSync { vbo!!.setData(verticesArray, BufferUsage.STATIC_DRAW) }
        verticesList.clear()
        return verticesArray.size
    }

    fun vertex(x: Float, y: Float, z: Float, r: Float, g: Float, b: Float, a: Float, u: Float, v: Float) {
        put(x)
        put(y)
        put(z)
        put(r)
        put(g)
        put(b)
        put(a)
        put(u)
        put(v)
    }

    fun put(v: Float) {
        verticesList.add(v)
    }

    override val type: ChunkMeshType?
        get() = ChunkMeshType.CUSTOM
}