package pize.tests.minecraft.game.resources

import pize.app.Disposable
import pize.audio.sound.AudioBuffer
import pize.audio.sound.Sound
import pize.graphics.font.BitmapFont
import pize.graphics.font.FontCharset
import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.tests.minecraft.utils.log.Logger
import java.awt.Rectangle

class ResourceManager : Disposable {
    var location: String? = null
    private val resources: MutableMap<String?, Resource<*>>
    private val unknownTexture: TextureRegion

    init {
        resources = HashMap()
        val unknownPixmap = Pixmap(2, 2)
        unknownPixmap.clear(1.0, 0.0, 1.0, 1.0)
        unknownPixmap.setPixel(0, 0, 0.0, 0.0, 0.0, 1.0)
        unknownPixmap.setPixel(1, 1, 0.0, 0.0, 0.0, 1.0)
        unknownTexture = TextureRegion(Texture(unknownPixmap))
    }

    fun getTexture(id: String?): TextureRegion? {
        val resource = resources[id] ?: return unknownTexture
        return (if (resource.isLoaded()) resource.getResource() else unknownTexture)
    }

    @JvmOverloads
    fun putTexture(id: String?, location: String, region: Rectangle? = null) {
        resources[id] = TextureResource(this.location + location, region)
    }

    fun getSound(id: String?): AudioBuffer? {
        return resources[id].getResource() as AudioBuffer
    }

    fun putSound(id: String?, location: String) {
        resources[id] = SoundResource(this.location + location)
    }

    fun getMusic(id: String?): Sound? {
        return resources[id].getResource() as Sound
    }

    fun putMusic(id: String?, location: String) {
        resources[id] = MusicResource(this.location + location)
    }

    fun getFont(id: String?): BitmapFont? {
        return resources[id].getResource() as BitmapFont
    }

    fun putFontFnt(id: String?, location: String) {
        resources[id] = FontResourceFnt(this.location + location)
    }

    fun putFontTtf(id: String?, location: String, size: Int) {
        resources[id] = FontResourceTtf(this.location + location, size)
    }

    fun putFontTtf(id: String?, location: String, size: Int, charset: FontCharset) {
        resources[id] = FontResourceTtf(this.location + location, size, charset)
    }

    fun load() {
        for (resource in resources.values) {
            Logger.Companion.instance().info("Load " + resource.javaClass.getSimpleName())
            resource.loadResource()
        }
    }

    fun reload() {
        for (resource in resources.values) if (resource.isLoaded()) resource.reloadResource() else resource.loadResource()
    }

    override fun dispose() {
        for (resource in resources.values) if (resource.isLoaded()) resource.dispose()
        unknownTexture.texture!!.dispose()
    }
}
