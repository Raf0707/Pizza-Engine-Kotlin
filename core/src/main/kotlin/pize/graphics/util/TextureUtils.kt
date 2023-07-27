package pize.graphics.util

import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture

object TextureUtils {
    private var whitePixel: Texture? = null
    fun quadTexture(): Texture? {
        if (whitePixel == null) {
            val whitePixelPixmap = Pixmap(1, 1)
            whitePixelPixmap.setPixel(0, 0, 1.0, 1.0, 1.0, 1.0)
            whitePixel = Texture(whitePixelPixmap)
        }
        return whitePixel
    }

    private fun dispose() { // Invoked from Context
        if (whitePixel != null) whitePixel!!.dispose()
    }
}
