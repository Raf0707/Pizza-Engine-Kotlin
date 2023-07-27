package pize.tests.minecraft.game.resources

import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.tests.minecraft.utils.log.Logger
import java.awt.Rectangle

class TextureResource @JvmOverloads constructor(location: String, private var region: Rectangle? = null) :
    Resource<TextureRegion?>(location) {
    override var resource: TextureRegion? = null
        private set
    override var isLoaded = false
        private set

    override fun loadResource() {
        try {
            if (region == null) resource = TextureRegion(Texture(location)) else resource = TextureRegion(
                Texture(
                    location
                ),
                region!!.getX().toInt(),
                region!!.getY().toInt(),
                region!!.getWidth().toInt(),
                region!!.getHeight().toInt()
            )
            isLoaded = true
        } catch (e: RuntimeException) {
            isLoaded = false
            Logger.Companion.instance().warn(e.localizedMessage)
        }
    }

    override fun reloadResource() {
        val oudTexture = resource
        loadResource()
        oudTexture!!.texture!!.dispose()
    }

    fun setRegion(region: Rectangle?) {
        this.region = region
    }

    override fun dispose() {
        resource!!.texture!!.dispose()
    }
}
