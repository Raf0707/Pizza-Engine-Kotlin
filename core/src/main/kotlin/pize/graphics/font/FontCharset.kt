package pize.graphics.font

import kotlin.math.max
import kotlin.math.min

class FontCharset(charset: String) {
    private var charset: String? = null
    var firstChar = 0
        private set
    var lastChar = 0
        private set

    init {
        set(charset)
    }

    private fun set(charset: String) {
        this.charset = charset
        lastChar = 0
        for (i in 0 until charset.length) lastChar = max(lastChar.toDouble(), charset[i].code.toDouble())
            .toInt()
        firstChar = lastChar
        for (i in 0 until charset.length) firstChar = min(firstChar.toDouble(), charset[i].code.toDouble())
            .toInt()
    }

    operator fun contains(character: Char): Boolean {
        return charset!!.contains(character.toString())
    }

    fun charAt(index: Int): Char {
        return charset!![index]
    }

    fun size(): Int {
        return charset!!.length
    }

    override fun toString(): String {
        return charset!!
    }

    companion object {
        val SPECIAL_SYMBOLS = FontCharset("~`!@#$%^&*()-_+={}[]|\\/:;\"'<>,.? ")
        @JvmField
        val NUMBERS = FontCharset("0123456789")
        val ENG = FontCharset("ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxyz")
        val RUS = FontCharset("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя")
        @JvmField
        val DEFAULT = FontCharset(SPECIAL_SYMBOLS.toString() + NUMBERS + ENG)
        val DEFAULT_RUS = FontCharset(SPECIAL_SYMBOLS.toString() + NUMBERS + ENG + RUS)
    }
}
