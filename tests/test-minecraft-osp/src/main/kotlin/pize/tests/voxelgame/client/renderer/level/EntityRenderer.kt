package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.entity.RemotePlayer
import pize.tests.voxelgame.client.entity.model.PlayerModel

class EntityRenderer(val levelRenderer: LevelRenderer) : Disposable {
    fun render(camera: GameCamera?) {
        val level = levelRenderer.gameRenderer.session.game.level
        for (entity in level.entities) {

            // Remote players
            if (entity is RemotePlayer) {
                entity.updateInterpolation()
                val model: PlayerModel = entity.model
                if (model != null) {
                    model.animate()
                    model.render(camera)
                }
            }
        }
    }

    override fun dispose() {}
}
