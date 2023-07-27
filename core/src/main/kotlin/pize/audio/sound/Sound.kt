package pize.audio.sound

import pize.files.Resource
import java.nio.ByteBuffer

class Sound : AudioSource {
    val buffer: AudioBuffer

    constructor(res: Resource) {
        buffer = AudioBuffer(res)
        setBuffer(buffer)
    }

    constructor(filepath: String) : this(Resource(filepath))
    constructor(data: ByteBuffer?, bitsPerSample: Int, channels: Int, sampleRate: Int) {
        buffer = AudioBuffer()
        AudioLoader.load(buffer, data, bitsPerSample, channels, sampleRate)
        setBuffer(buffer)
    }

    val duration: Float
        get() = buffer.duration

    override fun dispose() {
        buffer.dispose()
        super.dispose()
    }
}
