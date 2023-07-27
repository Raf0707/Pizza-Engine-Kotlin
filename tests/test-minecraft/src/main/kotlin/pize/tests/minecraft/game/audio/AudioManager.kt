package pize.tests.minecraft.game.audio

import pize.Pize.audio
import pize.app.Disposable
import pize.audio.sound.AudioSource
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.options.SoundCategory

class AudioManager(private val session: Session) : Disposable {
    private val audioSources: Array<AudioSource?>

    init {
        audioSources = arrayOfNulls(MAX_SOUND_NUM)
        for (i in audioSources.indices) audioSources[i] = AudioSource()
    }

    fun play(sound: Sound, volume: Float, pitch: Float) {
        var volume = volume
        for (audioSource in audioSources) if (!audioSource!!.isPlaying) {
            audioSource.setBuffer(session.resourceManager.getSound(sound.resourceId))
            volume *= session.options.getSoundVolume(sound.category) * sound.maxVolume
            if (sound.category != SoundCategory.MASTER) volume *= session.options.getSoundVolume(SoundCategory.MASTER)
            audioSource.setVolume(volume.toDouble())
            audioSource.setPitch(pitch.toDouble())
            audioSource.play()
            return
        }
    }

    val playingNum: Int
        get() {
            var playingSoundsNum = 0
            for (audioSource in audioSources) if (audioSource!!.isPlaying) playingSoundsNum++
            return playingSoundsNum
        }

    fun setDevice(audioDevice: String?) {
        audio()!!.getDevice(audioDevice)!!.makeCurrent()
        for (audioSource in audioSources) if (audioSource!!.isPlaying) {
            audioSource.pause()
            audioSource.play()
        }
    }

    override fun dispose() {
        for (audioSource in audioSources) audioSource!!.dispose()
    }

    companion object {
        const val MAX_SOUND_NUM = 128
    }
}
