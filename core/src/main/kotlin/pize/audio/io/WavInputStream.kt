package pize.audio.io

import java.io.*
import kotlin.math.min

class WavInputStream(inputStream: InputStream?) : FilterInputStream(inputStream) {
    private val channels: Int
    private val sampleRate: Int
    private var availableBytes: Int

    init {

        // Header
        if (read() != 'R'.code || read() != 'I'.code || read() != 'F'.code || read() != 'F'.code) throw RuntimeException(
            "RIFF header not found"
        )
        skipFully(4)
        if (read() != 'W'.code || read() != 'A'.code || read() != 'V'.code || read() != 'E'.code) throw RuntimeException(
            "Invalid wave file header"
        )
        val fmtChunkLength = seekToChunk('f', 'm', 't', ' ')

        // Audio Format
        val format = read() and 0xFF or (read() and 0xFF shl 8)
        if (format != 1) // PCM audio format code = 1
            throw RuntimeException("Unsupported format, WAV files must be PCM")

        // Num Channels
        channels = read() and 0xFF or (read() and 0xFF shl 8)
        if (channels != 1 && channels != 2) throw RuntimeException("WAV files must have 1 or 2 channels: $channels")

        // Sample Rate
        sampleRate = read() and 0xFF or (read() and 0xFF shl 8) or (read() and 0xFF shl 16) or (read() and 0xFF shl 24)
        skipFully(6)

        // Bits Per Sample
        val bitsPerSample = read() and 0xFF or (read() and 0xFF shl 8)
        if (bitsPerSample != 16) throw RuntimeException("WAV files must have 16 bits per sample: $bitsPerSample")

        // Skip to Data Chunk
        skipFully(fmtChunkLength - 16)
        availableBytes = seekToChunk('d', 'a', 't', 'a')
    }

    constructor(path: File?) : this(FileInputStream(path))
    constructor(path: String?) : this(File(path))

    @Throws(IOException::class)
    override fun read(buffer: ByteArray): Int {
        if (availableBytes == 0) return -1
        var offset = 0
        do {
            val length = min(super.read(buffer, offset, buffer.size - offset).toDouble(), availableBytes.toDouble())
                .toInt()
            if (length == -1) {
                return if (offset > 0) offset else -1
            }
            offset += length
            availableBytes -= length
        } while (offset < buffer.size)
        return offset
    }

    @Throws(IOException::class)
    private fun skipFully(count: Int) {
        var count = count
        while (count > 0) {
            val skipped = super.skip(count.toLong())
            if (skipped <= 0) throw EOFException("Unable to skip")
            count -= skipped.toInt()
        }
    }

    @Throws(IOException::class)
    private fun seekToChunk(c1: Char, c2: Char, c3: Char, c4: Char): Int {
        while (true) {
            var found = read() == c1.code
            found = found and (read() == c2.code)
            found = found and (read() == c3.code)
            found = found and (read() == c4.code)
            val chunkLength =
                read() and 0xFF or (read() and 0xFF shl 8) or (read() and 0xFF shl 16) or (read() and 0xFF shl 24)
            if (chunkLength == -1) throw IOException("Chunk not found: $c1$c2$c3$c4")
            if (found) return chunkLength
            skipFully(chunkLength)
        }
    }

    fun channels(): Int {
        return channels
    }

    fun sampleRate(): Int {
        return sampleRate
    }

    override fun available(): Int {
        return availableBytes
    }
}