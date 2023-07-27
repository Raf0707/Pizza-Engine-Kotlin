package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.control.camera.PerspectiveType

class PlayerRenderer(val levelRenderer: LevelRenderer) : Disposable {
    fun render(camera: GameCamera) {
        val options = levelRenderer.gameRenderer.session.options
        val playerModel = levelRenderer.gameRenderer.session.game.player.model

        // Render player
        if (playerModel != null) {
            playerModel.animate()
            if (camera.perspective != PerspectiveType.FIRST_PERSON || options!!.isFirstPersonModel) playerModel.render(
                camera
            )
        }
    }

    override fun dispose() {}
}