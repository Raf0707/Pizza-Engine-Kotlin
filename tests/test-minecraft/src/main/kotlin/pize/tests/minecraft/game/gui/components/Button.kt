package pize.tests.minecraft.game.gui.components

import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.components.ExpandType
import pize.gui.components.NinePatchImage
import pize.gui.components.RegionMesh
import pize.gui.constraint.Constraint.Companion.matchParent
import pize.gui.constraint.Constraint.Companion.relative
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.audio.Sound
import pize.tests.minecraft.game.gui.screen.Screen
import pize.tests.minecraft.game.gui.text.Component

class Button(private val session: Session, text: Component?) : MComponent() {
    private val texture: TextureRegion?
    private val hoverTexture: TextureRegion?
    private val blockedTexture: TextureRegion?
    private val background: NinePatchImage
    private val textView: TextView
    private var listener: Runnable? = null
    var isBlocked = false
        private set

    init {
        texture = session.resourceManager.getTexture("button")
        hoverTexture = session.resourceManager.getTexture("button_hover")
        blockedTexture = session.resourceManager.getTexture("button_blocked")
        background = NinePatchImage(texture, RegionMesh(0f, 0f, 2f, 2f, 198f, 17f, 200f, 20f))
        background.setSize(matchParent(), matchParent())
        background.expandType = ExpandType.HORIZONTAL
        super.setAsParentFor(background)
        textView = TextView(session, text)
        textView.setSize(relative((Screen.Companion.TEXT_HEIGHT / Screen.Companion.BUTTON_HEIGHT).toDouble()))
        textView.isScissor = true
        super.setAsParentFor(textView)
        super.alignItems(Align.CENTER)
    }

    fun setClickListener(listener: Runnable?) {
        this.listener = listener
    }

    fun block(flag: Boolean) {
        isBlocked = flag
    }

    var text: Component?
        get() = textView.text
        public set(component) {
            textView.text = component
        }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        if (super.isTouchDown) {
            session.audioManager.play(Sound.CLICK, 1f, 1f)
            if (listener != null) listener!!.run()
        }
        if (isBlocked) background.setTexture(blockedTexture!!) else if (isHover) background.setTexture(
            hoverTexture!!
        ) else background.setTexture(
            texture!!
        )
        background.render(batch)
        textView.render(batch)
    }

    override var isHover: Boolean
        public get() = super.isHover && !isBlocked
        set(isHover) {
            super.isHover = isHover
        }
}
