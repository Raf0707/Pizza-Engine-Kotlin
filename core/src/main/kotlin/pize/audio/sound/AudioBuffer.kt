package pize.audio.sound

import org.lwjgl.openal.AL10
import pize.app.Disposable
import pize.audio.util.AlUtils
import pize.files.Resource
import java.nio.ByteBuffer
import java.nio.ShortBuffer

class AudioBuffer() : Disposable {
    val id: Int

    init {
        id = AL10.alGenBuffers()
    }

    constructor(res: Resource) : this() {
        AudioLoader.load(this, res)
    }

    constructor(filepath: String) : this(Resource(filepath))

    fun setData(data: ByteBuffer?, channels: Int, sampleRate: Int) {
        val format = AlUtils.getAlFormat(bits, channels)
        AL10.alBufferData(id, format, data, sampleRate)
    }

    fun setData(data: ShortBuffer?, channels: Int, sampleRate: Int) {
        val format = AlUtils.getAlFormat(bits, channels)
        AL10.alBufferData(id, format, data, sampleRate)
    }

    val frequency: Int
        get() = AL10.alGetBufferi(id, AL10.AL_FREQUENCY)
    val bits: Int
        get() = AL10.alGetBufferi(id, AL10.AL_BITS)
    val channels: Int
        get() = AL10.alGetBufferi(id, AL10.AL_CHANNELS)
    val size: Int
        get() = AL10.alGetBufferi(id, AL10.AL_SIZE)
    val duration: Float
        get() = size.toFloat() * 8 / (channels * bits) / frequency

    override fun dispose() {
        AL10.alDeleteBuffers(id)
    }

    override fun hashCode(): Int {
        return id * 11
    }
}
