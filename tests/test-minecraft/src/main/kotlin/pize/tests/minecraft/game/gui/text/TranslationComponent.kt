package pize.tests.minecraft.game.gui.text

import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.gui.text.formatting.Style
import pize.tests.minecraft.game.lang.ClientLanguage

class TranslationComponent(private val parent: Component, val key: String?, vararg args: Component) : Component() {
    val args: Array<Component>
    private val argsIndices: IntArray
    private var decomposedWith: ClientLanguage? = null

    init {
        this.args = args
        argsIndices = IntArray(args.size)
    }

    private fun decompose() {
        super.components.clear()
        super.color.set(parent.color)
        System.arraycopy(parent.style, 0, super.style, 0, 6)
        val parts =
            decomposedWith!!.getOrDefault(key)!!.split("%s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var argIndex = 0
        for (part in parts) {
            super.formattedText(part)
            if (argIndex < args.size) {
                argsIndices[argIndex] = super.components.size - 1
                component(args[argIndex])
                argIndex++
            }
        }
    }

    fun update(session: Session) {
        val currentLanguage = session.languageManager.language
        if (decomposedWith !== currentLanguage) {
            decomposedWith = currentLanguage
            decompose()
        }
    }

    fun getArg(index: Int): Component {
        return args[argsIndices[index]]
    }

    override val style: Style
        get() = Style.Companion.DEFAULT
}
