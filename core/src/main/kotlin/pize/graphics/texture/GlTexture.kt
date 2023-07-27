package pize.graphics.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import pize.graphics.gl.Format
import pize.graphics.gl.GlObject
import pize.graphics.gl.SizedFormat
import pize.graphics.gl.Type

abstract class GlTexture @JvmOverloads constructor(
    protected val TEXTURE_TYPE: Int,
    @JvmField val parameters: TextureParameters = TextureParameters()
) : GlObject(GL11.glGenTextures()) {
    val type: Type?
        get() = parameters.type
    val format: Format?
        get() = parameters.sizedFormat.base
    val sizedFormat: SizedFormat?
        get() = parameters.sizedFormat

    protected fun genMipMap() {
        GL30.glGenerateMipmap(TEXTURE_TYPE)
    }

    fun bind() {
        GL11.glBindTexture(TEXTURE_TYPE, ID)
    }

    fun bind(num: Int) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + num)
        bind()
    }

    override fun dispose() {
        GL11.glDeleteTextures(ID)
    }
}
