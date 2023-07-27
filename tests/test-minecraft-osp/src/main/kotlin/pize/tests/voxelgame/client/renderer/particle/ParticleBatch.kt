package pize.tests.voxelgame.client.renderer.particle

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.camera.PerspectiveCamera
import pize.graphics.gl.BufferUsage
import pize.graphics.gl.Type
import pize.graphics.texture.Texture
import pize.graphics.util.Shader
import pize.graphics.util.batch.Batch
import pize.graphics.util.color.Color
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f
import java.util.concurrent.CopyOnWriteArrayList

class ParticleBatch(val size: Int) : Disposable {
    val instances: CopyOnWriteArrayList<ParticleInstance?>
    val shader: Shader
    val rotateMatrix: Matrix4f
    val mesh: Mesh
    var lastTexture: Texture? = null
    val currentColor: Color
    val vertices: FloatArray
    var vertexIndex = 0
    var particleIndex = 0

    init {
        instances = CopyOnWriteArrayList()
        shader = Shader(
            Resource("shader/level/particle/particle-batch.vert"),
            Resource("shader/level/particle/particle-batch.frag")
        )
        // Matrices
        rotateMatrix = Matrix4f()
        // Mesh
        mesh = Mesh(
            VertexAttr(3, Type.FLOAT),  // pos3
            VertexAttr(4, Type.FLOAT),  // col4
            VertexAttr(2, Type.FLOAT) // uv2
        )
        // Generate indices
        val indices = IntArray(QUAD_INDICES * size)
        for (i in 0 until size) {
            val quadIndexPointer = i * QUAD_INDICES
            val quadVertexPointer = i * QUAD_VERTICES
            indices[quadIndexPointer] = quadVertexPointer
            indices[quadIndexPointer + 1] = quadVertexPointer + 1
            indices[quadIndexPointer + 2] = quadVertexPointer + 2
            indices[quadIndexPointer + 3] = quadVertexPointer + 2
            indices[quadIndexPointer + 4] = quadVertexPointer + 3
            indices[quadIndexPointer + 5] = quadVertexPointer
        }
        mesh.eBO.setData(indices, BufferUsage.STATIC_DRAW)
        // Vertices array
        vertices = FloatArray(QUAD_VERTICES * size * mesh.vBO.vertexSize)
        // Color
        currentColor = Color()
    }

    fun spawnParticle(particle: Particle?, position: Vec3f) {
        val instance = particle!!.createInstance(position)
        instances.add(instance)
    }

    fun render(camera: PerspectiveCamera) {
        setup(camera) // Setup shader
        for (instance in instances) {
            instance!!.update()
            if (instance.elapsedTimeSeconds > instance.lifeTimeSeconds) {
                instances.remove(instance)
                continue
            }
            renderInstance(instance, camera)
        }
        flush() // Render
    }

    private fun renderInstance(instance: ParticleInstance?, camera: PerspectiveCamera) {
        if (particleIndex >= size) flush()

        // Texture
        val texture = instance.getParticle().texture
        if (texture != lastTexture) {
            flush()
            lastTexture = texture
        }
        val region = instance!!.region

        // Color
        currentColor.set(instance.color)
        currentColor.setA(currentColor.a() * instance.alpha)

        // Matrix
        rotateMatrix.toRotatedZ(instance.rotation.toDouble()).mul(Matrix4f().toLookAt(camera.rotation.direction))

        // Setup vertices
        val v0 = Vec3f(-0.5, 0.5, 0.0).mul(rotateMatrix).mul(instance.size).add(instance.position)
        val v1 = Vec3f(-0.5, -0.5, 0.0).mul(rotateMatrix).mul(instance.size).add(instance.position)
        val v2 = Vec3f(0.5, -0.5, 0.0).mul(rotateMatrix).mul(instance.size).add(instance.position)
        val v3 = Vec3f(0.5, 0.5, 0.0).mul(rotateMatrix).mul(instance.size).add(instance.position)

        // Add vertices
        addVertex(v0.x, v0.y, v0.z, region!!.u1(), region.v1())
        addVertex(v1.x, v1.y, v1.z, region.u1(), region.v2())
        addVertex(v2.x, v2.y, v2.z, region.u2(), region.v2())
        addVertex(v3.x, v3.y, v3.z, region.u2(), region.v1())
        particleIndex++
    }

    private fun addVertex(x: Float, y: Float, z: Float, u: Float, v: Float) {
        val pointer = vertexIndex * mesh.vBO.vertexSize

        // pos3
        vertices[pointer] = x
        vertices[pointer + 1] = y
        vertices[pointer + 2] = z
        // col4
        vertices[pointer + 3] = currentColor.r()
        vertices[pointer + 4] = currentColor.g()
        vertices[pointer + 5] = currentColor.b()
        vertices[pointer + 6] = currentColor.a()
        // uv2
        vertices[pointer + 7] = u
        vertices[pointer + 8] = v
        vertexIndex++
    }

    private fun setup(camera: PerspectiveCamera) {
        shader.bind()
        shader.setUniform("u_projection", camera.projection)
        shader.setUniform("u_view", camera.imaginaryView)
    }

    private fun flush() {
        if (lastTexture == null) return
        shader.setUniform("u_texture", lastTexture)
        mesh.vBO.setData(vertices, BufferUsage.STREAM_DRAW)
        mesh.vAO.drawElements(particleIndex * QUAD_INDICES)
        vertexIndex = 0
        particleIndex = 0
    }

    override fun dispose() {
        shader.dispose()
        mesh.dispose()
    }

    companion object {
        val QUAD_VERTICES = Batch.QUAD_VERTICES
        val QUAD_INDICES = Batch.QUAD_INDICES
    }
}