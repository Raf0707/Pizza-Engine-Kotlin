package pize.audio.io

import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class WavFile(private val file: File, private val sampleRate: Int, channels: Int) {
    private val channels: Short
    private var data: ByteArray

    init {
        this.channels = channels.toShort()
    }

    constructor(path: String?, sampleRate: Int, channels: Int) : this(File(path), sampleRate, channels)

    fun save() {
        try {
            DataOutputStream(FileOutputStream(file)).use { out ->
                run {
                    // Chunk Descriptor
                    out.writeBytes("RIFF") // ID
                    out.writeInt(Integer.reverseBytes(20 + 16 + Integer.reverseBytes(data.size))) // Size (Не обязательно)
                    out.writeBytes("WAVE") // Format
                }
                run {
                    // Ftm SubChunk
                    out.writeBytes("fmt ") // ID
                    out.writeInt(Integer.reverseBytes(16)) // Size (16 for PCM audio format)
                    out.writeShort(java.lang.Short.reverseBytes(1.toShort()).toInt()) // Audio Format (PCM = 1)
                    out.writeShort(java.lang.Short.reverseBytes(channels).toInt()) // Num Channels
                    out.writeInt(Integer.reverseBytes(sampleRate)) // Sample Rate
                    out.writeInt(Integer.reverseBytes(sampleRate * channels * BITS_PER_SAMPLE / 8)) // Byte Rate (Не обязательно)
                    out.writeShort(
                        java.lang.Short.reverseBytes((channels * BITS_PER_SAMPLE / 8).toShort()).toInt()
                    ) // Block Align
                    out.writeShort(
                        java.lang.Short.reverseBytes(BITS_PER_SAMPLE.toShort()).toInt()
                    ) // Bits Per Sample (16)
                }
                run {
                    // Data SubChunk
                    out.writeBytes("data") // ID
                    out.writeInt(Integer.reverseBytes(data.size)) // Size
                    out.write(data) // Data
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e.message)
        }
    }

    fun setData(data: ByteArray) {
        this.data = data
    }

    fun setData(buffer: ByteBuffer) {
        data = ByteArray(buffer.remaining())
        var i = 0
        while (buffer.hasRemaining()) {
            data[i] = buffer.get()
            i++
        }
    }

    companion object {
        const val BITS_PER_SAMPLE = 16
    }
}
