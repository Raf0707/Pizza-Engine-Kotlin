package pize.tests.minecraft.game.gui.text

import pize.graphics.font.FontCharset
import pize.math.Maths.random
import pize.tests.minecraft.game.gui.text.formatting.Style

class TextComponent(parent: Component?, private var text: String, style: Style?) : Component() {
    override var style: Style?
        get() = style
        set(style) {
            this.style = style
        }

    init {
        if (parent != null && (style === Style.Companion.DEFAULT || style == null)) this.style =
            Style(parent.color, *parent.style) else this.style = style
    }

    fun getText(): String {
        if (style!!.obfuscated) {
            val obfuscatedBuilder = StringBuilder()
            for (i in 0 until text.length) obfuscatedBuilder.append(
                FontCharset.DEFAULT.charAt(
                    random(
                        0,
                        FontCharset.DEFAULT.size() - 1
                    )
                )
            )
            return obfuscatedBuilder.toString()
        } else if (style!!.obfuscated_numbers) {
            val obfuscatedBuilder = StringBuilder()
            for (i in 0 until text.length) obfuscatedBuilder.append(
                FontCharset.NUMBERS.charAt(
                    random(
                        0,
                        FontCharset.NUMBERS.size() - 1
                    )
                )
            )
            return obfuscatedBuilder.toString()
        }
        return text
    }

    fun setText(text: String) {
        this.text = text
    }
}
