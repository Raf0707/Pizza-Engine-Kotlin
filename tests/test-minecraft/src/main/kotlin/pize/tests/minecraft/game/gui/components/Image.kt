package pize.tests.minecraft.game.gui.components

import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch

class Image(private val texture: TextureRegion?) : MComponent() {
    constructor(texture: Texture?) : this(TextureRegion(texture))

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        batch.draw(texture!!, x, y, width, height)
    }
}