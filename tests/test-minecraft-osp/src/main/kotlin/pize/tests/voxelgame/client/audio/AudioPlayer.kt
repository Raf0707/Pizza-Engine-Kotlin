package pize.tests.voxelgame.client.audio

import pize.app.Disposable
import pize.audio.sound.AudioBuffer
import pize.audio.sound.AudioSource
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.main.audio.Sound

class AudioPlayer(private val session: Minecraft) : Disposable {
    private val sources: Array<AudioSource?>

    init {
        println("[Client]: Initialize audio player")
        sources = arrayOfNulls(256)
        for (i in sources.indices) sources[i] = AudioSource()
    }

    fun play(sound: Sound, volume: Float, pitch: Float, x: Float, y: Float, z: Float) {
        println("Play: " + sound.id)
        play(session.resources.getSound(sound.id), volume, pitch, x, y, z)
    }

    fun play(soundID: String?, volume: Float, pitch: Float, x: Float, y: Float, z: Float) {
        println("Play: $soundID")
        play(session.resources.getSound(soundID), volume, pitch, x, y, z)
    }

    fun play(buffer: AudioBuffer?, volume: Float, pitch: Float, x: Float, y: Float, z: Float) {
        if (buffer == null) return
        val sourceIndex = firstFreeIndex
        if (sourceIndex == -1) return
        val source = sources[sourceIndex]
        source!!.setBuffer(buffer)
        source.setVolume(volume.toDouble())
        source.setPitch(pitch.toDouble())
        source.setPosition(x, y, z)
        source.play()
    }

    private val firstFreeIndex: Int
        private get() {
            for (i in sources.indices) if (!sources[i]!!.isPlaying) return i
            return -1
        }

    override fun dispose() {
        for (source in sources) source!!.dispose()
    }
}
