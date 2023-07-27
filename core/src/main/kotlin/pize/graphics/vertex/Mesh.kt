package pize.graphics.vertex

import pize.app.Disposable
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.Primitive

class Mesh : Disposable {
    private var mode: Primitive
    private var vertices: FloatArray
    private var indices: IntArray
    private val attributes: Array<VertexAttr>
    val vAO: VertexArray
    val vBO: VertexBuffer
    val eBO: ElementBuffer

    constructor(vararg attributes: VertexAttr) {
        mode = Primitive.TRIANGLES
        this.attributes = attributes as Array<VertexAttr>
        vertices = FloatArray(0)
        indices = IntArray(0)
        vAO = VertexArray()
        vBO = VertexBuffer()
        vBO.enableAttributes(*attributes)
        eBO = ElementBuffer()
    }

    constructor(mesh: Mesh) {
        attributes = mesh.attributes
        vertices = mesh.vertices.clone()
        indices = mesh.indices.clone()
        mode = mesh.mode
        vAO = VertexArray()
        vBO = VertexBuffer()
        vBO.enableAttributes(*attributes)
        vBO.setData(vertices, BufferUsage.DYNAMIC_DRAW)
        eBO = ElementBuffer()
        eBO.setData(indices, BufferUsage.DYNAMIC_DRAW)
    }

    fun render() {
        // vao.drawArrays(vbo.getVerticesNum(), mode);
        vAO.drawElements(eBO.indicesNum, mode)
    }

    fun setRenderMode(mode: Primitive) {
        this.mode = mode
    }

    fun setVertices(verticesList: List<Float>): Boolean {
        vertices = FloatArray(verticesList.size)
        for (i in vertices.indices) vertices[i] = verticesList[i]
        vBO.setData(vertices, BufferUsage.DYNAMIC_DRAW)
        return true
    }

    fun setVertices(vertices: FloatArray, usage: BufferUsage) {
        this.vertices = vertices
        vBO.setData(vertices, usage)
    }

    fun setVertices(vertices: FloatArray) {
        this.vertices = vertices
        vBO.setData(vertices, BufferUsage.STATIC_DRAW)
    }

    fun setIndices(indices: IntArray, usage: BufferUsage) {
        this.indices = indices
        eBO.setData(indices, usage)
    }

    fun setIndices(indices: IntArray) {
        this.indices = indices
        eBO.setData(indices, BufferUsage.STATIC_DRAW)
    }

    val indexedVertices: FloatArray
        get() {
            val indexedVertices = FloatArray(indices.size * 3)
            for (i in indices.indices) {
                val index = indices[i]
                indexedVertices[i * 3] = vertices[index * 3]
                indexedVertices[i * 3 + 1] = vertices[index * 3 + 1]
                indexedVertices[i * 3 + 2] = vertices[index * 3 + 2]
            }
            return indexedVertices
        }

    fun getVertices(): FloatArray {
        return vertices
    }

    fun getIndices(): IntArray {
        return indices
    }

    override fun dispose() {
        vBO.dispose()
        vAO.dispose()
        eBO.dispose()
    }

    fun copy(): Mesh {
        return Mesh(this)
    }
}
