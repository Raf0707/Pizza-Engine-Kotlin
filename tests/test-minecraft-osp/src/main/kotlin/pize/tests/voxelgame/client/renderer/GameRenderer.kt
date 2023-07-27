package pize.tests.voxelgame.client.renderer

import pize.app.AppAdapter
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.renderer.chat.ChatRenderer
import pize.tests.voxelgame.client.renderer.infopanel.InfoPanelRenderer
import pize.tests.voxelgame.client.renderer.level.LevelRenderer
import pize.tests.voxelgame.client.renderer.text.TextComponentBatch

class GameRenderer(val session: Minecraft) : AppAdapter() {
    val textComponentBatch: TextComponentBatch
    val worldRenderer: LevelRenderer
    val infoRenderer: InfoPanelRenderer
    val chatRenderer: ChatRenderer

    init {
        textComponentBatch = TextComponentBatch()
        worldRenderer = LevelRenderer(this)
        infoRenderer = InfoPanelRenderer(this)
        chatRenderer = ChatRenderer(this)
    }

    override fun render() {
        textComponentBatch.updateScale()
        worldRenderer.render()
        infoRenderer.render()
        chatRenderer.render()
    }

    override fun resize(width: Int, height: Int) {
        worldRenderer.resize(width, height)
    }

    override fun dispose() {
        textComponentBatch.dispose()
        worldRenderer.dispose()
        infoRenderer.dispose()
        chatRenderer.dispose()
    }
}
