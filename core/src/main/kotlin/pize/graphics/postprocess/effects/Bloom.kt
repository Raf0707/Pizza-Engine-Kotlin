package pize.graphics.postprocess.effects

import pize.Pize
import pize.files.Resource
import pize.graphics.gl.Gl
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.postprocess.PostProcessEffect
import pize.graphics.postprocess.RenderBufferObject
import pize.graphics.util.ScreenQuad
import pize.graphics.util.Shader

class Bloom(private var brightness: Float, private var radius: Float) : PostProcessEffect {
    private var bloom = 1f
    private var exposure = 2f
    private var gamma = 0.6f
    private val colorBuffer: FrameBufferObject
    private val fbo2: FrameBufferObject
    private val blurBuffer: FrameBufferObject
    private val depthBuffer: RenderBufferObject
    private val brightShader: Shader
    private val blurShader: Shader
    private val combineShader: Shader

    init {
        val width = Pize?.width!!
        val height = Pize?.height!!

        // Frame Buffer 1 & Render Buffer
        colorBuffer = FrameBufferObject(width, height)
        colorBuffer.create()
        colorBuffer.bind()
        depthBuffer = RenderBufferObject(width, height)
        depthBuffer.create()
        colorBuffer.unbind()

        // Frame Buffers 2 & 3
        fbo2 = FrameBufferObject(width, height)
        fbo2.create()
        blurBuffer = FrameBufferObject(width, height)
        blurBuffer.create()

        // Shader
        val vertexShader = Resource("shader/bloom/bloom.vert")
        brightShader = Shader(vertexShader, Resource("shader/bloom/bloom_bright.frag"))
        blurShader = Shader(vertexShader, Resource("shader/bloom/bloom_blur.frag"))
        combineShader = Shader(vertexShader, Resource("shader/bloom/bloom_combine.frag"))
    }

    fun setBrightness(brightness: Float) {
        this.brightness = brightness
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun setBloom(bloom: Float) {
        this.bloom = bloom
    }

    fun setExposure(exposure: Float) {
        this.exposure = exposure
    }

    fun setGamma(gamma: Float) {
        this.gamma = gamma
    }

    override fun begin() {
        // Draw Scene In FBO1
        depthBuffer.bind()
        colorBuffer.bind()
        Gl.clearColorDepthBuffers()
    }

    override fun end() {
        depthBuffer.unbind()
        colorBuffer.unbind()
        fbo2.bind()
        Gl.clearColorBuffer()
        run {
            brightShader.bind()
            brightShader.setUniform("u_frame", colorBuffer.texture)
            brightShader.setUniform("u_brightness", brightness)
            ScreenQuad.Companion.render()
        }
        fbo2.unbind()
        blurBuffer.bind()
        Gl.clearColorBuffer()
        run {
            blurShader.bind()
            blurShader.setUniform("u_frame", fbo2.texture)
            blurShader.setUniform("u_radius", radius)
            ScreenQuad.Companion.render()
        }
        blurBuffer.unbind()
        combineShader.bind()
        combineShader.setUniform("u_frame1", colorBuffer.texture)
        combineShader.setUniform("u_frame2", blurBuffer.texture)
        combineShader.setUniform("u_bloom", bloom)
        combineShader.setUniform("u_exposure", exposure)
        combineShader.setUniform("u_gamma", gamma)
        ScreenQuad.Companion.render()
    }

    override fun end(target: PostProcessEffect) {}
    override fun resize(width: Int, height: Int) {
        depthBuffer.resize(width, height)
        colorBuffer.resize(width, height)
        fbo2.resize(width, height)
        blurBuffer.resize(width, height)
    }

    override fun dispose() {
        depthBuffer.dispose()
        colorBuffer.dispose()
        fbo2.dispose()
        blurBuffer.dispose()
        brightShader.dispose()
        blurShader.dispose()
        combineShader.dispose()
    }
}
