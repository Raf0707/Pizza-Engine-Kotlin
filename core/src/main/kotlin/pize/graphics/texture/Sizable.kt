package pize.graphics.texture

abstract class Sizable {
    @JvmField
    var width = 0
    @JvmField
    var height = 0

    constructor(width: Int, height: Int) {
        setSize(width, height)
    }

    constructor(sizable: Sizable) {
        setSize(sizable)
    }

    protected fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    protected fun setSize(sizable: Sizable) {
        width = sizable.width
        height = sizable.height
    }

    fun aspect(): Float {
        return width.toFloat() / height
    }

    fun match(sizable: Sizable): Boolean {
        return sizable.width == width && sizable.height == height
    }

    fun match(width: Int, height: Int): Boolean {
        return this.width == width && this.height == height
    }
}
