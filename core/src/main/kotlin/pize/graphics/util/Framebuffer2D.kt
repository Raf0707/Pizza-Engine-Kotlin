package pize.graphics.util

import pize.app.Disposable
import pize.app.Resizable
import pize.graphics.gl.Gl
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.texture.Texture

class Framebuffer2D : Resizable, Disposable {
    // Для самых ленивых
    private val fbo: FrameBufferObject

    init {
        fbo = FrameBufferObject()
        fbo.create()
    }

    fun begin() {
        fbo.bind()
        Gl.clearColorBuffer()
    }

    fun end() {
        fbo.unbind()
    }

    val frameTexture: Texture?
        get() = fbo.texture

    override fun resize(width: Int, height: Int) {
        fbo.resize(width, height)
    }

    override fun dispose() {
        fbo.dispose()
    }
}
