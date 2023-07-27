package pize.tests.voxelgame.main.text

import pize.graphics.util.color.Color

open class Component {
    private val components: MutableList<Component>
    val style: TextStyle?
    val color: Color

    constructor(style: TextStyle?, color: Color) {
        components = ArrayList()
        this.style = style!!.copy()
        this.color = color.copy()
    }

    constructor() {
        components = ArrayList()
        style = TextStyle()
        color = Color()
    }

    fun component(component: Component): Component {
        components.add(component)
        return this
    }

    fun style(styleFormatting: StyleFormatting): Component {
        style!!.enable(styleFormatting)
        return this
    }

    fun color(textColor: TextColor): Component {
        color.set(textColor.color)
        return this
    }

    fun color(r: Double, g: Double, b: Double): Component {
        color[r, g, b] = 1.0
        return this
    }

    fun reset(): Component {
        style!!.reset()
        color.reset()
        return this
    }

    fun text(text: String?): Component {
        val component = ComponentText(style, color, text)
        components.add(component)
        return this
    }

    fun text(objectToString: Any?): Component {
        return text(objectToString.toString())
    }

    fun translate(translateKey: String?, vararg arguments: Component?): Component {
        val component = ComponentTranslate(style, color, translateKey, *arguments)
        components.add(component)
        return this
    }

    fun formattedText(text: String?): Component {
        text(text)
        return this
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (component in components) builder.append(component.toString())
        return builder.toString()
    }

    fun toFlatList(): MutableList<ComponentText> {
        val list: MutableList<ComponentText> = ArrayList()
        addToFlatList(list)
        return list
    }

    private fun addToFlatList(list: MutableList<ComponentText>) {
        for (component in components) {
            if (component is ComponentText) list.add(component)
            component.addToFlatList(list)
        }
    }

    fun addComponent(component: Component) {
        components.add(component)
    }
}
