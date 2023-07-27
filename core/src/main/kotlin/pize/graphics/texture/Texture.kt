package pize.graphics.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import pize.app.Resizable
import pize.files.Resource

class Texture : GlTexture, Resizable {
    var width = 0
        private set
    var height = 0
        private set
    var pixmap: Pixmap? = null
        private set

    constructor(width: Int, height: Int) : super(GL11.GL_TEXTURE_2D) {
        resize(width, height)
    }

    constructor(pixmap: Pixmap?) : super(GL11.GL_TEXTURE_2D) {
        this.pixmap = pixmap
        resize(pixmap.getWidth(), pixmap.getHeight())
    }

    constructor(filepath: String?) : this(load(filepath))
    constructor(res: Resource?) : this(load(res))
    constructor(texture: Texture) : this(texture.pixmap!!.copy())

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        update()
    }

    fun update() {
        bind()
        parameters.use(GL11.GL_TEXTURE_2D)
        parameters.texImage2D(GL11.GL_TEXTURE_2D, if (pixmap != null) pixmap.getBuffer() else null, width, height)
        genMipMap()
    }

    fun regenerateMipmapLevels(vararg mipmaps: Pixmap) {
        if (mipmaps.size == 0) return
        bind()
        parameters.setMipmapLevels(mipmaps.size)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, parameters.mipmapLevels)
        for (i in 0 until parameters.mipmapLevels) {
            val pixmap = mipmaps[i]
            parameters.texImage2D(GL11.GL_TEXTURE_2D, pixmap.buffer, pixmap.width, pixmap.height, i + 1)
        }
    }

    fun regenerateMipmapLevels(levels: Int) {
        if (parameters.mipmapLevels == levels) return
        bind()
        parameters.setMipmapLevels(levels)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, parameters.mipmapLevels)
        genMipMap()
    }

    protected fun genMipMapManual() {
        var pixmap = pixmap.getMipmapped()
        for (level in 1..parameters.mipmapLevels) {
            parameters.texImage2D(GL11.GL_TEXTURE_2D, pixmap.buffer, pixmap!!.width, pixmap!!.height, level)
            if (level != parameters.mipmapLevels) pixmap = pixmap.getMipmapped()
        }
    }

    fun setPixmap(pixmap: Pixmap): Texture {
        if (this.pixmap == null) return this
        this.pixmap!!.set(pixmap)
        width = pixmap.getWidth()
        height = pixmap.getHeight()
        update()
        return this
    }

    fun aspect(): Float {
        return width.toFloat() / height
    }

    companion object {
        fun unbind() {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
        }
    }
}