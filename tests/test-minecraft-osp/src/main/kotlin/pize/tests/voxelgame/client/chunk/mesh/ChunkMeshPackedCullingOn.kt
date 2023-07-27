package pize.tests.voxelgame.client.chunk.mesh

import pize.Pize.execSync
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.Type
import pize.graphics.vertex.VertexAttr

open class ChunkMeshPackedCullingOn : ChunkMesh(
    VertexAttr(
        1,
        Type.FLOAT
    ),  // packed1 (int) - (Type.FLOAT потому что похоже баг какой-то хз шиза тут должен быть Type.INT)
    VertexAttr(1, Type.FLOAT) // packed2 (int)
) {
    val verticesList: MutableList<Int>

    init {
        verticesList = ArrayList()
    }

    fun vertex(x: Int, y: Int, z: Int, u: Float, v: Float, light: Float, r: Float, g: Float, b: Float, a: Float) {
        val atlasTilesX = 32
        val atlasTilesY = 32

        // Packed position
        val positionPacked = x or  // 5 bit
                (y shl 5) or  // 9 bit
                (z shl 14) or  // 5 bit
                ((u * atlasTilesX).toInt() shl 19) or  // 4 bit
                ((v * atlasTilesY).toInt() shl 23) // 4 bit
        // 4 bit remaining
        put(positionPacked) // x, y, z, u, v

        // Packed color
        val colorPacked = (r * light * 255).toInt() or  // 8 bit
                ((g * light * 255).toInt() shl 8) or  // 8 bit
                ((b * light * 255).toInt() shl 16) or  // 8 bit
                ((a * 255).toInt() shl 24) // 8 bit
        put(colorPacked) // r, g, b, a
    }

    fun vertex(packed1: Int, packed2: Int) {
        put(packed1)
        put(packed2)
    }

    fun put(value: Int) {
        verticesList.add(value)
    }

    override fun updateVertices(): Int {
        if (verticesList.size == 0) return 0
        val verticesArray = IntArray(verticesList.size)
        for (i in verticesList.indices) verticesArray[i] = verticesList[i]
        execSync { vbo!!.setData(verticesArray, BufferUsage.STATIC_DRAW) }
        verticesList.clear()
        return verticesArray.size
    }

    override val type: ChunkMeshType?
        get() = ChunkMeshType.SOLID
}
