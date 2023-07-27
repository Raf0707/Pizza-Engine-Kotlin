package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.gl.DepthFunc
import pize.graphics.gl.Gl.depthFunc
import pize.graphics.gl.Gl.disable
import pize.graphics.gl.Gl.enable
import pize.graphics.gl.Gl.polygonOffset
import pize.graphics.gl.Target
import pize.graphics.util.Shader
import pize.tests.voxelgame.client.chunk.ClientChunk
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.main.chunk.ChunkUtils

class ChunkRenderer(private val levelRenderer: LevelRenderer) : Disposable {
    private val packedChunkShader: Shader
    private val customShader: Shader
    var renderedChunks = 0
        private set

    init {
        depthFunc(DepthFunc.LEQUAL)
        packedChunkShader = Shader(
            Resource("shader/level/chunk/solid-blocks-packed.vert"),
            Resource("shader/level/chunk/solid-blocks-packed.frag")
        )
        customShader =
            Shader(Resource("shader/level/chunk/custom-blocks.vert"), Resource("shader/level/chunk/custom-blocks.frag"))
        polygonOffset(1f, 1f) // For BlockSelector line rendering
    }

    fun render(camera: GameCamera) {
        setupShaders(camera)
        enable(Target.POLYGON_OFFSET_FILL)
        renderMeshes(camera)
        disable(Target.POLYGON_OFFSET_FILL)
    }

    private fun renderMeshes(camera: GameCamera) {
        // Level
        val level = levelRenderer.gameRenderer.session.game.level ?: return

        // Chunks
        val chunks: Collection<ClientChunk?> = level.chunkManager.allChunks // Get all chunks
            .stream().filter { chunk: ClientChunk? -> camera.isChunkSeen(chunk) }.toList() // Frustum culling

        // Rendered chunks for the info panel
        renderedChunks = chunks.size

        // Atlas
        val blockAtlas = levelRenderer.gameRenderer.session.resources.blocks


        // Update translation matrix
        for (chunk in chunks) chunk!!.updateTranslationMatrix(camera)

        // Render custom blocks
        disable(Target.CULL_FACE)
        customShader.bind()
        for (chunk in chunks) {
            customShader.setUniform("u_model", chunk.getTranslationMatrix())
            chunk.getMeshStack().custom.render()
        }
        enable(Target.CULL_FACE)

        // Render solid blocks
        packedChunkShader.bind()
        for (chunk in chunks) {
            packedChunkShader.setUniform("u_model", chunk.getTranslationMatrix())
            chunk.getMeshStack().packed.render()
        }

        // Render translucent blocks
        disable(Target.CULL_FACE)
        for (chunk in chunks) {
            packedChunkShader.setUniform("u_model", chunk.getTranslationMatrix())
            chunk.getMeshStack().translucent.render()
        }
        enable(Target.CULL_FACE)
    }

    private fun setupShaders(camera: GameCamera) {
        val options = levelRenderer.gameRenderer.session.options
        val fogColor = levelRenderer.skyRenderer.getFogColor()
        val fogStart = levelRenderer.skyRenderer.fogStart
        val blockAtlas = levelRenderer.gameRenderer.session.resources.blocks
        val gameTime = levelRenderer.gameRenderer.session.game.time

        // Solid
        packedChunkShader.bind()
        packedChunkShader.setUniform("u_projection", camera.projection)
        packedChunkShader.setUniform("u_view", camera.view)
        packedChunkShader.setUniform("u_atlas", blockAtlas)
        packedChunkShader.setUniform("u_renderDistanceBlocks", (options!!.renderDistance - 1) * ChunkUtils.SIZE)
        packedChunkShader.setUniform("u_fogColor", fogColor!!)
        packedChunkShader.setUniform("u_fogStart", fogStart)
        packedChunkShader.setUniform("u_brightness", options.brightness)
        packedChunkShader.setUniform("u_gameTime", gameTime.ticks.toFloat())

        // Custom
        customShader.bind()
        customShader.setUniform("u_projection", camera.projection)
        customShader.setUniform("u_view", camera.view)
        customShader.setUniform("u_atlas", blockAtlas)
        customShader.setUniform("u_renderDistanceBlocks", (options.renderDistance - 1) * ChunkUtils.SIZE)
        customShader.setUniform("u_fogColor", fogColor!!)
        customShader.setUniform("u_fogStart", fogStart)
        customShader.setUniform("u_brightness", options.brightness)
    }

    override fun dispose() {
        packedChunkShader.dispose()
        customShader.dispose()
    }
}
