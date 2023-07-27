package pize.tests.minecraft.game.gui.screen.screens

import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.screen.Screen
import pize.tests.minecraft.game.options.Options

abstract class IOptionsScreen(session: Session) : Screen(session) {
    override fun shouldCloseOnEsc(): Boolean {
        return true
    }

    override fun renderDirtBackground(): Boolean {
        return true
    }

    override fun dispose() {}
    fun saveOptions() {
        session.options.save()
    }

    val options: Options?
        get() = session.options
}
