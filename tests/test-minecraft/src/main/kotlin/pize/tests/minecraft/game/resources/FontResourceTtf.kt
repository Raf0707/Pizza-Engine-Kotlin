package pize.tests.minecraft.game.resources

import pize.graphics.font.BitmapFont
import pize.graphics.font.FontCharset
import pize.graphics.font.FontLoader.loadTrueType
import pize.tests.minecraft.utils.log.Logger

class FontResourceTtf @JvmOverloads constructor(
    location: String,
    private val size: Int,
    private val charset: FontCharset = FontCharset.DEFAULT
) : Resource<BitmapFont?>(location) {
    override var resource: BitmapFont? = null
        private set
    override var isLoaded = false
        private set

    override fun loadResource() {
        try {
            resource = loadTrueType(location, size, charset)
            isLoaded = true
        } catch (e: Throwable) {
            isLoaded = false
            Logger.Companion.instance().error(e.localizedMessage)
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