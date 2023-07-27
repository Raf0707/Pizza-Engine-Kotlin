package pize.tests.voxelgame.client.entity.model

import pize.graphics.gl.Type
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr

class BoxBuilder(
    private val x1: Float,
    private val y1: Float,
    private val z1: Float,
    private val x2: Float,
    private val y2: Float,
    private val z2: Float
) {
    private val mesh: Mesh
    private val vertices: FloatArray
    private var vertexPointer = 0

    init {
        mesh = Mesh(VertexAttr(3, Type.FLOAT), VertexAttr(4, Type.FLOAT), VertexAttr(2, Type.FLOAT))
        vertices = FloatArray(4 * 6 * mesh.vBO.vertexSize)
    }

    fun nx(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x1, y2, z2, r, g, b, a, u1, v1)
        addVertex(x1, y1, z2, r, g, b, a, u1, v2)
        addVertex(x1, y1, z1, r, g, b, a, u2, v2)
        addVertex(x1, y2, z1, r, g, b, a, u2, v1)
        return this
    }

    fun px(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x2, y2, z1, r, g, b, a, u1, v1)
        addVertex(x2, y1, z1, r, g, b, a, u1, v2)
        addVertex(x2, y1, z2, r, g, b, a, u2, v2)
        addVertex(x2, y2, z2, r, g, b, a, u2, v1)
        return this
    }

    fun ny(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x1, y1, z2, r, g, b, a, u1, v1)
        addVertex(x2, y1, z2, r, g, b, a, u1, v2)
        addVertex(x2, y1, z1, r, g, b, a, u2, v2)
        addVertex(x1, y1, z1, r, g, b, a, u2, v1)
        return this
    }

    fun py(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x1, y2, z1, r, g, b, a, u1, v1)
        addVertex(x2, y2, z1, r, g, b, a, u1, v2)
        addVertex(x2, y2, z2, r, g, b, a, u2, v2)
        addVertex(x1, y2, z2, r, g, b, a, u2, v1)
        return this
    }

    fun nz(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x1, y2, z1, r, g, b, a, u1, v1)
        addVertex(x1, y1, z1, r, g, b, a, u1, v2)
        addVertex(x2, y1, z1, r, g, b, a, u2, v2)
        addVertex(x2, y2, z1, r, g, b, a, u2, v1)
        return this
    }

    fun pz(r: Float, g: Float, b: Float, a: Float, u1: Float, v1: Float, u2: Float, v2: Float): BoxBuilder {
        addVertex(x2, y2, z2, r, g, b, a, u1, v1)
        addVertex(x2, y1, z2, r, g, b, a, u1, v2)
        addVertex(x1, y1, z2, r, g, b, a, u2, v2)
        addVertex(x1, y2, z2, r, g, b, a, u2, v1)
        return this
    }

    private fun addVertex(x: Float, y: Float, z: Float, r: Float, g: Float, b: Float, a: Float, u: Float, v: Float) {
        /*
        float[] cube = new float[]{
            x1, y2, z2,  x1, y1, z2,  x1, y1, z1,  repeat3  x1, y2, z1,  repeat0,  // -x
            x2, y2, z1,  x2, y1, z1,  x2, y1, z2,  repeat3  x2, y2, z2,  repeat0,  // +x
            x1, y1, z2,  x2, y1, z2,  x2, y1, z1,  repeat3  x1, y1, z1,  repeat0,  // -y
            x1, y2, z1,  x2, y2, z1,  x2, y2, z2,  repeat3  x1, y2, z2,  repeat0,  // +y
            x1, y2, z1,  x1, y1, z1,  x2, y1, z1,  repeat3, x2, y2, z1,  repeat0,  // -z
            x2, y2, z2,  x2, y1, z2,  x1, y1, z2,  repeat3, x1, y2, z2,  repeat0,  // +z
        };
        */
        val p = vertexPointer * mesh.vBO.vertexSize
        vertices[p] = x
        vertices[p + 1] = y
        vertices[p + 2] = z
        vertices[p + 3] = r
        vertices[p + 4] = g
        vertices[p + 5] = b
        vertices[p + 6] = a
        vertices[p + 7] = u
        vertices[p + 8] = v
        vertexPointer++
    }

    fun end(): Mesh {
        mesh.setVertices(vertices)
        mesh.setIndices(
            intArrayOf(
                0, 1, 2, 2, 3, 0,  // -x
                4, 5, 6, 6, 7, 4,  // +x
                8, 9, 10, 10, 11, 8,  // -y
                12, 13, 14, 14, 15, 12,  // +y
                16, 17, 18, 18, 19, 16,  // -z
                20, 21, 22, 22, 23, 20
            )
        )
        return mesh
    }
}
