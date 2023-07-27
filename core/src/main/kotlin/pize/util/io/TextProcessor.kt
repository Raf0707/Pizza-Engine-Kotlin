package pize.util.io

import pize.Pize
import pize.app.Disposable
import pize.io.glfw.Key
import pize.io.glfw.KeyAction
import pize.io.keyboard.CharCallback
import pize.io.keyboard.KeyCallback
import pize.util.StringUtils
import java.util.*

class TextProcessor @JvmOverloads constructor(var isNewLineOnEnter: Boolean = true) : Disposable, CharCallback,
    KeyCallback {
    var isActive = true
    private val lines: MutableList<String>
    private var currentLineLength = 0
    var cursorX = 0
        private set
    var cursorY = 0
        private set
    private var cursorInEnd = false
    var tabSpaces = 4

    init {
        lines = ArrayList()
        lines.add("")
        Pize.keyboard()!!.addCharCallback(this)
        Pize.keyboard()!!.addKeyCallback(this)
    }

    override fun invoke(character: Char) {
        if (!isActive) return
        insertChar(character)
    }

    override fun invoke(keyCode: Int, action: KeyAction) {
        if (!isActive || action == KeyAction.RELEASE) return
        moveCursor(keyCode)
        if (keyCode == Key.TAB.GLFW) insertLine(" ".repeat(tabSpaces))
        if (isNewLineOnEnter && keyCode == Key.ENTER.GLFW) newLineAndWrap() else if (keyCode == Key.BACKSPACE.GLFW && !(cursorX == 0 && cursorY == 0)) {
            if (cursorX == 0) removeLineAndWrap() else removeChar()
        }
    }

    private fun removeChar() {
        val line = lines[cursorY]
        lines[cursorY] = line.substring(0, cursorX - 1) + line.substring(cursorX)
        cursorX--
        currentLineLength--
    }

    fun insertChar(character: Char) {
        val currentLine = lines[cursorY]
        lines[cursorY] = currentLine.substring(0, cursorX) + character + currentLine.substring(cursorX)
        cursorX++
        currentLineLength++
    }

    fun insertLine(line: String) {
        var line = line
        line = line.replace("\n".toRegex(), "")
        var currentLine = lines[cursorY]
        currentLine = currentLine.substring(0, cursorX) + line + currentLine.substring(cursorX)
        lines[cursorY] = currentLine
        cursorX += line.length
        currentLineLength += line.length
    }

    fun insertText(text: String) {
        val linesNum = StringUtils.count(text, "\n") + 1
        val lines = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 0 until linesNum) {
            if (i != 0) newLineAndWrap()
            if (i < lines.size) insertLine(lines[i])
        }
    }

    fun newLineAndWrap() {
        val prevLine = lines[cursorY]
        lines[cursorY] = prevLine.substring(0, cursorX)
        lines.add(cursorY + 1, prevLine.substring(cursorX))
        cursorY++
        updateCurrentLineLength()
        moveCursorHome()
    }

    fun removeLineAndWrap() {
        val removedLine = lines[cursorY]
        lines.removeAt(cursorY)
        cursorY--
        updateCurrentLineLength()
        moveCursorEnd()
        lines[cursorY] = lines[cursorY] + removedLine
        updateCurrentLineLength()
    }

    fun removeLine() {
        if (lines.size == 1) lines[0] = "" else {
            lines.removeAt(cursorY)
            if (cursorY >= lines.size) cursorY--
        }
        updateCurrentLineLength()
        moveCursorEnd()
    }

    private fun updateCurrentLineLength() {
        currentLineLength = lines[cursorY].length
    }

    private fun moveCursor(keyCode: Int) {
        if (keyCode == Key.END.GLFW) moveCursorEnd()
        if (keyCode == Key.HOME.GLFW) moveCursorHome()
        if (keyCode == Key.UP.GLFW) moveCursorUp()
        if (keyCode == Key.DOWN.GLFW) moveCursorDown()
        if (keyCode == Key.LEFT.GLFW) moveCursorLeft()
        if (keyCode == Key.RIGHT.GLFW) moveCursorRight()
        cursorInEnd = cursorX == currentLineLength && cursorX > 0 && !lines[cursorY].substring(0, cursorX).isBlank()
    }

    fun moveCursorEnd() {
        cursorX = currentLineLength
    }

    fun moveCursorHome() {
        cursorX = 0
    }

    fun moveCursorDown() {
        if (cursorY < lines.size - 1) {
            cursorY++
            updateCurrentLineLength()
            norCursorX()
        }
    }

    fun moveCursorUp() {
        if (cursorY > 0) {
            cursorY--
            updateCurrentLineLength()
            norCursorX()
        }
    }

    fun moveCursorLeft() {
        if (cursorX > 0) cursorX-- else if (cursorY > 0) {
            cursorY--
            updateCurrentLineLength()
            moveCursorEnd()
        }
    }

    fun moveCursorRight() {
        if (cursorX < currentLineLength) cursorX++ else if (cursorY < lines.size - 1) {
            cursorY++
            updateCurrentLineLength()
            moveCursorHome()
        }
    }

    private fun norCursorX() {
        if (cursorX > currentLineLength || cursorInEnd) cursorX = currentLineLength
    }

    fun getLines(): List<String> {
        return lines
    }

    val currentLine: String?
        get() = if (cursorY >= lines.size) null else lines[cursorY]

    fun clear() {
        moveCursorHome()
        cursorY = 0
        lines.clear()
        lines.add("")
        updateCurrentLineLength()
    }

    override fun toString(): String {
        val joiner = StringJoiner("\n")
        for (line in lines) joiner.add(line)
        return joiner.toString()
    }

    override fun dispose() {
        lines.clear()
        Pize.keyboard()!!.removeCharCallback(this)
        Pize.keyboard()!!.removeKeyCallback(this)
    }
}
