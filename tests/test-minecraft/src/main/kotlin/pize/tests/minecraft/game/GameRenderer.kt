package pize.tests.minecraft.game

import pize.graphics.util.batch.TextureBatch

class GameRenderer(private val session: Session) : Renderer {
    private val batch: TextureBatch

    init {
        batch = TextureBatch()
    }

    override fun render() {
        batch.begin()
        session.screenManager.render(batch)
        session.ingameGui.render(batch)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun dispose() {
        batch.dispose()
    }
}
