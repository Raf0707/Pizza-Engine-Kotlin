package pize.tests.minecraft.game.gui.text.formatting

import pize.graphics.util.color.ImmutableColor

enum class TextFormatting(val code: Char, val formatting: ITextFormatting?) {
    BLACK('0', ColorFormatting(0, 0, 0)),
    DARK_BLUE('1', ColorFormatting(0, 0, 170)),
    DARK_GREEN('2', ColorFormatting(0, 170, 0)),
    DARK_AQUA('3', ColorFormatting(0, 170, 170)),
    DARK_RED('4', ColorFormatting(170, 0, 0)),
    DARK_PURPLE('5', ColorFormatting(170, 0, 170)),
    GOLD('6', ColorFormatting(255, 170, 0)),
    GRAY('7', ColorFormatting(170, 170, 170)),
    DARK_GRAY('8', ColorFormatting(85, 85, 85)),
    BLUE('9', ColorFormatting(85, 85, 255)),
    GREEN('a', ColorFormatting(85, 255, 85)),
    AQUA('b', ColorFormatting(85, 255, 255)),
    RED('c', ColorFormatting(255, 85, 85)),
    LIGHT_PURPLE('d', ColorFormatting(255, 85, 255)),
    YELLOW('e', ColorFormatting(255, 255, 85)),
    WHITE('f', ColorFormatting(255, 255, 255)),

    //                  BISON XDDD
    BOLD('b', StyleFormatting.BOLD),
    ITALIC('i', StyleFormatting.ITALIC),
    UNDERLINE('u', StyleFormatting.UNDERLINE),
    STRIKETHROUGH('s', StyleFormatting.STRIKETHROUGH),
    OBFUSCATED('o', StyleFormatting.OBFUSCATED),
    OBFUSCATED_NUMBERS('n', StyleFormatting.OBFUSCATED_NUMBERS),
    RESET('r', null);

    fun color(): ImmutableColor? {
        return (formatting as ColorFormatting?).getColor()
    }

    fun style(): StyleFormatting? {
        return formatting as StyleFormatting?
    }

    val isStyle: Boolean
        get() = formatting!!.javaClass == StyleFormatting::class.java
    val isColor: Boolean
        get() = formatting!!.javaClass == ColorFormatting::class.java

    companion object {
        const val FORMATTING_SYMBOL = '§'
        fun fromCode(code: Char): TextFormatting? {
            for (textFormatting in entries) if (textFormatting.code == code) return textFormatting
            return null
        }
    }
}
