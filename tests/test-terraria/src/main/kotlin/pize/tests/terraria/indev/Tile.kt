package pize.tests.terraria.indev

import pize.graphics.texture.Texture

class Tile(id: String?, texture: Texture?, textureType: TextureType) : ITile(id) {
    private val textures: TileTextures

    init {
        textures = TileTextures.Companion.generate(texture, textureType)
    }
}
