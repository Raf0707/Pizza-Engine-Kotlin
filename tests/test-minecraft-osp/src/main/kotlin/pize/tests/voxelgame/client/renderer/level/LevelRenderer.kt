package pize.tests.voxelgame.client.renderer.level

import pize.Pize.height
import pize.Pize.width
import pize.app.Disposable
import pize.app.Resizable
import pize.files.Resource
import pize.graphics.gl.Gl.disable
import pize.graphics.gl.Gl.enable
import pize.graphics.gl.Gl.lineWidth
import pize.graphics.gl.Target
import pize.graphics.texture.Texture
import pize.graphics.util.Framebuffer2D
import pize.graphics.util.Framebuffer3D
import pize.graphics.util.ScreenQuad
import pize.graphics.util.Shader
import pize.graphics.util.batch.TextureBatch
import pize.graphics.util.color.Color
import pize.tests.voxelgame.client.renderer.GameRenderer
import pize.tests.voxelgame.client.renderer.particle.ParticleBatch

class LevelRenderer(val gameRenderer: GameRenderer) : Disposable, Resizable {

    // Renderers
    val skyRenderer: SkyRenderer
    val chunkRenderer: ChunkRenderer
    val chunkBorderRenderer: ChunkBorderRenderer
    val blockSelectorRenderer: BlockSelectorRenderer
    val entityRenderer: EntityRenderer
    val playerRenderer: PlayerRenderer
    val vignetteRenderer: VignetteRenderer
    val particleBatch: ParticleBatch

    // Postprocessing
    private val batch: TextureBatch
    private val batchFramebuffer: Framebuffer2D
    private val cursorTexture: Texture
    private val postShader: Shader
    private val postFramebuffer: Framebuffer3D
    val screenColor: Color

    init {

        // Renderers
        skyRenderer = SkyRenderer(this) // Sky
        chunkRenderer = ChunkRenderer(this) // Chunks
        chunkBorderRenderer = ChunkBorderRenderer(this) // Chunk Border (F3 + G)
        blockSelectorRenderer = BlockSelectorRenderer(this) // Block Selector
        entityRenderer = EntityRenderer(this) // Entities
        playerRenderer = PlayerRenderer(this) // Player
        vignetteRenderer = VignetteRenderer(this) // Vignette
        particleBatch = ParticleBatch(64) // Particles

        // Postprocessing
        batch = TextureBatch()
        batchFramebuffer = Framebuffer2D()
        cursorTexture = Texture("texture/cursor.png")
        postShader = Shader(Resource("shader/post.vert"), Resource("shader/post.frag"))
        postFramebuffer = Framebuffer3D()
        screenColor = Color()

        // For ChunkBorder and BlockSelector line rendering
        lineWidth(2f)
    }

    fun render() {
        // Get camera
        val camera = gameRenderer.session.game.camera ?: return
        if (camera.isInWater) screenColor[0.4, 0.6] = 1.0 else screenColor.reset()
        // Render world
        postFramebuffer.begin()
        run {
            enable(Target.DEPTH_TEST)
            skyRenderer.render(camera) // Sky
            chunkRenderer.render(camera) // Chunks
            playerRenderer.render(camera) // Player
            entityRenderer.render(camera) // Entities
            blockSelectorRenderer.render(camera) // Block selector
            chunkBorderRenderer.render(camera) // Chunk border
            particleBatch.render(camera) // Particles
            disable(Target.DEPTH_TEST)
        }
        postFramebuffer.end()

        // Render cursor
        batchFramebuffer.begin()
        run {
            batch.begin()
            val cursorSize = height / 48f
            batch.draw(cursorTexture, width / 2f - cursorSize / 2, height / 2f - cursorSize / 2, cursorSize, cursorSize)
            batch.end()
        }
        batchFramebuffer.end()
        postShader.bind()
        postShader.setUniform("u_frame", postFramebuffer.frameTexture)
        postShader.setUniform("u_batch", batchFramebuffer.frameTexture)
        postShader.setUniform("u_color", screenColor)
        ScreenQuad.render()

        // Render Vignette
        vignetteRenderer.render(batch)
    }

    override fun resize(width: Int, height: Int) {
        // Postprocessing
        postFramebuffer.resize(width, height)
        batchFramebuffer.resize(width, height)
    }

    override fun dispose() {
        // Renderers
        skyRenderer.dispose()
        chunkRenderer.dispose()
        chunkBorderRenderer.dispose()
        blockSelectorRenderer.dispose()
        entityRenderer.dispose()
        playerRenderer.dispose()
        vignetteRenderer.dispose()
        particleBatch.dispose()

        // Postprocessing
        batch.dispose()
        cursorTexture.dispose()
        postFramebuffer.dispose()
        batchFramebuffer.dispose()
    }
}
