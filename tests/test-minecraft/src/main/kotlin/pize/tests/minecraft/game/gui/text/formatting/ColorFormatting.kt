package pize.tests.minecraft.game.gui.text.formatting

import pize.graphics.util.color.ImmutableColor

class ColorFormatting(r: Int, g: Int, b: Int) : ITextFormatting {
    val color: ImmutableColor

    init {
        color = ImmutableColor(r, g, b, 255)
    }
}
