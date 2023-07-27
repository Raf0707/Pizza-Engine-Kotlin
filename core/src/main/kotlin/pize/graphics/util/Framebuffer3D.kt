package pize.graphics.util

import pize.app.Disposable
import pize.app.Resizable
import pize.graphics.gl.Gl
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.postprocess.RenderBufferObject
import pize.graphics.texture.Texture

class Framebuffer3D : Resizable, Disposable {
    private val fbo: FrameBufferObject
    private val rbo: RenderBufferObject

    init {
        fbo = FrameBufferObject()
        fbo.create()
        fbo.bind()
        rbo = RenderBufferObject()
        rbo.create()
        fbo.unbind()
    }

    fun begin() {
        fbo.bind()
        rbo.bind()
        Gl.clearColorDepthBuffers()
    }

    fun end() {
        fbo.unbind()
        rbo.unbind()
    }

    val frameTexture: Texture?
        get() = fbo.texture
    val depthMap: Texture?
        get() = rbo.texture

    override fun resize(width: Int, height: Int) {
        fbo.resize(width, height)
        rbo.resize(width, height)
    }

    override fun dispose() {
        fbo.dispose()
        rbo.dispose()
    }
}
