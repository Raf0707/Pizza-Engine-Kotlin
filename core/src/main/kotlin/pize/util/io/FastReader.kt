package pize.util.io

import pize.util.Utils
import java.io.*

class FastReader @JvmOverloads constructor(private val inputStream: InputStream? = System.`in`) {
    private val buffer: ByteArray
    private var pointer = 0
    private var bytesRead = 0
    private var charBuffer: CharArray

    init {
        buffer = ByteArray(BUFFER_SIZE)
        charBuffer = CharArray(128)
    }

    @Throws(IOException::class)
    private fun read(): Byte {
        if (bytesRead == EOF.toInt()) throw IOException()
        if (pointer == bytesRead) fillBuffer()
        return buffer[pointer++]
    }

    private fun fillBuffer() {
        try {
            pointer = 0
            bytesRead = inputStream!!.read(buffer)
            if (!hasNext()) buffer[0] = EOF
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    operator fun next(): String? {
        try {
            if (!hasNext() || readJunk(SPACE.toInt()) == EOF.toInt()) return null
            var i = 0
            while (true) {
                while (pointer < bytesRead) {
                    if (buffer[pointer] > SPACE) {
                        if (i == charBuffer.size) doubleCharBufferSize()
                        charBuffer[i++] = Char(buffer[pointer++].toUShort())
                    } else {
                        pointer++
                        return String(charBuffer, 0, i)
                    }
                }
                bytesRead = inputStream!!.read(buffer)
                if (bytesRead == EOF.toInt()) return String(charBuffer, 0, i)
                pointer = 0
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun doubleCharBufferSize() {
        val newBuffer = CharArray(charBuffer.size shl 1)
        System.arraycopy(charBuffer, 0, newBuffer, 0, charBuffer.size)
        charBuffer = newBuffer
    }

    @Throws(IOException::class)
    private fun readJunk(token: Int): Int {
        if (bytesRead == EOF.toInt()) return EOF.toInt()
        do {
            while (pointer < bytesRead) {
                if (buffer[pointer] > token) return 0
                pointer++
            }
            bytesRead = inputStream!!.read(buffer)
            if (bytesRead == EOF.toInt()) return EOF.toInt()
            pointer = 0
        } while (true)
    }

    fun nextLine(): String {
        try {
            val c = read()
            if (c == NEW_LINE || c == EOF) return ""
            var i = 0
            charBuffer[i++] = Char(c.toUShort())
            do {
                while (pointer < bytesRead) {
                    if (buffer[pointer] != NEW_LINE) {
                        if (i == charBuffer.size) doubleCharBufferSize()
                        charBuffer[i++] = Char(buffer[pointer++].toUShort())
                    } else {
                        pointer++
                        return String(charBuffer, 0, i)
                    }
                }
                bytesRead = inputStream!!.read(buffer)
                if (bytesRead == EOF.toInt()) return String(charBuffer, 0, i)
                pointer = 0
            } while (true)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun readString(): String {
        return try {
            String(inputStream!!.readAllBytes())
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun nextInt(): Int {
        return next()!!.toInt()
    }

    fun nextFloat(): Float {
        return next()!!.toFloat()
    }

    fun nextLong(): Long {
        return next()!!.toLong()
    }

    fun nextDouble(): Double {
        return next()!!.toDouble()
    }

    fun nextBool(): Boolean {
        return next().toBoolean()
    }

    operator fun hasNext(): Boolean {
        return bytesRead != EOF.toInt()
    }

    fun waitNext() {
        while (!hasNext());
    }

    fun close() {
        if (inputStream != null) Utils.close(inputStream)
    }

    companion object {
        private const val BUFFER_SIZE = 1 shl 16
        private const val EOF: Byte = -1
        private const val NEW_LINE: Byte = 10
        private const val SPACE: Byte = 32
    }
}
