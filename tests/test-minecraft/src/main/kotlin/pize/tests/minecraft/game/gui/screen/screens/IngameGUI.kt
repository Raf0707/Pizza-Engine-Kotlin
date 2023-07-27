package pize.tests.minecraft.game.gui.screen.screens

import pize.Pize.fPS
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.UIComponent
import pize.gui.components.Layout
import pize.gui.constraint.Constraint.Companion.relativeToHeight
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.components.TextView
import pize.tests.minecraft.game.gui.screen.Screen
import pize.tests.minecraft.game.gui.text.Component
import pize.tests.minecraft.game.gui.text.TextComponent

class IngameGUI(session: Session) : Screen(session) {
    private val layout: Layout

    init {

        // Main Layout
        layout = Layout()
        layout.setLayoutType(LayoutType.VERTICAL)
        layout.alignItems(Align.LEFT_UP)

        // <FPS>
        val fps = TextView(session, Component().formattedText(fPS.toString() + " FPS"))
        fps.setPosition(relativeToHeight(0.005))
        fps.disableShadow(true)
        fps.show(session.options.isShowFps)
        layout.put("fps", fps)
    }

    override fun render(batch: TextureBatch) {
        ((layout.get<UIComponent<TextureBatch>>("fps") as TextView).text
            .getComponent(0) as TextComponent).text = fPS.toString() + " FPS"
        layout.render(batch)
    }

    override fun resize(width: Int, height: Int) {}
    fun showFps(showFps: Boolean) {
        layout.get<UIComponent<TextureBatch>>("fps").show(showFps)
    }

    override fun onShow() {}
    override fun close() {}
    override fun dispose() {}
    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun renderDirtBackground(): Boolean {
        return false
    }
}
