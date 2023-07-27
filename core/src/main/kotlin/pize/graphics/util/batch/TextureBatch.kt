package pize.graphics.util.batch

import pize.Pize
import pize.files.Resource
import pize.graphics.camera.Camera
import pize.graphics.gl.*
import pize.graphics.texture.*
import pize.graphics.util.Shader
import pize.graphics.util.TextureUtils
import pize.graphics.util.batch.scissor.Scissor
import pize.graphics.util.color.IColor
import pize.graphics.vertex.*
import pize.math.vecmath.matrix.Matrix3
import pize.math.vecmath.matrix.Matrix3f
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec2f

class TextureBatch @JvmOverloads constructor(private val batchSize: Int = 256) : Batch() {
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
    private val vertices: FloatArray
    private var lastTexture: Texture? = null
    private var projectionMatrix: Matrix4f? = null
    private var viewMatrix: Matrix4f? = null
    private var customShader: Shader? = null
    @JvmField
    val scissor = Scissor(this)

    init {

        // Shader
        shader = Shader(
            Resource("shader/batch/batch.vert").readString(),
            Resource("shader/batch/batch.frag").readString()
        )
        run {
            // Create VAO, VBO, EBO
            vao = VertexArray()
            vbo = VertexBuffer()
            vbo.enableAttributes(VertexAttr(2, Type.FLOAT), VertexAttr(2, Type.FLOAT), VertexAttr(4, Type.FLOAT, false))
            ebo = ElementBuffer()
            val indices = IntArray(Batch.Companion.QUAD_INDICES * batchSize)
            for (i in 0 until batchSize) {
                val indexQuadOffset: Int = Batch.Companion.QUAD_INDICES * i
                val vertexQuadOffset: Int = Batch.Companion.QUAD_VERTICES * i
                indices[indexQuadOffset] = vertexQuadOffset
                indices[indexQuadOffset + 1] = vertexQuadOffset + 1
                indices[indexQuadOffset + 2] = vertexQuadOffset + 2
                indices[indexQuadOffset + 3] = vertexQuadOffset + 2
                indices[indexQuadOffset + 4] = vertexQuadOffset + 3
                indices[indexQuadOffset + 5] = vertexQuadOffset
            }
            ebo.setData(indices, BufferUsage.STATIC_DRAW)
        }
        vertices = FloatArray(Batch.Companion.QUAD_VERTICES * batchSize * vbo.getVertexSize())
        transformOrigin = Vec2f(0.5)
        transformMatrix = Matrix3f()
        rotationMatrix = Matrix3f()
        shearMatrix = Matrix3f()
        scaleMatrix = Matrix3f()
        flipMatrix = Matrix3f()
    }

    override fun draw(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        if (size + 1 >= batchSize) end()
        if (texture !== lastTexture) {
            end()
            lastTexture = texture
        }
        addTexturedQuad(x, y, width, height, 0f, 0f, 1f, 1f, color.r(), color.g(), color.b(), color.a())
        size++
    }

    fun draw(
        texture: Texture?,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        if (size + 1 >= batchSize) end()
        if (texture !== lastTexture) {
            end()
            lastTexture = texture
        }
        addTexturedQuad(x, y, width, height, 0f, 0f, 1f, 1f, r, g, b, a)
        size++
    }

    fun drawQuad(r: Double, g: Double, b: Double, a: Double, x: Float, y: Float, width: Float, height: Float) {
        draw(TextureUtils.quadTexture(), x, y, width, height, r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
    }

    fun drawQuad(color: IColor, x: Float, y: Float, width: Float, height: Float) {
        drawQuad(
            color.r().toDouble(),
            color.g().toDouble(),
            color.b().toDouble(),
            color.a().toDouble(),
            x,
            y,
            width,
            height
        )
    }

    fun drawQuad(alpha: Double, x: Float, y: Float, width: Float, height: Float) {
        drawQuad(0.0, 0.0, 0.0, alpha, x, y, width, height)
    }

    override fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float) {
        if (size + 1 >= batchSize) end()
        val texture = texReg.texture
        if (texture !== lastTexture) {
            end()
            lastTexture = texture
        }
        addTexturedQuad(
            x,
            y,
            width,
            height,
            texReg.u1(),
            texReg.v1(),
            texReg.u2(),
            texReg.v2(),
            color.r(),
            color.g(),
            color.b(),
            color.a()
        )
        size++
    }

    override fun draw(texture: Texture?, x: Float, y: Float, width: Float, height: Float, region: Region) {
        if (size + 1 >= batchSize) end()
        if (texture !== lastTexture) {
            end()
            lastTexture = texture
        }
        addTexturedQuad(
            x,
            y,
            width,
            height,
            region.u1(),
            region.v1(),
            region.u2(),
            region.v2(),
            color.r(),
            color.g(),
            color.b(),
            color.a()
        )
        size++
    }

    override fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float, region: Region) {
        if (size + 1 >= batchSize) end()
        val texture = texReg.texture
        if (texture !== lastTexture) {
            end()
            lastTexture = texture
        }
        val regionInRegion: Region = Region.Companion.calcRegionInRegion(texReg, region)
        addTexturedQuad(
            x, y, width, height,
            regionInRegion.u1(),
            regionInRegion.v1(),
            regionInRegion.u2(),
            regionInRegion.v2(),
            color.r(),
            color.g(),
            color.b(),
            color.a()
        )
        size++
    }

    fun begin(projection: Matrix4f?, view: Matrix4f?) {
        projectionMatrix = projection
        viewMatrix = view
    }

    fun begin(camera: Camera) {
        begin(camera.getProjection(), camera.getView())
    }

    fun begin() {
        if (viewMatrix == null) viewMatrix = Matrix4f()
        if (projectionMatrix == null) projectionMatrix = Matrix4f()
        begin(
            projectionMatrix!!.toOrthographic(0f, 0f, Pize.getWidth().toFloat(), Pize.getHeight().toFloat()),
            viewMatrix
        )
    }

    fun end(): Int {
        if (lastTexture == null || size == 0) return -1
        val usedShader = if (customShader == null) shader else customShader!!
        usedShader.bind()
        usedShader.setUniform("u_projection", projectionMatrix)
        usedShader.setUniform("u_view", viewMatrix)
        usedShader.setUniform("u_texture", lastTexture)
        vbo!!.setData(vertices, BufferUsage.STREAM_DRAW)
        vao!!.drawElements(size * Batch.Companion.QUAD_INDICES)

        // Reset
        val sizeResult = size
        size = 0
        vertexOffset = 0
        return sizeResult
    }

    fun useShader(shader: Shader?) {
        customShader = shader
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
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        val origin = Vec2f(width * transformOrigin.x, height * transformOrigin.y)
        transformMatrix.set(rotationMatrix.getMul(scaleMatrix.getMul(shearMatrix.getMul(flipMatrix))))
        val vertex1 = Vec2f(0f, 0f).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex2 = Vec2f(width, 0f).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex3 = Vec2f(width, height).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        val vertex4 = Vec2f(0f, height).sub(origin).mul(transformMatrix).add(origin).add(x, y)
        addVertex(vertex1!!.x, vertex1.y, u1, v2, r, g, b, a)
        addVertex(vertex2!!.x, vertex2.y, u2, v2, r, g, b, a)
        addVertex(vertex3!!.x, vertex3.y, u2, v1, r, g, b, a)
        addVertex(vertex4!!.x, vertex4.y, u1, v1, r, g, b, a)
    }

    private fun addVertex(x: Float, y: Float, s: Float, t: Float, r: Float, g: Float, b: Float, a: Float) {
        vertices[vertexOffset] = x
        vertices[vertexOffset + 1] = y
        vertices[vertexOffset + 2] = s
        vertices[vertexOffset + 3] = t
        vertices[vertexOffset + 4] = r
        vertices[vertexOffset + 5] = g
        vertices[vertexOffset + 6] = b
        vertices[vertexOffset + 7] = a
        vertexOffset += vbo.getVertexSize()
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
}
