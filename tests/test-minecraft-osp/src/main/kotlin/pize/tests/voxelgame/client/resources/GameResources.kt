package pize.tests.voxelgame.client.resources

import pize.app.Disposable
import pize.audio.sound.AudioBuffer
import pize.audio.sound.AudioLoader.load
import pize.files.Resource
import pize.graphics.texture.PixmapIO.load
import pize.graphics.texture.Region
import pize.graphics.texture.Texture
import pize.graphics.texture.atlas.TextureAtlas
import pize.tests.voxelgame.main.Identifier

class GameResources : Disposable {
    private val blockAtlas: TextureAtlas<String>
    private val blocksToLoadList: MutableList<Resource>
    private val audioList: MutableMap<String?, AudioBuffer>

    init {
        blockAtlas = TextureAtlas()
        blocksToLoadList = ArrayList()
        audioList = HashMap()
    }

    fun load() {
        println("[Client]: Build blocks atlas")
        for (resource in blocksToLoadList) {
            val name = resource.simpleName
            val pixmap = load(resource)
            blockAtlas.put(name, pixmap)
        }
        blockAtlas.generate(512, 512)
    }

    fun registerBlockRegion(resource: Resource) {
        blocksToLoadList.add(resource)
    }

    fun registerBlockRegion(path: String, name: String) {
        registerBlockRegion(Resource("$path$name.png"))
    }

    fun getBlockRegion(name: String): Region? {
        return blockAtlas.getRegion(name)
    }

    val blocks: Texture?
        get() = blockAtlas.texture

    fun registerSound(soundID: String?, resource: Resource?) {
        println("Load sound $soundID")
        val buffer = AudioBuffer()
        load(buffer, resource!!)
        audioList[soundID] = buffer
    }

    fun registerSound(soundID: String, path: String, name: String) {
        registerSound(Identifier.namespaceID(soundID), Resource("$path$name.ogg"))
    }

    fun registerSound(path: String, name: String) {
        val soundID = name.replace("/", ".")
        registerSound(soundID, path, name)
    }

    fun registerSound(path: String, name: String, min: Int, max: Int) {
        for (i in min..max) {
            val soundID = name.replace("/", ".") + "." + i
            registerSound(soundID, path, name + i)
        }
    }

    fun getSound(soundID: String?): AudioBuffer? {
        return audioList[soundID]
    }

    override fun dispose() {
        blocksToLoadList.clear()
        blockAtlas.texture!!.dispose()
        for (audio in audioList.values) audio.dispose()
    }
}
