package pize.tests.minecraft.game.gui.text.formatting

import pize.graphics.util.color.IColor
import pize.graphics.util.color.ImmutableColor

class Style {
    val color: ImmutableColor
    val bold: Boolean
    val italic: Boolean
    val underline: Boolean
    val strikethrough: Boolean
    val obfuscated: Boolean
    val obfuscated_numbers: Boolean

    constructor(
        color: IColor?,
        bold: Boolean,
        italic: Boolean,
        underline: Boolean,
        strikethrough: Boolean,
        obfuscated: Boolean,
        obfuscated_numbers: Boolean
    ) {
        this.color = ImmutableColor(color!!)
        this.bold = bold
        this.italic = italic
        this.underline = underline
        this.strikethrough = strikethrough
        this.obfuscated = obfuscated
        this.obfuscated_numbers = obfuscated_numbers
    }

    constructor(color: IColor?, vararg style: Boolean) {
        this.color = ImmutableColor(color!!)
        bold = style.size > 0 && style[0]
        italic = style.size > 1 && style[1]
        underline = style.size > 2 && style[2]
        strikethrough = style.size > 3 && style[3]
        obfuscated = style.size > 4 && style[4]
        obfuscated_numbers = style.size > 5 && style[5]
    }

    companion object {
        val DEFAULT = Style(TextFormatting.WHITE.color())
    }
}
