package pize.audio

import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import pize.app.Disposable

class AudioContext(private val device: AudioDevice) : Disposable {
    val id: Long

    init {
        id = ALC10.alcCreateContext(device.id, intArrayOf(0))
        val currentContext = ALC10.alcGetCurrentContext()
        ALC10.alcMakeContextCurrent(id)
        AL.createCapabilities(ALC.createCapabilities(device.id))
        ALC10.alcMakeContextCurrent(currentContext)
    }

    fun makeCurrent() {
        ALC10.alcMakeContextCurrent(id)
    }

    override fun dispose() {
        ALC10.alcDestroyContext(id)
    }
}
