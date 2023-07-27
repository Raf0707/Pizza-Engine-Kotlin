package pize.tests.minecraft.game.resources

import pize.audio.sound.AudioBuffer
import pize.tests.minecraft.utils.log.Logger

class SoundResource(location: String) : Resource<AudioBuffer?>(location) {
    override var resource: AudioBuffer? = null
        private set
    override var isLoaded = false
        private set

    override fun loadResource() {
        try {
            resource = AudioBuffer(location)
            isLoaded = true
        } catch (e: RuntimeException) {
            isLoaded = false
            Logger.Companion.instance().warn(e.localizedMessage)
        }
    }

    override fun reloadResource() {
        val oldSound = resource
        loadResource()
        oldSound!!.dispose()
    }

    override fun dispose() {
        resource!!.dispose()
    }
}
