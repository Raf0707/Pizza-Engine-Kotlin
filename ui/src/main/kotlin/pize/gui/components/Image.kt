package pize.gui.components

import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent

class Image(private val texture: TextureRegion) : UIComponent<TextureBatch>() {
    constructor(texture: Texture?) : this(TextureRegion(texture))

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        batch.draw(texture, x, y, width, height)
    }
}
