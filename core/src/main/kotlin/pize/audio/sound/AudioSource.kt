package pize.audio.sound

import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL11
import pize.app.Disposable
import pize.math.Mathc
import pize.math.vecmath.vector.Vec3f

open class AudioSource : Disposable {
    protected val id: Int

    init {
        id = AL10.alGenSources()
    }

    fun queueBuffers(buffers: Array<AudioBuffer>) {
        val bufferIds = IntArray(buffers.size)
        for (i in buffers.indices) bufferIds[i] = buffers[i].id
        AL10.alSourceQueueBuffers(id, bufferIds)
    }

    fun queueBuffer(buffer: AudioBuffer) {
        AL10.alSourceQueueBuffers(id, buffer.id)
    }

    fun unqueueBuffers(buffers: Array<AudioBuffer>) {
        val bufferIds = IntArray(buffers.size)
        for (i in buffers.indices) bufferIds[i] = buffers[i].id
        AL10.alSourceUnqueueBuffers(id, bufferIds)
    }

    fun unqueueBuffers() {
        AL10.alSourceUnqueueBuffers(id)
    }

    fun unqueueBuffer(buffer: AudioBuffer) {
        AL10.alSourceUnqueueBuffers(buffer.id)
    }

    val queuedBufferCount: Int
        get() = AL10.alGetSourcei(id, AL10.AL_BUFFERS_QUEUED)
    val processedBufferCount: Int
        get() = AL10.alGetSourcei(id, AL10.AL_BUFFERS_PROCESSED)

    fun setBuffer(buffer: AudioBuffer) {
        AL10.alSourcei(id, AL10.AL_BUFFER, buffer.id)
    }

    var isLooping: Boolean
        get() = AL10.alGetSourcei(id, AL10.AL_LOOPING) == 1
        set(loop) {
            AL10.alSourcei(id, AL10.AL_LOOPING, if (loop) AL10.AL_TRUE else AL10.AL_FALSE)
        }
    val volume: Float
        get() = AL10.alGetSourcef(id, AL10.AL_GAIN)
    val pitch: Float
        get() = AL10.alGetSourcef(id, AL10.AL_PITCH)
    val position: Float
        get() = AL10.alGetSourcef(id, AL11.AL_SEC_OFFSET)

    fun setVolume(volume: Double) {
        AL10.alSourcef(id, AL10.AL_GAIN, volume.toFloat())
    }

    fun setPitch(pitch: Double) {
        AL10.alSourcef(id, AL10.AL_PITCH, pitch.toFloat())
    }

    fun setPosition(seconds: Double) {
        AL10.alSourcef(id, AL11.AL_SEC_OFFSET, seconds.toFloat())
    }

    fun setRelative(relative: Boolean) {
        AL10.alSourcei(id, AL10.AL_SOURCE_RELATIVE, if (relative) AL10.AL_TRUE else AL10.AL_FALSE)
    }

    fun setPosition(position: Vec3f) {
        AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, position.z)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        AL10.alSource3f(id, AL10.AL_POSITION, x, y, z)
    }

    fun setVelocity(speed: Vec3f) {
        AL10.alSource3f(id, AL10.AL_VELOCITY, speed.x, speed.y, speed.z)
    }

    fun setVelocity(x: Float, y: Float, z: Float) {
        AL10.alSource3f(id, AL10.AL_VELOCITY, x, y, z)
    }

    fun setPan(pan: Float) {
        AL10.alSource3f(
            id,
            AL10.AL_POSITION,
            Mathc.cos((pan - 1) * Math.PI / 2),
            0f,
            Mathc.sin((pan + 1) * Math.PI / 2)
        )
    }

    val isPlaying: Boolean
        get() = state == AL10.AL_PLAYING
    val isPaused: Boolean
        get() = state == AL10.AL_PAUSED
    val isStopped: Boolean
        get() = state == AL10.AL_STOPPED
    val isInitial: Boolean
        get() = state == AL10.AL_INITIAL
    private val state: Int
        private get() = AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE)

    fun play() {
        AL10.alSourcePlay(id)
    }

    fun pause() {
        AL10.alSourcePause(id)
    }

    fun stop() {
        AL10.alSourceStop(id)
    }

    override fun dispose() {
        AL10.alDeleteSources(id)
    }
}
