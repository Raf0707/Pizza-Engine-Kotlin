package pize.tests.minecraft.game.gui.screen

import pize.app.Disposable
import pize.app.Resizable
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.DirtBackground
import pize.tests.minecraft.game.gui.text.Component

abstract class Screen(val session: Session) : Resizable, Disposable {
    private val options_background: DirtBackground

    init {
        options_background = DirtBackground(session)
    }

    fun update(batch: TextureBatch) {
        if (shouldCloseOnEsc() && Key.ESCAPE.isDown) close()
        if (renderDirtBackground()) {
            options_background.render(batch)
        }
    }

    fun toScreen(id: String?) {
        session.screenManager.setCurrentScreen(id)
    }

    abstract fun render(batch: TextureBatch)
    abstract fun onShow()
    abstract fun shouldCloseOnEsc(): Boolean
    abstract fun renderDirtBackground(): Boolean
    abstract fun close()
    fun boolToText(bool: Boolean): Component? {
        return Component().translation(if (bool) "text.on" else "text.off")
    }

    companion object {
        const val BUTTON_HEIGHT = 0.055f
        const val TEXT_SCALING = 8 / 20f
        const val TEXT_HEIGHT = BUTTON_HEIGHT * TEXT_SCALING
    }
}
