package pize.audio.sound

import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.Decoder
import javazoom.jl.decoder.Header
import javazoom.jl.decoder.SampleBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.openal.AL10
import pize.audio.io.OggInputStream
import pize.audio.io.WavInputStream
import pize.audio.util.AlUtils
import pize.files.Resource
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

object AudioLoader {
    fun loadWav(audioBuffer: AudioBuffer?, resource: Resource) {
        if (audioBuffer == null) return
        try {
            WavInputStream(resource.inStream()).use { input ->
                val data = input.readAllBytes()
                val buffer = BufferUtils.createByteBuffer(data.size)
                buffer.put(data).flip()
                audioBuffer.setData(buffer, input.channels(), input.sampleRate())
            }
        } catch (e: Exception) {
            throw RuntimeException("Sound '" + resource.path + "' reading is failed: " + e.message)
        }
    }

    fun loadOgg(audioBuffer: AudioBuffer?, resource: Resource) {
        if (audioBuffer == null) return
        try {
            OggInputStream(resource.inStream()).use { input ->
                val output = ByteArrayOutputStream(4096)
                val tempBuffer = ByteArray(2048)
                while (!input.atEnd()) {
                    val length = input.read(tempBuffer)
                    if (length == -1) break
                    output.write(tempBuffer, 0, length)
                }
                val buffer = output.toByteArray()
                val bufferSize = buffer.size - buffer.size % if (input.channels() > 1) 4 else 2
                val byteBuffer = BufferUtils.createByteBuffer(bufferSize)
                byteBuffer.order(ByteOrder.nativeOrder())
                byteBuffer.put(buffer)
                byteBuffer.flip()
                audioBuffer.setData(byteBuffer.asShortBuffer(), input.channels(), input.sampleRate())
            }
        } catch (e: IOException) {
            throw RuntimeException("Sound '" + resource.path + "' reading is failed: " + e.message)
        }
    }

    fun loadMp3(audioBuffer: AudioBuffer?, resource: Resource) {
        if (audioBuffer == null) return
        val output = ByteArrayOutputStream(1024)
        val bitstream = Bitstream(resource.inStream())
        val decoder = Decoder()
        var sampleRate = -1
        var channels = -1
        try {
            while (true) {
                val header = bitstream.readFrame() ?: break
                val buffer = decoder.decodeFrame(header, bitstream) as SampleBuffer
                for (value in buffer.buffer) {
                    output.write(value.toInt() and 0xff)
                    output.write(value.toInt() shr 8 and 0xff)
                }
                if (channels == -1) {
                    channels = if (header.mode() == Header.SINGLE_CHANNEL) 1 else 2
                    sampleRate = decoder.outputFrequency
                }
                bitstream.closeFrame()
            }
            val byteBuffer = BufferUtils.createByteBuffer(output.size())
            byteBuffer.order(ByteOrder.nativeOrder())
            byteBuffer.put(output.toByteArray())
            byteBuffer.flip()
            audioBuffer.setData(byteBuffer.asShortBuffer(), channels, sampleRate)
        } catch (e: Throwable) {
            throw RuntimeException("Sound '" + resource.path + "' reading is failed: " + e.message)
        }
    }

    @JvmStatic
    fun load(audioBuffer: AudioBuffer?, resource: Resource) {
        when (resource.getExtension().lowercase(Locale.getDefault())) {
            "ogg" -> loadOgg(audioBuffer, resource)
            "wav" -> loadWav(audioBuffer, resource)
            "mp3" -> loadMp3(audioBuffer, resource)
            else -> throw Error("Sound format is not supported: $resource")
        }
    }

    fun load(audioBuffer: AudioBuffer?, data: ByteBuffer?, bitsPerSample: Int, channels: Int, sampleRate: Int) {
        if (audioBuffer == null) return
        val format = AlUtils.getAlFormat(bitsPerSample, channels)
        AL10.alBufferData(audioBuffer.id, format, data, sampleRate)
    }
}
