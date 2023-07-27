package pize.tests.voxelgame.main.text

import pize.graphics.util.color.Color
import pize.tests.voxelgame.main.lang.Language

class ComponentTranslate(
    style: TextStyle?,
    color: Color,
    private val translationKey: String?,
    vararg arguments: Component
) : Component(style, color) {
    private val arguments: Array<Component>
    private var translatedWith: Language? = null

    init {
        this.arguments = arguments
    }

    fun updateTranslation(language: Language) {
        if (language === translatedWith) return
        val translation = language.getTranslation(translationKey)
        val translationParts = translation!!.split("%s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        super.formattedText(translationParts[0])
        for (argument in arguments) {
            super.addComponent(argument)
            super.formattedText(translationParts[++i])
        }
        translatedWith = language
    }

    override fun toString(): String {
        return super.toString()
    }
}
