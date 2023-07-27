package pize.tests.voxelgame.client.entity.model

import pize.files.Resource
import pize.graphics.texture.Texture
import pize.graphics.util.Shader
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.entity.AbstractClientPlayer
import pize.tests.voxelgame.main.entity.Player

open class HumanoidModel(protected val player: AbstractClientPlayer) {
    protected val torso: ModelPart
    protected val head: ModelPart
    protected val leftLeg: ModelPart
    protected val rightLeg: ModelPart
    protected val leftArm: ModelPart
    protected val rightArm: ModelPart
    protected val shader: Shader
    protected val skinTexture: Texture

    init {
        shader = Shader(Resource("shader/level/entity/model.vert"), Resource("shader/level/entity/model.frag"))
        val skinID: Int = kotlin.math.abs(player.name.hashCode().toDouble()) % 20 + 1
        skinTexture = Texture("texture/skin/skin$skinID.png")
        head = ModelPart(
            BoxBuilder(-4 * w, 0 * w, -4 * w, 4 * w, 8 * w, 4 * w)
                .nx(1f, 1f, 1f, 1f, 24 * t, 8 * t, 32 * t, 16 * t)
                .px(1f, 1f, 1f, 1f, 8 * t, 8 * t, 16 * t, 16 * t)
                .ny(1f, 1f, 1f, 1f, 16 * t, 0 * t, 24 * t, 8 * t)
                .py(1f, 1f, 1f, 1f, 8 * t, 0 * t, 16 * t, 8 * t)
                .pz(1f, 1f, 1f, 1f, 16 * t, 8 * t, 24 * t, 16 * t)
                .nz(1f, 1f, 1f, 1f, 0 * t, 8 * t, 8 * t, 16 * t)
                .end()
        )
        head.setInitialPose(0.0, (24 * w).toDouble(), 0.0)
        torso = ModelPart(
            BoxBuilder(-2 * w, -6 * w, -4 * w, 2 * w, 6 * w, 4 * w)
                .nx(1f, 1f, 1f, 1f, 32 * t, 20 * t, 40 * t, 32 * t)
                .px(1f, 1f, 1f, 1f, 20 * t, 20 * t, 28 * t, 32 * t)
                .ny(1f, 1f, 1f, 1f, 28 * t, 16 * t, 36 * t, 20 * t)
                .py(1f, 1f, 1f, 1f, 20 * t, 16 * t, 28 * t, 20 * t)
                .pz(1f, 1f, 1f, 1f, 28 * t, 20 * t, 32 * t, 32 * t)
                .nz(1f, 1f, 1f, 1f, 16 * t, 20 * t, 20 * t, 32 * t)
                .end()
        )
        torso.setInitialPose(0.0, (18 * w).toDouble(), 0.0)
        leftLeg = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 28 * t, 52 * t, 32 * t, 64 * t)
                .px(1f, 1f, 1f, 1f, 20 * t, 52 * t, 24 * t, 64 * t)
                .ny(1f, 1f, 1f, 1f, 24 * t, 48 * t, 28 * t, 52 * t)
                .py(1f, 1f, 1f, 1f, 20 * t, 48 * t, 24 * t, 52 * t)
                .pz(1f, 1f, 1f, 1f, 24 * t, 52 * t, 28 * t, 64 * t)
                .nz(1f, 1f, 1f, 1f, 16 * t, 52 * t, 20 * t, 64 * t)
                .end()
        )
        leftLeg.setParent(torso)
        leftLeg.setInitialPose(0.0, (-8 * w).toDouble(), (2 * w).toDouble())
        rightLeg = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 12 * t, 20 * t, 16 * t, 32 * t)
                .px(1f, 1f, 1f, 1f, 4 * t, 20 * t, 8 * t, 32 * t)
                .ny(1f, 1f, 1f, 1f, 8 * t, 16 * t, 12 * t, 20 * t)
                .py(1f, 1f, 1f, 1f, 4 * t, 16 * t, 8 * t, 20 * t)
                .pz(1f, 1f, 1f, 1f, 8 * t, 20 * t, 12 * t, 32 * t)
                .nz(1f, 1f, 1f, 1f, 0 * t, 20 * t, 4 * t, 32 * t)
                .end()
        )
        rightLeg.setParent(torso)
        rightLeg.setInitialPose(0.0, (-8 * w).toDouble(), (-2 * w).toDouble())
        leftArm = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 44 * t, 52 * t, 48 * t, 64 * t)
                .px(1f, 1f, 1f, 1f, 36 * t, 52 * t, 40 * t, 64 * t)
                .ny(1f, 1f, 1f, 1f, 40 * t, 48 * t, 44 * t, 52 * t)
                .py(1f, 1f, 1f, 1f, 36 * t, 48 * t, 40 * t, 52 * t)
                .pz(1f, 1f, 1f, 1f, 40 * t, 52 * t, 44 * t, 64 * t)
                .nz(1f, 1f, 1f, 1f, 32 * t, 52 * t, 36 * t, 64 * t)
                .end()
        )
        leftArm.setParent(torso)
        leftArm.setInitialPose(0.0, (4 * w).toDouble(), (6 * w).toDouble())
        rightArm = ModelPart(
            BoxBuilder(-2 * w, -10 * w, -2 * w, 2 * w, 2 * w, 2 * w)
                .nx(1f, 1f, 1f, 1f, 52 * t, 20 * t, 56 * t, 32 * t)
                .px(1f, 1f, 1f, 1f, 44 * t, 20 * t, 48 * t, 32 * t)
                .ny(1f, 1f, 1f, 1f, 48 * t, 16 * t, 52 * t, 20 * t)
                .py(1f, 1f, 1f, 1f, 44 * t, 16 * t, 48 * t, 20 * t)
                .pz(1f, 1f, 1f, 1f, 48 * t, 20 * t, 52 * t, 32 * t)
                .nz(1f, 1f, 1f, 1f, 40 * t, 20 * t, 44 * t, 32 * t)
                .end()
        )
        rightArm.setParent(torso)
        rightArm.setInitialPose(0.0, (4 * w).toDouble(), (-6 * w).toDouble())
    }

    fun getPlayer(): Player {
        return player
    }

    open fun render(camera: GameCamera?) {
        if (camera == null) return
        shader.bind()
        shader.setUniform("u_projection", camera.projection)
        shader.setUniform("u_view", camera.view)
        shader.setUniform("u_texture", skinTexture)
        torso.render(camera, shader, "u_model")
        head.render(camera, shader, "u_model")
        leftLeg.render(camera, shader, "u_model")
        rightLeg.render(camera, shader, "u_model")
        leftArm.render(camera, shader, "u_model")
        rightArm.render(camera, shader, "u_model")
    }

    open fun dispose() {
        torso.mesh.dispose()
        head.mesh.dispose()
        leftLeg.mesh.dispose()
        rightLeg.mesh.dispose()
        leftArm.mesh.dispose()
        rightArm.mesh.dispose()
        shader.dispose()
        skinTexture.dispose()
    }

    companion object {
        private const val t = 1 / 64f // Pixel size on texture
        private const val w = 1.8f / 32 // Pixel size in world
    }
}
