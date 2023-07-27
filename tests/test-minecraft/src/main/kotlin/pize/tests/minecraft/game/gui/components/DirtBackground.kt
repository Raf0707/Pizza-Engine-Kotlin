package pize.tests.minecraft.game.gui.components

import pize.Pize
import pize.graphics.gl.Wrap
import pize.graphics.texture.Region
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.gui.constraint.Constraint.Companion.pixel
import pize.tests.minecraft.game.Session

class DirtBackground(session: Session) : MComponent() {
    private val texture: Texture?

    init {
        texture = session.resourceManager.getTexture("options_background").texture
        texture.parameters.setWrap(Wrap.REPEAT)
        texture.update()
        this.setSize(pixel(1.0))
    }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        val size = 5
        batch.setColor(0.25, 0.25, 0.25, 1.0)
        batch.draw(
            texture, 0f, 0f, Pize.width.toFloat(), Pize.height.toFloat(), Region(
                0f, 0f,
                Pize.width / (8f * size * width),
                Pize.height / (8f * size * height)
            )
        )
        batch.setColor(1.0, 1.0, 1.0, 1.0)
    }
}