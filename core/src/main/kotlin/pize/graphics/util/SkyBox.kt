package pize.graphics.util

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.camera.Camera
import pize.graphics.gl.Gl
import pize.graphics.gl.Type
import pize.graphics.texture.CubeMap
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr
import pize.math.vecmath.matrix.Matrix4f

class SkyBox @JvmOverloads constructor(
    px: String? =
        "skybox/2/skybox_positive_x.png", nx: String? =
        "skybox/2/skybox_negative_x.png", py: String? =
        "skybox/2/skybox_positive_y.png", ny: String? =
        "skybox/2/skybox_negative_y.png", pz: String? =
        "skybox/2/skybox_positive_z.png", nz: String? =
        "skybox/2/skybox_negative_z.png"
) : Disposable {
    val cubeMap: CubeMap
    private val shader: Shader
    private val mesh: Mesh

    init {
        cubeMap = CubeMap(px, nx, py, ny, pz, nz)
        shader = Shader(Resource("shader/skybox/skybox.vert"), Resource("shader/skybox/skybox.frag"))
        mesh = Mesh(VertexAttr(3, Type.FLOAT))
        mesh.setVertices(
            floatArrayOf(
                -1f, -1f, 1f,  //0
                1f, -1f, 1f,  //1
                -1f, 1f, 1f,  //2
                1f, 1f, 1f,  //3
                -1f, -1f, -1f,  //4
                1f, -1f, -1f,  //5
                -1f, 1f, -1f,  //6
                1f, 1f, -1f //7
            )
        )
        mesh.setIndices(
            intArrayOf( //Top
                7, 6, 2,
                2, 3, 7,  //Bottom
                0, 4, 5,
                5, 1, 0,  //Left
                0, 2, 6,
                6, 4, 0,  //Right
                7, 3, 1,
                1, 5, 7,  //Front
                3, 2, 0,
                0, 1, 3,  //Back
                4, 6, 7,
                7, 5, 4
            )
        )
    }

    fun render(projection: Matrix4f?, view: Matrix4f?) {
        Gl.depthMask(false)
        shader.bind()
        shader.setUniform("u_projection", projection)
        shader.setUniform("u_view", view)
        shader.setUniform("u_cubeMap", cubeMap)
        mesh.render()
        Gl.depthMask(true)
    }

    fun render(camera: Camera) {
        val view = camera.getView().copy().cullPosition()
        render(camera.getProjection(), view)
    }

    override fun dispose() {
        cubeMap.dispose()
        shader.dispose()
        mesh.dispose()
    }
}
