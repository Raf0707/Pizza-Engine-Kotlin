package pize.graphics.postprocess

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import pize.Pize
import pize.app.Resizable
import pize.graphics.gl.*
import pize.graphics.texture.*
import pize.graphics.util.ScreenQuad
import pize.graphics.util.ScreenQuadShader
import java.nio.*

class FrameBufferObject @JvmOverloads constructor(
    var width: Int = Pize.getWidth(),
    var height: Int = Pize.getHeight()
) : GlObject(GL30.glGenFramebuffers()), Resizable {
    private var attachment: Attachment
    val texture: Texture
    private var draw = true
    private var read = true

    init {
        attachment = Attachment.COLOR_ATTACHMENT0
        texture = Texture(width, height)
        texture.parameters
            .setWrap(Wrap.CLAMP_TO_EDGE)
            .setFilter(Filter.NEAREST)
            .setMipmapLevels(0)
    }

    fun setAttachment(attachment: Attachment) {
        this.attachment = attachment
    }

    fun setRead(read: Boolean) {
        this.read = read
    }

    fun setWrite(draw: Boolean) {
        this.draw = draw
    }

    val info: TextureParameters?
        get() = texture.parameters

    fun create() {
        texture.update()
        bind()
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.GL, GL11.GL_TEXTURE_2D, texture.id, 0)
        GL11.glDrawBuffer(if (draw) attachment.GL else GL11.GL_NONE)
        GL11.glReadBuffer(if (read) attachment.GL else GL11.GL_NONE)
        unbind()
    }

    override fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        texture.resize(width, height)
    }

    fun copyTo(texture: Texture) {
        bind()
        texture.bind()
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height)
        unbind()
    }

    fun getBuffer(texture: Texture): ByteBuffer {
        bind()
        val width = texture.width
        val height = texture.height
        val buffer = BufferUtils.createByteBuffer(width * height * texture.format.channels)
        GL11.glReadPixels(0, 0, width, height, texture.format.GL, texture.type.GL, buffer)
        unbind()
        return buffer
    }

    fun renderToScreen() {
        unbind()
        ScreenQuadShader.Companion.use(texture)
        ScreenQuad.Companion.render()
    }

    fun bind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ID)
    }

    override fun dispose() {
        GL30.glDeleteFramebuffers(ID)
        texture.dispose()
    }

    fun unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    }
}
