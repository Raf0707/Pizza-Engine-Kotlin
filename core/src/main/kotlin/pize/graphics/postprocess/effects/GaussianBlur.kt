package pize.graphics.postprocess.effects

import pize.Pize
import pize.files.Resource
import pize.graphics.gl.Gl
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.postprocess.PostProcessEffect
import pize.graphics.postprocess.RenderBufferObject
import pize.graphics.util.ScreenQuad
import pize.graphics.util.Shader

class GaussianBlur(private var radius: Float) : PostProcessEffect {
    private val fbo1: FrameBufferObject
    private val fbo2: FrameBufferObject
    private val rbo: RenderBufferObject
    private val shader: Shader

    init {
        val width = Pize.getWidth()
        val height = Pize.getHeight()

        // Frame Buffer 1 & Render Buffer
        fbo1 = FrameBufferObject(width, height)
        fbo1.create()
        fbo1.bind()
        rbo = RenderBufferObject(width, height)
        rbo.create()
        fbo1.unbind()

        // Frame Buffer 2
        fbo2 = FrameBufferObject(width, height)
        fbo2.create()

        // Shader (quad)
        shader = Shader(Resource("shader/blur/blur.vert"), Resource("shader/blur/blur.frag"))
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    override fun begin() {
        // Draw Scene In FBO1
        rbo.bind()
        fbo1.bind()
        Gl.clearColorDepthBuffers()
    }

    override fun end() {
        fbo1.unbind()
        rbo.unbind()

        // Draw FBO1+BlurX In FBO2
        fbo2.bind()
        Gl.clearColorBuffer()
        run {
            shader.bind()
            shader.setUniform("u_frame", fbo1.texture)
            shader.setUniform("u_axis", 0)
            shader.setUniform("u_radius", radius)
            ScreenQuad.Companion.render()
        }
        fbo2.unbind()

        // Draw FBO2+BlurY On Screen
        shader.bind()
        shader.setUniform("u_frame", fbo2.texture)
        shader.setUniform("u_axis", 1)
        shader.setUniform("u_radius", radius)
        ScreenQuad.Companion.render()
    }

    override fun end(target: PostProcessEffect) {
        fbo1.unbind()
        rbo.unbind()

        // Draw FBO1+BlurX In FBO2
        fbo2.bind()
        Gl.clearColorBuffer()
        run {
            shader.bind()
            shader.setUniform("u_frame", fbo1.texture)
            shader.setUniform("u_axis", 0)
            shader.setUniform("u_radius", radius)
            ScreenQuad.Companion.render()
        }
        fbo2.unbind()

        // Draw FBO2+BlurY In Target
        target.begin()
        shader.bind()
        shader.setUniform("u_frame", fbo2.texture)
        shader.setUniform("u_axis", 1)
        shader.setUniform("u_radius", radius)
        ScreenQuad.Companion.render()
    }

    override fun resize(width: Int, height: Int) {
        fbo1.resize(width, height)
        fbo2.resize(width, height)
        rbo.resize(width, height)
    }

    override fun dispose() {
        fbo1.dispose()
        fbo2.dispose()
        rbo.dispose()
        shader.dispose()
    }
}
