package pize.tests.voxelgame.main.text

class TextStyle {
    var data: Byte
        private set

    constructor() {
        data = 0
    }

    constructor(textStyle: TextStyle) {
        data = textStyle.data
    }

    constructor(data: Byte) {
        this.data = data
    }

    fun enable(style: StyleFormatting) {
        data = (data.toInt() or (1 shl style.styleID)).toByte()
    }

    fun reset() {
        data = 0
    }

    val isItalic: Boolean
        get() = data.toInt() and (1 shl StyleFormatting.ITALIC.styleID) == 1
    val isBold: Boolean
        get() = data.toInt() and (1 shl StyleFormatting.BOLD.styleID) == 1

    fun copy(): TextStyle {
        return TextStyle(this)
    }
}
