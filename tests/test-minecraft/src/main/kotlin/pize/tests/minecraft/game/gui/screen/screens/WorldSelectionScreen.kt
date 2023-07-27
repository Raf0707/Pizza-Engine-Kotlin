package pize.tests.minecraft.game.gui.screen.screens

import pize.graphics.util.batch.TextureBatch
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.screen.Screen

class WorldSelectionScreen(session: Session) : Screen(session) {
    override fun render(batch: TextureBatch) {}
    override fun resize(width: Int, height: Int) {}
    override fun onShow() {}
    override fun shouldCloseOnEsc(): Boolean {
        return true
    }

    override fun close() {
        toScreen("main_menu")
    }

    override fun dispose() {}
    override fun renderDirtBackground(): Boolean {
        return true
    }
}