package pize.tests.terraria.indev

import pize.graphics.texture.Texture

class TileTextures private constructor(private val texture: Texture?) {
    companion object {
        fun generate(texture: Texture?, type: TextureType): TileTextures {
            val tileTextures = TileTextures(texture)
            if (type == TextureType.TILE_DEFAULT) {
            }
            return tileTextures
        }
    }
}
