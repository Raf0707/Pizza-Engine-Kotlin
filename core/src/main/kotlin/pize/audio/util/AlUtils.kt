package pize.audio.util

import org.lwjgl.openal.AL10
import javax.sound.sampled.AudioFormat

object AlUtils {
    @Throws(Error::class)
    fun getAlFormat(bitsPerSample: Int, channels: Int): Int {
        if (bitsPerSample == 16) {
            if (channels == 1) return AL10.AL_FORMAT_MONO16 else if (channels == 2) return AL10.AL_FORMAT_STEREO16
        } else if (bitsPerSample == 8) {
            if (channels == 1) return AL10.AL_FORMAT_MONO8 else if (channels == 2) return AL10.AL_FORMAT_STEREO8
        }
        throw Error("Unsupported audio format")
    }

    fun getAlFormat(format: AudioFormat): Int {
        return getAlFormat(format.sampleSizeInBits, format.channels)
    }
}
