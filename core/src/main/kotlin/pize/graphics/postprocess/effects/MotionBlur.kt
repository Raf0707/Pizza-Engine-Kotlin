package pize.graphics.postprocess.effects

import pize.Pize
import pize.files.Resource
import pize.graphics.gl.Gl
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.postprocess.PostProcessEffect
import pize.graphics.postprocess.RenderBufferObject
import pize.graphics.texture.*
import pize.graphics.util.ScreenQuad
import pize.graphics.util.Shader

class MotionBlur : PostProcessEffect {
    private val fbo1: FrameBufferObject
    private val fbo2: FrameBufferObject
    private val rbo1: RenderBufferObject
    private val rbo2: RenderBufferObject
    private val shader: Shader
    private val backFrame: Texture

    init {
        val width = Pize?.width!!
        val height = Pize?.height!!

        // Frame & Render Buffer 1
        fbo1 = FrameBufferObject(width, height)
        fbo1.create()
        fbo1.bind()
        rbo1 = RenderBufferObject(width, height)
        rbo1.create()
        fbo1.unbind()

        // Frame & Render Buffer 2
        fbo2 = FrameBufferObject(width, height)
        fbo2.create()
        fbo2.bind()
        rbo2 = RenderBufferObject(width, height)
        rbo2.create()
        fbo2.unbind()

        // Shader (quad)
        shader = Shader(Resource("shader/motion/motion.vert"), Resource("shader/motion/motion.frag"))

        // Texture (previous frame)
        backFrame = Texture(width, height)
    }

    override fun begin() {
        // Draw Scene In Fbo1
        fbo1.bind()
        rbo1.bind()
        Gl.clearColorDepthBuffers()
    }

    override fun end() {
        fbo1.unbind()
        rbo1.unbind()

        // Draw Scene+FBO1 In FBO2
        fbo2.bind()
        rbo2.bind()
        Gl.clearColorDepthBuffers()
        run {
            shader.bind()
            shader.setUniform("u_frame", fbo1.texture)
            shader.setUniform("u_backFrame", backFrame)
            ScreenQuad.Companion.render()
        }
        fbo2.unbind()
        rbo2.unbind()

        // Copy FBO2 as backFrame
        fbo2.copyTo(backFrame)

        // Draw Scene+FBO1 On Screen
        shader.bind()
        shader.setUniform("u_frame", fbo1.texture)
        shader.setUniform("u_backFrame", backFrame)
        ScreenQuad.Companion.render()
    }

    override fun end(target: PostProcessEffect) {
        fbo1.unbind()
        rbo1.unbind()

        // Draw Scene+FBO1 In FBO2
        fbo2.bind()
        rbo2.bind()
        Gl.clearColorDepthBuffers()
        run {
            shader.bind()
            shader.setUniform("u_frame", fbo1.texture)
            shader.setUniform("u_backFrame", backFrame)
            ScreenQuad.Companion.render()
        }
        fbo2.unbind()
        rbo2.unbind()

        // Copy FBO2 as backFrame
        fbo2.copyTo(backFrame)

        // Draw Scene+FBO1 In Target
        target.begin()
        shader.bind()
        shader.setUniform("u_frame", fbo1.texture)
        shader.setUniform("u_backFrame", backFrame)
        ScreenQuad.Companion.render()
    }

    override fun resize(width: Int, height: Int) {
        fbo1.resize(width, height)
        fbo2.resize(width, height)
        rbo1.resize(width, height)
        rbo2.resize(width, height)
        backFrame.resize(width, height)
    }

    override fun dispose() {
        fbo1.dispose()
        fbo2.dispose()
        rbo1.dispose()
        rbo2.dispose()
        backFrame.dispose()
        shader.dispose()
    }
}
