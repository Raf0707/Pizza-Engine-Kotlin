package pize.gui

import pize.Pize
import pize.Pize.mouse
import pize.Pize.window
import pize.gui.constraint.*
import pize.gui.constraint.RelativeConstraint.RelativeTo

abstract class UIComponent<C> @JvmOverloads constructor(
    var xConstraint: Constraint? = Constraint.Companion.zero(),
    var yConstraint: Constraint? = Constraint.Companion.zero(),
    var widthConstraint: Constraint? = Constraint.Companion.matchParent(),
    var heightConstraint: Constraint? = Constraint.Companion.matchParent()
) : Cloneable {
    var x = 0f
        protected set
    var y = 0f
        protected set
    var width = 0f
        protected set
    var height = 0f
        protected set
    var isShow = false
        private set
    open var isHover = false
        private set
    private var preGrab = false
    var isGrab = false
        private set
    private var align: Align? = null
    private var layoutType: LayoutType? = null
    private var alignComponents: Align? = null
    private var shiftX = 0f
    private var shiftY = 0f
    private var childShiftX = 0f
    private var childShiftY = 0f
    private var parent: UIComponent<C>? = null
    private val childList: MutableMap<String, UIComponent<C>>
    private var sortedChildList: List<UIComponent<C>>
    private var indexAsChild = 0

    init {
        childList = HashMap()
        sortedChildList = ArrayList()
        setLayoutType(LayoutType.CONSTRAINT)
        alignItems(Align.LEFT_DOWN)
        show(true)
    }

    constructor(component: UIComponent<C>) : this(
        component.xConstraint,
        component.yConstraint,
        component.widthConstraint,
        component.heightConstraint
    )

    fun render(canvas: C) {
        correctConstraints()
        calculateSize()
        correctSize()
        calculatePos()
        correctPos()

        // render element
        if (isShow) render(canvas, x, y, width, height) else return

        // touch
        isHover = checkIsHover()
        isGrab = preGrab
        if (this.isTouchDown) preGrab = true else if (Pize.isTouchReleased) preGrab = false


        // render children
        if (sortedChildList.size == 0) return
        var shiftX = 0f
        var shiftY = 0f
        for (child in sortedChildList) {
            child.shiftX = shiftX + childShiftX
            child.shiftY = shiftY + childShiftY
            child.render(canvas)
            when (layoutType) {
                LayoutType.HORIZONTAL -> shiftX += alignSignX(alignComponents) * (if (sortedChildList.size != 1 && child === sortedChildList[0] && isCenteredX) (child.width + sortedChildList[1].width) / 2 else child.width) + child.getConstraintX()
                LayoutType.VERTICAL -> shiftY += alignSignY(alignComponents) * (if (sortedChildList.size != 1 && child === sortedChildList[0] && isCenteredY) (child.height + sortedChildList[1].height) / 2 else child.height) + child.getConstraintY()
            }
        }
        renderEnd(canvas)
    }

    protected open fun correctConstraints() {}
    protected open fun correctSize() {}
    protected open fun correctPos() {}
    protected abstract fun render(canvas: C, x: Float, y: Float, width: Float, height: Float)
    protected open fun renderEnd(batch: C) {}
    val isTouchDown: Boolean
        get() = Pize.isTouchDown && isHover
    val isTouched: Boolean
        get() = Pize.isTouched && isHover
    val isTouchReleased: Boolean
        get() = Pize.isTouchReleased && isGrab

    fun show(show: Boolean) {
        isShow = show
    }

    fun setX(constraint: Constraint?) {
        xConstraint = constraint
    }

    fun setY(constraint: Constraint?) {
        yConstraint = constraint
    }

    fun setPosition(x: Constraint?, y: Constraint?) {
        xConstraint = x
        yConstraint = y
    }

    fun setPosition(xy: Constraint?) {
        xConstraint = xy
        yConstraint = xy
    }

    fun setChildShift(x: Float, y: Float) {
        childShiftX = x
        childShiftY = y
    }

    open fun setWidth(constraint: Constraint?) {
        widthConstraint = constraint
        calculateSize()
    }

    open fun setHeight(constraint: Constraint?) {
        heightConstraint = constraint
        calculateSize()
    }

    fun setSize(width: Constraint?, height: Constraint?) {
        widthConstraint = width
        heightConstraint = height
        calculateSize()
    }

    open fun setSize(widthHeight: Constraint?) {
        widthConstraint = widthHeight
        heightConstraint = widthHeight
        calculateSize()
    }

    fun setSize(component: UIComponent<*>) {
        widthConstraint = component.widthConstraint
        heightConstraint = component.heightConstraint
        calculateSize()
    }

    fun aspect(): Float {
        return width / height
    }

    fun put(id: String, child: UIComponent<C>) {
        child.indexAsChild = childList.size
        child.setParent(this)
        childList[id] = child
        sortedChildList = childList.values.stream()
            .sorted(Comparator.comparingInt { component: UIComponent<C> -> component.indexAsChild })
            .toList()
    }

    fun getParent(): UIComponent<*>? {
        return parent
    }

    protected fun setParent(component: UIComponent<C>?) {
        parent = component
    }

    protected fun setAsParentFor(vararg components: UIComponent<C>) {
        for (component in components) component.setParent(this)
    }

    operator fun <T : UIComponent<C>?> get(id: String): T {
        return childList[id] as T
    }

    fun <T : UIComponent<C>?> getByOrder(index: Int): T {
        return sortedChildList[index] as T
    }

    fun setLayoutType(layoutType: LayoutType?) {
        this.layoutType = layoutType
    }

    fun alignItems(alignComponents: Align?) {
        this.alignComponents = alignComponents
    }

    fun alignSelf(align: Align?) {
        this.align = align
    }

    private fun checkIsHover(): Boolean {
        val mouseX = Pize.x.toFloat()
        val mouseY = Pize.y.toFloat()
        if (!window()!!.isFocused || mouseX < 0 || mouseX >= Pize.width || mouseY < 0 || mouseY >= Pize.height) return false
        var hover = mouse()!!.isInBounds(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
        hover = hover and isChildHover(childList.values)
        if (parent != null) for (i in parent!!.sortedChildList.indices.reversed()) {
            val child = parent!!.sortedChildList[i]
            if (child === this) break
            if (!child.isShow) continue
            hover =
                hover and (mouseX < child.x || mouseY < child.y || mouseX >= child.x + child.width || mouseY >= child.y + child.height)
        }
        return hover
    }

    private fun isChildHover(childList: Collection<UIComponent<C>>): Boolean {
        val mouseX = Pize.x.toFloat()
        val mouseY = Pize.y.toFloat()
        var hover = true
        for (child in childList) {
            hover =
                hover and (!child.isShow || mouseX < child.x || mouseY < child.y || mouseX >= child.x + child.width || mouseY >= child.y + child.height)
            if (hover) hover = isChildHover(child.childList.values)
        }
        return hover
    }

    private fun calculateSize() {
        if (widthConstraint.getType() == ConstraintType.ASPECT) height = calcConstraintY(heightConstraint)
        width = calcConstraintX(widthConstraint)
        if (widthConstraint.getType() != ConstraintType.ASPECT) height = calcConstraintY(heightConstraint)
    }

    private fun calculatePos() {
        x = parentX + getConstraintX()
        y = parentY + getConstraintY()
        if (align == null) {
            x += shiftX + parentAlignOffsetX
            y += shiftY + parentAlignOffsetY
        } else {
            x += getAlignOffsetX(align)
            y += getAlignOffsetY(align)
        }
    }

    protected fun calcConstraintX(x: Constraint?): Float {
        return when (x.getType()) {
            ConstraintType.PIXEL -> x.getValue()
            ConstraintType.ASPECT -> x.getValue() * height
            ConstraintType.RELATIVE -> getRelativeWidth(x as RelativeConstraint?)
        }
    }

    protected fun calcConstraintY(y: Constraint?): Float {
        return when (y.getType()) {
            ConstraintType.PIXEL -> y.getValue()
            ConstraintType.ASPECT -> y.getValue() * width
            ConstraintType.RELATIVE -> getRelativeHeight(y as RelativeConstraint?)
        }
    }

    private fun getConstraintX(): Float {
        val sign =
            if (align == null) if (parent != null) alignSignX(parent!!.alignComponents) else 1 else alignSignX(align)
        return calcConstraintX(xConstraint) * sign
    }

    private fun getConstraintY(): Float {
        val sign =
            if (align == null) if (parent != null) parent!!.alignSignY(parent!!.alignComponents) else 1 else alignSignY(
                align
            )
        return calcConstraintY(yConstraint) * sign
    }

    private fun getRelativeWidth(constraint: RelativeConstraint?): Float {
        return constraint.getValue() * when (constraint.getRelativeTo()) {
            RelativeTo.AUTO, RelativeTo.WIDTH -> parentWidth
            RelativeTo.HEIGHT -> parentHeight
        }
    }

    private fun getRelativeHeight(constraint: RelativeConstraint?): Float {
        return constraint.getValue() * when (constraint.getRelativeTo()) {
            RelativeTo.AUTO, RelativeTo.HEIGHT -> parentHeight
            RelativeTo.WIDTH -> parentWidth
        }
    }

    private val parentX: Float
        private get() = if (parent == null) 0 else parent!!.x
    private val parentY: Float
        private get() = if (parent == null) 0 else parent!!.y
    private val parentWidth: Float
        private get() = if (parent == null) Pize.width.toFloat() else parent!!.width
    private val parentHeight: Float
        private get() = if (parent == null) Pize.height.toFloat() else parent!!.height
    private val parentAlignOffsetX: Float
        private get() = if (parent != null) getAlignOffsetX(parent!!.alignComponents) else 0
    private val parentAlignOffsetY: Float
        private get() = if (parent != null) getAlignOffsetY(parent!!.alignComponents) else 0

    private fun getAlignOffsetX(align: Align?): Float {
        return if (align == Align.CENTER || align == Align.DOWN || align == Align.UP) (parentWidth - width) / 2 else if (align == Align.RIGHT || align == Align.RIGHT_DOWN || align == Align.RIGHT_UP) parentWidth - width else 0
    }

    private fun getAlignOffsetY(align: Align?): Float {
        return if (align == Align.CENTER || align == Align.LEFT || align == Align.RIGHT) (parentHeight - height) / 2 else if (align == Align.UP || align == Align.LEFT_UP || align == Align.RIGHT_UP) parentHeight - height else 0
    }

    private fun alignSignX(align: Align?): Int {
        return if (align == Align.RIGHT || align == Align.RIGHT_DOWN || align == Align.RIGHT_UP) -1 else 1
    }

    private fun alignSignY(align: Align?): Int {
        return if (align == Align.UP || align == Align.LEFT_UP || align == Align.RIGHT_UP) -1 else 1
    }

    private val isCenteredX: Boolean
        private get() = when (alignComponents) {
            Align.UP, Align.DOWN, Align.CENTER -> true
            else -> false
        }
    private val isCenteredY: Boolean
        private get() = when (alignComponents) {
            Align.LEFT, Align.RIGHT, Align.CENTER -> true
            else -> false
        }

    fun copy(): UIComponent<C> {
        return try {
            super.clone() as UIComponent<*>
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }
}
