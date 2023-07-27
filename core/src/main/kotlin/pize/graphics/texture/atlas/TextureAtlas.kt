package pize.graphics.texture.atlas

import pize.files.Resource
import pize.graphics.texture.*
import pize.graphics.texture.PixmapIO.load
import kotlin.math.max

class TextureAtlas<I> {
    private val images: MutableList<Image<I>>
    private var regions: MutableMap<I, Region>? = null
    var texture: Texture? = null
        private set

    init {
        images = ArrayList()
    }

    fun generate(width: Int, height: Int, paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        // Sort images from big to small perimeter
        val atlasHalfPerimeter = width + height
        images.sortWith(Comparator.comparingInt { image: Image<I> -> atlasHalfPerimeter - image.halfPerimeter })
        val pixmap = Pixmap(width, height)
        val root = TextureAtlasNode(0, 0, width - paddingLeft, height - paddingTop)
        regions = HashMap(images.size)

        // Iterate all images to generate
        for (image in images) {
            val drawResult = root.insert(image.pixmap, paddingLeft, paddingTop, paddingRight, paddingBottom)
                ?: throw Error("Insufficient atlas area")
            val drawX = drawResult.x + paddingLeft
            val drawY = drawResult.y + paddingTop
            val drawWidth = image.pixmap.width
            val drawHeight = image.pixmap.height
            pixmap.drawPixmap(image.pixmap, drawX, drawY)
            (regions as HashMap<I, Region>)[image.identifier] = Region(
                drawX.toDouble() / width,
                drawY.toDouble() / height,
                (drawX + drawWidth).toDouble() / width,
                (drawY + drawHeight).toDouble() / height
            )
        }
        texture = Texture(pixmap)
        images.clear()
    }

    fun generate(width: Int, height: Int, paddingRight: Int, paddingBottom: Int) {
        generate(width, height, 0, 0, paddingRight, paddingBottom)
    }

    @JvmOverloads
    fun generate(width: Int, height: Int, padding: Int = 0) {
        generate(width, height, padding, padding)
    }

    fun put(identifier: I, pixmap: Pixmap) {
        images.add(Image(pixmap, identifier))
    }

    fun put(identifier: I, res: Resource?) {
        put(identifier, load(res!!))
    }

    fun put(identifier: I, path: String) {
        put(identifier, Resource(path))
    }

    fun getRegions(): Map<I, Region>? {
        return regions
    }

    fun getRegion(identifier: I): Region? {
        return regions!![identifier]
    }

    fun size(): Int {
        return max(regions!!.size.toDouble(), images.size.toDouble()).toInt()
    }

    private class Image<K>(
        val pixmap: Pixmap, // Indexing for regions
        val identifier: K
    ) {
        val halfPerimeter: Int

        init {
            halfPerimeter = pixmap.width + pixmap.height
        }
    }
}
