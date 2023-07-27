package pize.tests.minecraft.game.resources

import pize.audio.sound.Sound
import pize.tests.minecraft.utils.log.Logger

class MusicResource(location: String) : Resource<Sound?>(location) {
    override var resource: Sound? = null
        private set
    override var isLoaded = false
        private set

    override fun loadResource() {
        try {
            resource = Sound(location)
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
