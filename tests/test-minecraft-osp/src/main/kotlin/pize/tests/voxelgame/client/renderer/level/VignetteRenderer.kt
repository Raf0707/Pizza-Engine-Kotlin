package pize.tests.voxelgame.client.renderer.level

import pize.Pize.height
import pize.Pize.width
import pize.app.Disposable
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch

class VignetteRenderer(val levelRenderer: LevelRenderer) : Disposable {
    private val vignetteTexture: Texture
    var vignette = 0f

    init {
        vignetteTexture = Texture("texture/vignette.png")
    }

    fun render(batch: TextureBatch) {
        batch.begin()

        // Get light level
        val game = levelRenderer.gameRenderer.session.game
        val playerPos = game.player.position
        val level = game.level
        val light = level!!.getLight(playerPos.xf(), playerPos.yf(), playerPos.zf()).toFloat()

        // Interpolation
        vignette += (1 - light / 15f - vignette) / 100f

        // Render
        batch.setAlpha(vignette.toDouble())
        batch.draw(vignetteTexture, 0f, 0f, width.toFloat(), height.toFloat())
        batch.resetColor()
        batch.end()
    }

    override fun dispose() {
        vignetteTexture.dispose()
    }
}
