package pize.gui.components

import pize.app.Disposable
import pize.graphics.font.BitmapFont
import pize.graphics.util.batch.TextureBatch
import pize.util.io.TextProcessor

class TextEdit(font: BitmapFont?, newLineOnEnter: Boolean) : TextView("", font), Disposable {
    private val textProcessor: TextProcessor

    init {
        textProcessor = TextProcessor(newLineOnEnter)
    }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        super.setText(textProcessor.toString())
        super.render(batch, x, y, width, height)
    }

    override fun dispose() {
        textProcessor.dispose()
    }
}
