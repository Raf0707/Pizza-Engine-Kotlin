package pize.tests.minecraft.game.resources

import pize.graphics.font.BitmapFont
import pize.graphics.font.FontLoader.loadFnt
import pize.tests.minecraft.utils.log.Logger

class FontResourceFnt(location: String) : Resource<BitmapFont?>(location) {
    override var resource: BitmapFont? = null
        private set
    override var isLoaded = false
        private set

    override fun loadResource() {
        try {
            resource = loadFnt(location)
            isLoaded = true
        } catch (e: RuntimeException) {
            isLoaded = false
            Logger.Companion.instance().warn(e.localizedMessage)
        }
    }

    override fun reloadResource() {
        val oldFont = resource
        loadResource()
        oldFont!!.dispose()
    }

    override fun dispose() {
        resource!!.dispose()
    }
}