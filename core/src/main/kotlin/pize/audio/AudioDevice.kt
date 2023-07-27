package pize.audio

import org.lwjgl.openal.ALC10
import org.lwjgl.openal.ALC11
import pize.app.Disposable

class AudioDevice(device: String?) : Disposable {
    val id: Long
    val context: AudioContext

    init {
        id = ALC10.alcOpenDevice(device)
        context = AudioContext(this)
    }

    val name: String?
        get() = ALC10.alcGetString(id, ALC11.ALC_ALL_DEVICES_SPECIFIER)
    val stereoSources: Int
        get() = ALC10.alcGetInteger(id, ALC11.ALC_STEREO_SOURCES)
    val monoSources: Int
        get() = ALC10.alcGetInteger(id, ALC11.ALC_MONO_SOURCES)
    val sync: Int
        get() = ALC10.alcGetInteger(id, ALC10.ALC_SYNC)
    val samples: Int
        get() = ALC10.alcGetInteger(id, ALC11.ALC_CAPTURE_SAMPLES)
    val frequency: Int
        get() = ALC10.alcGetInteger(id, ALC10.ALC_FREQUENCY)
    val refreshRate: Int
        get() = ALC10.alcGetInteger(id, ALC10.ALC_REFRESH)

    fun makeCurrent() {
        context.makeCurrent()
    }

    override fun dispose() {
        context.dispose()
        ALC10.alcCloseDevice(id)
    }
}
