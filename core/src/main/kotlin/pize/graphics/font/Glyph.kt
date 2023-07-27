package pize.graphics.font

import pize.graphics.texture.Region
import pize.graphics.util.batch.Batch

class Glyph(
    var fontOf: BitmapFont,
    val id: Int,
    @JvmField val offsetX: Float,
    @JvmField val offsetY: Float,
    val width: Float,
    val height: Float,
    val region: Region,
    @JvmField val advanceX: Float,
    val page: Int
) {
    fun render(batch: Batch, x: Float, y: Float) {
        val scale = fontOf.scale
        batch.draw(
            fontOf.getPage(page),
            x,
            y,
            width * scale,
            height * scale,
            region
        )
    }
}