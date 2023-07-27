package pize.tests.minecraft.game.gui.screen

import pize.app.Disposable
import pize.app.Resizable
import pize.graphics.util.batch.TextureBatch

class ScreenManager : Resizable, Disposable {
    private val screens: MutableMap<String?, Screen>
    private var current: String? = null

    init {
        screens = HashMap()
    }

    fun setCurrentScreen(id: String?) {
        if (screens.containsKey(id)) {
            current = id
            screens[id]!!.onShow()
        } else current = null
    }

    fun putScreen(id: String?, screen: Screen) {
        screens[id] = screen
    }

    fun putGui(id: String?, screen: Screen) {
        screens[id] = screen
    }

    fun render(batch: TextureBatch) {
        val screen = screens[current]
        if (screen != null) {
            screen.update(batch)
            screen.render(batch)
        }
    }

    override fun resize(width: Int, height: Int) {
        for (screen in screens.values) screen.resize(width, height)
    }

    override fun dispose() {
        for (screen in screens.values) screen.dispose()
    }
}
