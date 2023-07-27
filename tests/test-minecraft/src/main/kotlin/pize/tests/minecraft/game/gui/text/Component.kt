package pize.tests.minecraft.game.gui.text

import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.text.formatting.*
import java.util.*

open class Component {
    protected val components: MutableList<Component?>
    val color: Color
    val style: BooleanArray

    init {
        components = ArrayList()
        color = Color()
        style = BooleanArray(StyleFormatting.entries.size)
    }

    fun text(objectToString: Any): Component {
        components.add(
            TextComponent(
                this, objectToString.toString(),
                Style(color.copy(), style[0], style[1], style[2], style[3], style[4], style[5])
            )
        )
        return this
    }

    fun formattedText(text: String?): Component {
        var textPart = StringBuilder()
        var i = 0
        while (i < text!!.length) {
            val code = text[i]
            if (code == TextFormatting.Companion.FORMATTING_SYMBOL && i < text.length - 1) {
                if (textPart.toString() != "") {
                    text(textPart.toString())
                    textPart = StringBuilder()
                }
                val format: TextFormatting = TextFormatting.Companion.fromCode(text[i + 1])
                if (format != null) {
                    if (format == TextFormatting.RESET) reset() else if (format.isColor) color(format) else style(format)
                }
                i++
            } else textPart.append(code)
            i++
        }
        if (textPart.toString() != "") text(textPart.toString())
        return this
    }

    fun formattedText(components: Array<TextComponent?>): Component {
        Collections.addAll(this.components, *components)
        return this
    }

    fun translation(translationKey: String?, vararg args: Component?): Component {
        components.add(TranslationComponent(this, translationKey, *args))
        return this
    }

    fun component(component: Component?): Component {
        components.add(component)
        return this
    }

    fun color(format: TextFormatting): Component {
        color.set(format.color()!!)
        return this
    }

    fun color(color: IColor?): Component {
        this.color.set(color!!)
        return this
    }

    fun style(format: TextFormatting): Component {
        style[format.style()!!.id] = true
        return this
    }

    fun style(style: StyleFormatting): Component {
        this.style[style.id] = true
        return this
    }

    fun reset(): Component {
        color.set(TextFormatting.WHITE.color()!!)
        Arrays.fill(style, false)
        return this
    }

    fun clear(): Component {
        components.clear()
        return this
    }

    fun getComponent(index: Int): Component? {
        return components[index]
    }

    fun getComponentAsTest(index: Int): TextComponent? {
        return components[index] as TextComponent?
    }

    fun getComponentAsTranslation(index: Int): TranslationComponent? {
        return components[index] as TranslationComponent?
    }

    fun size(): Int {
        return components.size
    }

    fun getAllText(session: Session): String {
        val builder = StringBuilder()
        for (component in components) {
            builder.append(
                when (component!!.javaClass.getSimpleName()) {
                    "TextComponent" -> (component as TextComponent?)!!.getText()
                    "TranslationComponent" -> {
                        val translationComponent = component as TranslationComponent?
                        translationComponent!!.update(session)
                        translationComponent.getAllText(session)
                    }

                    else -> component.getAllText(session)
                }
            )
        }
        return builder.toString()
    }

    fun getAllComponents(session: Session): List<TextComponent?> {
        val allComponents: MutableList<TextComponent?> = ArrayList()
        getAllComponents(session, allComponents, this)
        return allComponents
    }

    private fun getAllComponents(
        session: Session,
        allComponents: MutableList<TextComponent?>,
        currentComponent: Component?
    ) {
        for (component in currentComponent!!.components) {
            if (component!!.size() == 0) {
                if (component.javaClass == TranslationComponent::class.java) {
                    val translationComponent = component as TranslationComponent?
                    translationComponent!!.update(session)
                    getAllComponents(session, allComponents, translationComponent)
                } else allComponents.add(component as TextComponent?)
            } else getAllComponents(session, allComponents, component)
        }
    }
}
