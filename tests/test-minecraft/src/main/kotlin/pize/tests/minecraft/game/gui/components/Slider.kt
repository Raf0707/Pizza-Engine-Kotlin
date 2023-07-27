package pize.tests.minecraft.game.gui.components

import pize.Pize
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.components.ExpandType
import pize.gui.components.NinePatchImage
import pize.gui.components.RegionMesh
import pize.gui.constraint.Constraint.Companion.aspect
import pize.gui.constraint.Constraint.Companion.matchParent
import pize.gui.constraint.Constraint.Companion.relative
import pize.gui.constraint.Constraint.Companion.zero
import pize.math.Maths.clamp
import pize.math.Maths.round
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.audio.Sound
import pize.tests.minecraft.game.gui.text.Component

open class Slider(protected val session: Session) : MComponent() {
    private val background: NinePatchImage
    private val handle: NinePatchImage
    var value = 0f
        private set
    private var prevValue = 0f
    private var divisions = 0f
    private var drag = false
    private val textView: TextView
    private val handleTexture: TextureRegion?
    private val handleHoverTexture: TextureRegion?

    init {
        handleTexture = session.resourceManager.getTexture("button")
        handleHoverTexture = session.resourceManager.getTexture("button_hover")
        val blockedTexture = session.resourceManager.getTexture("button_blocked")
        background = NinePatchImage(blockedTexture!!, RegionMesh(0f, 0f, 2f, 2f, 198f, 17f, 200f, 20f))
        super.setAsParentFor(background)
        background.setSize(matchParent())
        background.expandType = ExpandType.HORIZONTAL
        handle = NinePatchImage(handleTexture, RegionMesh(0f, 0f, 2f, 2f, 198f, 17f, 200f, 20f))
        super.setAsParentFor(handle)
        handle.setPosition(zero(), zero())
        handle.setSize(aspect((8 / 20f).toDouble()), relative(1.0))
        handle.expandType = ExpandType.HORIZONTAL
        textView = TextView(session, null)
        textView.alignSelf(Align.CENTER)
        textView.isScissor = true
        super.setAsParentFor(textView)
    }

    public override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        if (super.isTouchReleased) session.audioManager.play(Sound.CLICK, 1f, 1f)
        val handleWidth = handle.width
        handle.xConstraint!!.setValue((value * (width - handleWidth)).toDouble())
        if (isHover) handle.setTexture(handleHoverTexture!!) else handle.setTexture(handleTexture!!)
        background.render(batch)
        handle.render(batch)
        textView.render(batch)
        if (isTouchDown) drag = true else if (Pize.isTouchReleased) drag = false
        prevValue = value
        if (!drag) return
        val mouseX = Pize.x.toFloat()
        value = clamp((mouseX - x - handleWidth / 2) / (width - handleWidth), 0f, 1f)
        if (divisions > 0) value = round((value * divisions).toDouble()) / divisions
    }

    fun setValue(value: Double): Slider {
        if (value > 1 || value < 0) return this
        this.value = value.toFloat()
        return this
    }

    val isChanged: Boolean
        get() = prevValue != value

    fun setDivisions(divisions: Int): Slider {
        this.divisions = divisions.toFloat()
        return this
    }

    var text: Component?
        get() = textView.text
        public set(component) {
            textView.text = component
        }
}
