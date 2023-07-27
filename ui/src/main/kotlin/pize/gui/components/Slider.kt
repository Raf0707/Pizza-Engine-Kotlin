package pize.gui.components

import pize.Pize
import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent
import pize.gui.constraint.*
import pize.math.Maths.clamp
import pize.math.Maths.round

class Slider(private val background: UIComponent<TextureBatch?>, private val handle: UIComponent<TextureBatch?>) :
    UIComponent<TextureBatch?>() {
    var value = 0f
        private set
    private var prevValue = 0f
    private var divisions = 0f
    private var drag = false

    init {
        super.setAsParentFor(background, handle)
        background.setSize(Constraint.Companion.matchParent())
        handle.setPosition(Constraint.Companion.zero(), Constraint.Companion.zero())
    }

    public override fun render(batch: TextureBatch?, x: Float, y: Float, width: Float, height: Float) {
        background.render(batch)
        val handleWidth = handle.width
        handle.xConstraint?.setValue((value * (width - handleWidth)).toDouble())
        handle.render(batch)
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
}
