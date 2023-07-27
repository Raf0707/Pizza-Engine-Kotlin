package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.gl.Primitive
import pize.graphics.gl.Type
import pize.graphics.util.Shader
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.main.chunk.ChunkUtils

class ChunkBorderRenderer(private val levelRenderer: LevelRenderer) : Disposable {
    private val shader: Shader
    private val mesh: Mesh
    private val translationMatrix: Matrix4f
    var isShow = false
        private set

    init {
        shader = Shader(Resource("shader/line.vert"), Resource("shader/line.frag"))
        mesh = Mesh(VertexAttr(3, Type.FLOAT))
        mesh.setRenderMode(Primitive.LINES)
        mesh.setVertices(
            floatArrayOf(
                16f, 256f, 16f,  //0
                0f, 256f, 16f,  //1
                16f, 0f, 16f,  //2
                0f, 0f, 16f,  //3
                16f, 256f, 0f,  //4
                0f, 256f, 0f,  //5
                16f, 0f, 0f,  //6
                0f, 0f, 0f
            )
        )
        mesh.setIndices(
            intArrayOf(
                7, 6, 6, 2, 2, 3, 3, 7,  //Top
                0, 4, 4, 5, 5, 1, 1, 0,  //Bottom
                0, 2, 2, 6, 6, 4, 4, 0,  //Left
                7, 3, 3, 1, 1, 5, 5, 7,  //Right
                3, 2, 2, 0, 0, 1, 1, 3,  //Front
                4, 6, 6, 7, 7, 5, 5, 4
            )
        )
        translationMatrix = Matrix4f()
    }

    fun show(show: Boolean) {
        isShow = show
    }

    fun toggleShow() {
        show(!isShow)
    }

    fun render(camera: GameCamera) {
        if (!isShow) return
        shader.bind()
        shader.setUniform("u_projection", camera.projection)
        shader.setUniform("u_view", camera.view)
        val position =
            Vec3f(camera.chunkX() * ChunkUtils.SIZE, 0, camera.chunkZ() * ChunkUtils.SIZE).sub(camera.x, 0f, camera.z)
        translationMatrix.toTranslated(position)
        shader.setUniform("u_model", translationMatrix)
        mesh.render()
    }

    override fun dispose() {
        shader.dispose()
        mesh.dispose()
    }
}
