package pize.audio.util

import org.lwjgl.BufferUtils
import pize.Pize
import pize.math.Maths
import java.nio.ByteBuffer
import kotlin.math.sign

class SoundGenerator {
    @JvmField
    var sampleRate: Int
    @JvmField
    var channels = 2
    var bitsPerSample = 16

    init {
        sampleRate = Pize.audio()?.current?.frequency!!
    }

    fun sin(frequency: Double, seconds: Double): ByteBuffer {
        val samples = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(samples * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        val sinPosInc = frequency / sampleRate
        for (i in 0 until samples) {
            val sample = Maths.round(Byte.MAX_VALUE * kotlin.math.sin(sinPos * Maths.PI2)).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
        }
        buffer.flip()
        return buffer
    }

    fun sinDown(frequency: Double, seconds: Double): ByteBuffer {
        val samples = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(samples * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        var sinPosInc = frequency / sampleRate
        val offset = sinPosInc / samples
        for (i in 0 until samples) {
            val sample = Maths.round(Byte.MAX_VALUE * kotlin.math.sin(sinPos * Maths.PI2)).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
            sinPosInc -= offset
        }
        buffer.flip()
        return buffer
    }

    fun sinUp(frequency: Double, seconds: Double): ByteBuffer {
        val samples = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(samples * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        var sinPosInc = frequency / sampleRate
        val offset = sinPosInc / samples
        for (i in 0 until samples) {
            val sample = Maths.round(Byte.MAX_VALUE * kotlin.math.sin(sinPos * Maths.PI2)).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
            sinPosInc += offset
        }
        buffer.flip()
        return buffer
    }

    fun square(frequency: Double, seconds: Double): ByteBuffer {
        val size = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(size * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        val sinPosInc = frequency / sampleRate
        for (i in 0 until size) {
            val sample = Maths.round(Byte.MAX_VALUE * sign(kotlin.math.sin(sinPos * Maths.PI2))).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
        }
        buffer.flip()
        return buffer
    }

    fun squareDown(frequency: Double, seconds: Double): ByteBuffer {
        val samples = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(samples * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        var sinPosInc = frequency / sampleRate
        val offset = sinPosInc / samples
        for (i in 0 until samples) {
            val sample = Maths.round(Byte.MAX_VALUE * sign(kotlin.math.sin(sinPos * Maths.PI2))).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
            sinPosInc -= offset
        }
        buffer.flip()
        return buffer
    }

    fun squareUp(frequency: Double, seconds: Double): ByteBuffer {
        val samples = Maths.round(seconds * sampleRate)
        val buffer = BufferUtils.createByteBuffer(samples * bitsPerSample / 8 * channels)
        var sinPos = 0.0
        var sinPosInc = frequency / sampleRate
        val offset = sinPosInc / samples
        for (i in 0 until samples) {
            val sample = Maths.round(Byte.MAX_VALUE * sign(kotlin.math.sin(sinPos * Maths.PI2))).toFloat()
            for (j in 0 until channels) when (bitsPerSample) {
                16 -> buffer.putShort(sample.toInt().toShort())
                8 -> buffer.put(sample.toInt().toByte())
            }
            sinPos += sinPosInc
            if (sinPos > 1) sinPos -= 1.0
            sinPosInc += offset
        }
        buffer.flip()
        return buffer
    }
}
