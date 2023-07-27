package pize.graphics.util.batch.scissor

import pize.graphics.gl.Gl
import java.awt.Rectangle

class ScissorNode(val index: Int, val rectangle: Rectangle, val scissorOfIndex: Int) {

    constructor(index: Int, x: Int, y: Int, width: Int, height: Int, scissorOfIndex: Int) : this(
        index,
        Rectangle(x, y, width, height),
        scissorOfIndex
    )

    fun activate() {
        Gl.scissor(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
    }

    val x: Int
        get() = rectangle.x
    val y: Int
        get() = rectangle.y
    val x2: Int
        get() = rectangle.x + rectangle.width
    val y2: Int
        get() = rectangle.y + rectangle.height
    val width: Int
        get() = rectangle.width
    val height: Int
        get() = rectangle.height
}
