package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.util.Shader
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f
import pize.tests.voxelgame.client.control.camera.GameCamera

class BlockSelectorRenderer(private val levelRenderer: LevelRenderer) : Disposable {
    private val shader: Shader
    private val translationMatrix: Matrix4f

    init {
        shader = Shader(Resource("shader/line.vert"), Resource("shader/line.frag"))
        translationMatrix = Matrix4f()
    }

    fun render(camera: GameCamera) {
        val rayCast = levelRenderer.gameRenderer.session.game.blockRayCast
        if (!rayCast!!.isSelected) return
        shader.bind()
        shader.setUniform("u_projection", camera.projection)
        shader.setUniform("u_view", camera.view)
        translationMatrix.toTranslated(Vec3f(rayCast.selectedBlockPosition).sub(camera.x, 0f, camera.z))
        shader.setUniform("u_model", translationMatrix)
        val shape = rayCast.selectedBlockProps.getState(rayCast.selectedBlockState.toInt()).cursor
        shape?.render()
    }

    override fun dispose() {
        shader.dispose()
    }
}
