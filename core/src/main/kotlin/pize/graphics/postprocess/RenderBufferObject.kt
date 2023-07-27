package pize.graphics.postprocess

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import pize.Pize
import pize.app.Resizable
import pize.graphics.gl.*
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureParameters

class RenderBufferObject @JvmOverloads constructor(
    var width: Int = Pize?.width!!,
    var height: Int = Pize?.height!!
) : GlObject(GL30.glGenRenderbuffers()), Resizable {
    private var attachment: Attachment
    val texture: Texture

    init {
        attachment = Attachment.DEPTH_ATTACHMENT
        texture = Texture(width, height)
        texture.parameters
            .setSizedFormat(SizedFormat.DEPTH_COMPONENT32)
            .setType(Type.FLOAT)
            .setWrap(Wrap.CLAMP_TO_EDGE)
            .setFilter(Filter.NEAREST)
            .setMipmapLevels(0)
    }

    fun setAttachment(attachment: Attachment) {
        this.attachment = attachment
    }

    val info: TextureParameters?
        get() = texture.parameters

    fun create() {
        texture.update()
        bind()
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.GL, GL11.GL_TEXTURE_2D, texture.ID, 0)
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, texture.sizedFormat?.GL!!, width, height)
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment.GL, GL30.GL_RENDERBUFFER, ID)
        unbind()
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        texture.resize(width, height)
        bind()
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, texture.sizedFormat?.GL!!, width, height)
        unbind()
    }

    fun bind() {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, ID)
    }

    override fun dispose() {
        GL30.glDeleteRenderbuffers(ID)
        texture.dispose()
    }

    fun unbind() {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0)
    }
}
