package pize.graphics.texture.atlas

import pize.graphics.texture.Pixmap
import java.awt.Rectangle

class TextureAtlasNode(x: Int, y: Int, width: Int, height: Int) {
    private var imagePixmap: Pixmap? = null
    private val rectangle: Rectangle
    private var child1: TextureAtlasNode? = null
    private var child2: TextureAtlasNode? = null

    init {
        rectangle = Rectangle(x, y, width, height)
    }

    fun hasImage(): Boolean {
        return imagePixmap != null
    }

    val x: Int
        get() = rectangle.x
    val y: Int
        get() = rectangle.y
    val width: Int
        get() = rectangle.width
    val height: Int
        get() = rectangle.height

    fun insert(
        imagePixmap: Pixmap,
        paddingLeft: Int,
        paddingTop: Int,
        paddingRight: Int,
        paddingBottom: Int
    ): TextureAtlasNode? {
        val x = x
        val y = y
        val width = width
        val height = height
        val imageWidth = imagePixmap.width + paddingRight
        val imageHeight = imagePixmap.height + paddingBottom
        return if (child1 == null && child2 == null) { // Is leaf
            if (hasImage()) // Occupied
                return null
            val diffWidth = width - imageWidth
            val diffHeight = height - imageHeight
            if (imageWidth > width || imageHeight > height) // Does not fit
                return null
            if (imageWidth == width && imageHeight == height) { // Perfect fit
                this.imagePixmap = imagePixmap
                return this
            }
            if (diffWidth > diffHeight) { // X
                child1 = TextureAtlasNode(
                    x,
                    y,
                    imageWidth,
                    height
                )
                child2 = TextureAtlasNode(
                    x + imageWidth + paddingLeft,
                    y,
                    width - imageWidth - paddingLeft,
                    height
                )
            } else { // Y
                child1 = TextureAtlasNode(
                    x,
                    y,
                    width,
                    imageHeight
                )
                child2 = TextureAtlasNode(
                    x,
                    y + imageHeight + paddingTop,
                    width,
                    height - imageHeight - paddingTop
                )
            }
            child1!!.insert(imagePixmap, paddingLeft, paddingTop, paddingRight, paddingBottom)
        } else {
            val newNode = child1!!.insert(imagePixmap, paddingLeft, paddingTop, paddingRight, paddingBottom)
            newNode
                ?: child2!!.insert(
                    imagePixmap,
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    paddingBottom
                )
        }
    }
}
