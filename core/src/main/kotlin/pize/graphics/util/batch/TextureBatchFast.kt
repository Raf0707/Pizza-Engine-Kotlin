package pize.graphics.util.batch

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import pize.Pize
import pize.files.Resource
import pize.graphics.camera.Camera
import pize.graphics.gl.*
import pize.graphics.texture.*
import pize.graphics.util.Shader
import pize.graphics.vertex.*
import pize.math.vecmath.matrix.Matrix3
import pize.math.vecmath.matrix.Matrix3f
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec2f
import kotlin.math.max
import kotlin.math.min

class TextureBatchFast(maxSize: Int, maxTextures: Int, roundPosition: Boolean) : Batch() {
    private var vbo: VertexBuffer? = null
    private var vao: VertexArray? = null
    private var ebo: ElementBuffer? = null
    private val shader: Shader
    override val transformOrigin: Vec2f
    private val transformMatrix: Matrix3f
    private val rotationMatrix: Matrix3f
    private val shearMatrix: Matrix3f
    private val scaleMatrix: Matrix3f
    private val flipMatrix: Matrix3f
    private var size = 0
    private var vertexOffset = 0
    private val maxSize: Int
    private val vertices: FloatArray
    private val texSlots: IntArray
    private val textures: MutableList<Texture?>
    private var defaultView: Matrix4f? = null
    private var defaultProjection: Matrix4f? = null

    init {
        var maxTextures = maxTextures
        this.maxSize = maxSize
        maxTextures = min(
            max(2.0, maxTextures.toDouble()),
            MAX_TEXTURE_SLOTS.toDouble()
        ).toInt()

        // Shader
        val vs = Resource("shader/batch/fast_batch.vert").readString().replace(
            "POS_FUNC", if (roundPosition) "vec4(round(pos.x), round(pos.y), 0, pos.w)" else "pos"
        )
        val fs = Resource("shader/batch/fast_batch.frag").readString().replace(
            "TEX_SLOTS", maxTextures.toString()
        )
        shader = Shader(vs, fs)
        run {
            // Create VAO, VBO, EBO
            vao = VertexArray()
            vbo = VertexBuffer()
            vbo!!.enableAttributes(
                VertexAttr(2, Type.FLOAT),
                VertexAttr(2, Type.FLOAT),
                VertexAttr(4, Type.FLOAT, true),
                VertexAttr(1, Type.FLOAT)
            )
            ebo = ElementBuffer()
            val indices = IntArray(Batch.Companion.QUAD_INDICES * maxSize)
            for (i in 0 until maxSize) {
                val indexQuadOffset: Int = Batch.Companion.QUAD_INDICES * i
                val vertexQuadOffset: Int = Batch.Companion.QUAD_VERTICES * i
                indices[indexQuadOffset] = vertexQuadOffset
                indices[indexQuadOffset + 1] = vertexQuadOffset + 1
                indices[indexQuadOffset + 2] = vertexQuadOffset + 2
                indices[indexQuadOffset + 3] = vertexQuadOffset + 2
                indices[indexQuadOffset + 4] = vertexQuadOffset + 3
                indices[indexQuadOffset + 5] = vertexQuadOffset
            }
            ebo!!.setData(indices, BufferUsage.STATIC_DRAW)
        }
        texSlots = IntArray(maxTextures)
        for (i in 0 until maxTextures) texSlots[i] = i
        vertices = FloatArray(Batch.Companion.QUAD_VERTICES * maxSize * vbo?.vertexSize!!)
        textures = ArrayList()
        transformOrigin = Vec2f(0.5)
        transformMatrix = Matrix3f()
        rotationMatrix = Matrix3f()
        shearMatrix = Matrix3f()
        scaleMatrix = Matrix3f()
        flipMatrix = Matrix3f()
    }

    override fun draw(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        if (size + 1 >= maxSize) return
        size++
        if (!textures.contains(texture)) textures.add(texture)
        addTexturedQuad(x, y, width, height, 0f, 0f, 1f, 1f, getTextureIndex(texture))
    }

    override fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float) {
        if (size + 1 >= maxSize) return
        size++
        val texture = texReg.texture
        if (!textures.contains(texture)) textures.add(texture)
        addTexturedQuad(
            x,
            y,
            width,
            height,
            texReg.u1(),
            texReg.v1(),
            texReg.u2(),
            texReg.v2(),
            getTextureIndex(texture)
        )
    }

    override fun draw(texture: Texture?, x: Float, y: Float, width: Float, height: Float, region: Region) {
        if (size + 1 >= maxSize) return
        size++
        if (!textures.contains(texture)) textures.add(texture)
        addTexturedQuad(
            x,
            y,
            width,
            height,
            region.u1(),
            region.v1(),
            region.u2(),
            region.v2(),
            getTextureIndex(texture)
        )
    }

    override fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float, region: Region) {
        if (size + 1 >= maxSize) return
        size++
        val texture = texReg.texture
        if (!textures.contains(texture)) textures.add(texture)
        val regionInRegion: Region = Region.Companion.calcRegionInRegion(texReg, region)
        addTexturedQuad(
            x, y, width, height,
            regionInRegion.u1(),
            regionInRegion.v1(),
            regionInRegion.u2(),
            regionInRegion.v2(),
            getTextureIndex(texture)
        )
    }

    fun end(projection: Matrix4f?, view: Matrix4f?) {
        shader.bind()
        shader.setUniform("u_projection", projection)
        shader.setUniform("u_view", view)
        shader.setUniform("u_textures", texSlots) // Need a Uniform Buffer !!!
        for (i in textures.indices) textures[i]!!.bind(i + 1)
        vbo!!.setData(vertices, BufferUsage.STREAM_DRAW)
        vao!!.drawElements(size * Batch.Companion.QUAD_INDICES)

        // Reset
        size = 0
        vertexOffset = 0
        textures.clear()
    }

    fun end(cam: Camera) {
        end(cam?.projection!!, cam?.view!!)
    }

    fun end() {
        if (defaultView == null) defaultView = Matrix4f()
        if (defaultProjection == null) defaultProjection = Matrix4f()
        end(
            defaultProjection!!.toOrthographic(0f, 0f, Pize?.width!!.toFloat(), Pize?.height!!.toFloat()),
            defaultView
        )
    }

    private fun getTextureIndex(texture: Texture?): Int {
        var texIndex = 0
        for (i in textures.indices) if (textures[i] == texture) {
            texIndex = i + 1
            break
        }
        return texIndex
    }

    private fun addTexturedQuad(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        u1: Float,
        v1: Float,
        u2: Float,
        v2: Float,
        texIndex: Int
    ) {
        val origin = Vec2f(width * transformOrigin.x, height * transformOrigin.y)
        transformMatrix.set(rotationMatrix.getMul(scaleMatrix.getMul(shearMatrix.getMul(flipMatrix))))
        val vertex1 = Vec2f(0f, 0f).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex2 = Vec2f(width, 0f).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex3 = Vec2f(width, height).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex4 = Vec2f(0f, height).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        addVertex(vertex1!!.x, vertex1.y, u1, v2, texIndex)
        addVertex(vertex2!!.x, vertex2.y, u2, v2, texIndex)
        addVertex(vertex3!!.x, vertex3.y, u2, v1, texIndex)
        addVertex(vertex4!!.x, vertex4.y, u1, v1, texIndex)
    }

    private fun addVertex(x: Float, y: Float, s: Float, t: Float, texIndex: Int) {
        vertices[vertexOffset] = x
        vertices[vertexOffset + 1] = y
        vertices[vertexOffset + 2] = s
        vertices[vertexOffset + 3] = t
        vertices[vertexOffset + 4] = color.r()
        vertices[vertexOffset + 5] = color.g()
        vertices[vertexOffset + 6] = color.b()
        vertices[vertexOffset + 7] = color.a()
        vertices[vertexOffset + 8] = texIndex.toFloat()
        vertexOffset += vbo?.vertexSize!!
    }

    override fun setTransformOrigin(x: Double, y: Double) {
        transformOrigin[x] = y
    }

    override fun rotate(angle: Float) {
        rotationMatrix.toRotated(angle.toDouble())
    }

    override fun shear(angleX: Float, angleY: Float) {
        shearMatrix.toSheared(angleX, angleY)
    }

    override fun scale(scale: Float) {
        scaleMatrix.toScaled(scale)
    }

    override fun scale(x: Float, y: Float) {
        scaleMatrix.toScaled(x, y)
    }

    override fun flip(x: Boolean, y: Boolean) {
        flipMatrix.`val`[Matrix3.Companion.m00.toInt()] = (if (y) 1 else -1).toFloat()
        flipMatrix.`val`[Matrix3.Companion.m11.toInt()] = (if (x) 1 else -1).toFloat()
    }

    override fun size(): Int {
        return size
    }

    override fun dispose() {
        shader.dispose()
        vbo!!.dispose()
        vao!!.dispose()
        ebo!!.dispose()
    }

    companion object {
        var MAX_TEXTURE_SLOTS = GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)
    }
}