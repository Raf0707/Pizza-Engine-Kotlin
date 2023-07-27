package pize.graphics.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.awt.image.BufferedImage

class TextureArray : GlTexture {
    private val pixmapList: MutableList<Pixmap?>

    constructor(vararg textureData: Pixmap) : super(GL30.GL_TEXTURE_2D_ARRAY) {
        bind()
        pixmapList = ArrayList()
        for (z in textureData.indices) {
            val td = textureData[z]
            pixmapList.add(td)
            parameters.texSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, td.buffer, td.getWidth(), td.getHeight(), z)
            parameters.use(TEXTURE_TYPE)
            genMipMap()
        }
        unbind()
    }

    constructor(vararg bufferedImage: BufferedImage?) : super(GL30.GL_TEXTURE_2D_ARRAY) {
        bind()
        pixmapList = ArrayList()
        for (z in bufferedImage.indices) {
            val td: Pixmap = load(bufferedImage[z])
            pixmapList.add(td)
            parameters.texSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, td.buffer, td.getWidth(), td.getHeight(), z)
            parameters.use(TEXTURE_TYPE)
            genMipMap()
        }
        unbind()
    }

    constructor(vararg texture: Texture) : super(GL30.GL_TEXTURE_2D_ARRAY) {
        bind()
        pixmapList = ArrayList()
        for (z in texture.indices) {
            val td = texture[z].pixmap.copy()
            pixmapList.add(td)
            parameters.texSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, td.buffer, td.getWidth(), td.getHeight(), z)
            parameters.use(TEXTURE_TYPE)
            genMipMap()
        }
        unbind()
    }

    constructor(vararg file: String?) : this(false, *file)
    constructor(invY: Boolean, vararg file: String) : super(GL30.GL_TEXTURE_2D_ARRAY) {
        bind()
        pixmapList = ArrayList()
        for (z in file.indices) {
            val f = file[z]
            val td = PixmapIO.load(f, false, invY)
            pixmapList.add(td)
            parameters.texSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, td.buffer, td.getWidth(), td.getHeight(), z)
        }
        parameters.use(TEXTURE_TYPE)
        genMipMap()
    }

    fun getPixmapList(): List<Pixmap?> {
        return pixmapList
    }

    val width: Int
        get() = if (pixmapList.size > 0) pixmapList[0].getWidth() else -1
    val height: Int
        get() = if (pixmapList.size > 0) pixmapList[0].getHeight() else -1

    companion object {
        fun unbind() {
            GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0)
        }
    }
}
