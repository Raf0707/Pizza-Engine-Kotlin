package pize.tests.editor

import pize.Pize.create
import pize.Pize.run
import pize.app.AppAdapter
import pize.graphics.font.BitmapFont
import pize.graphics.font.FontLoader.default
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.util.io.TextProcessor
import java.util.*

class Main : AppAdapter() {
    private var batch: TextureBatch? = null
    private var font: BitmapFont? = null
    private var text: TextProcessor? = null
    override fun init() {
        batch = TextureBatch()
        font = default
        text = TextProcessor(true)
    }

    override fun render() {
        if (Key.LEFT_CONTROL.isPressed && Key.Y.isDown) text!!.removeLine()
        clearColorBuffer()
        clearColor(0.2, 0.2, 0.2, 1.0)
        batch!!.begin()

        // Iterate lines
        val lineNumbersJoiner = StringJoiner("\n")
        val lines = text!!.getLines()
        for (i in lines.indices) {
            // Add line number
            lineNumbersJoiner.add((i + 1).toString())

            // Draw line background
            val lineWidth = font!!.getLineWidth(lines[i])
            val advance = font!!.lineAdvanceScaled
            batch!!.drawQuad(0.1, 0.15, 0.2, 1.0, 50f, 10 + (lines.size - 1 - i) * advance, lineWidth, advance)
            batch!!.drawQuad(0.3, 0.45, 0.5, 1.0, 0f, 10 + (lines.size - 1 - i) * advance, 50f, advance)
        }
        // Draw line numbers
        font!!.drawText(batch!!, lineNumbersJoiner.toString(), 5f, 10f)

        // Draw text
        font!!.drawText(batch!!, text.toString(), 50f, 10f)

        // Draw cursor
        if (System.currentTimeMillis() / 500 % 2 == 0L && text!!.isActive) {
            val currentLine = text!!.currentLine
            val cursorY =
                (text!!.cursorY + 1) * font!!.lineAdvanceScaled - font!!.lineAdvanceScaled * text!!.getLines().size
            val cursorX = font!!.getLineWidth(currentLine!!.substring(0, text!!.cursorX))
            batch!!.drawQuad(1.0, 1.0, 1.0, 1.0, 50 + cursorX, 10 + cursorY, 2f, font!!.lineAdvanceScaled)
        }
        batch!!.end()
    }

    override fun dispose() {
        text!!.dispose()
        batch!!.dispose()
        font!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Editor", 1280, 720)
            run(Main())
        }
    }
}
