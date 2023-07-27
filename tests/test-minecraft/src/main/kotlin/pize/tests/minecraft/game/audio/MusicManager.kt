package pize.tests.minecraft.game.audio

import pize.app.Disposable
import pize.audio.sound.Sound
import pize.math.Maths.random
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.game.options.SoundCategory
import pize.util.Utils.delayMillis
import pize.util.time.PizeRunnable

class MusicManager(private val session: Session) : Disposable {
    private var currentSet: MusicSet? = null
    private var currentIndex = 0
    private var current: Sound? = null

    init {
        setCurrentSet(MusicSet.MAIN_MENU)
    }

    fun updateVolumeForPlaying() {
        if (current != null) current!!.setVolume(volume.toDouble())
    }

    private val volume: Float
        private get() = (session.options.getSoundVolume(SoundCategory.MUSIC)
                * session.options.getSoundVolume(SoundCategory.MASTER))

    fun setCurrentSet(set: MusicSet?) {
        currentSet = set
        if (set == null) return
        currentIndex = random(currentSet!!.size() - 1)
        play()
    }

    private fun play() {
        if (current != null) current!!.stop()
        current = session.resourceManager.getMusic(currentSet!![currentIndex])
        if (current == null) return
        current!!.setVolume(volume.toDouble())
        current!!.play()
        PizeRunnable {
            delayMillis(1)
            next()
        }.runLaterAsync((current!!.duration * 1000).toLong())
    }

    private fun next() {
        currentIndex++
        if (currentIndex >= currentSet!!.size()) currentIndex = 0
        play()
    }

    override fun dispose() {
        current!!.dispose()
    }
}
