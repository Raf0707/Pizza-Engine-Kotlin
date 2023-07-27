package pize.graphics.texture

class TextureRegion : Region {
    @JvmField
    var texture: Texture? = null

    constructor(textureRegion: TextureRegion) {
        setTextureRegion(textureRegion)
    }

    constructor(texture: Texture?, x: Int, y: Int, width: Int, height: Int) {
        setTextureRegion(texture, x, y, width, height)
    }

    constructor(texture: Texture?, u1: Double, v1: Double, u2: Double, v2: Double) {
        setTextureRegion(texture, u1, v1, u2, v2)
    }

    constructor(texture: Texture?, region: Region) {
        setTextureRegion(texture, region)
    }

    constructor(texture: Texture?) {
        setTextureRegion(texture, 0.0, 0.0, 1.0, 1.0)
    }

    constructor(texture: TextureRegion, x: Int, y: Int, width: Int, height: Int) {
        setTextureRegion(texture, x, y, width, height)
    }

    constructor(texture: TextureRegion, u1: Double, v1: Double, u2: Double, v2: Double) {
        setTextureRegion(texture, u1, v1, u2, v2)
    }

    constructor(texture: TextureRegion, region: Region) {
        setTextureRegion(texture, region)
    }

    fun setTextureRegion(textureRegion: TextureRegion) {
        texture = textureRegion.texture
        super.set(textureRegion.u1, textureRegion.v1, textureRegion.u2, textureRegion.v2)
    }

    fun setTextureRegion(texture: Texture?, x: Int, y: Int, width: Int, height: Int) {
        this.texture = texture
        setRegion(x, y, width, height)
    }

    fun setTextureRegion(texture: Texture?, u1: Double, v1: Double, u2: Double, v2: Double) {
        this.texture = texture
        setRegion(u1, v1, u2, v2)
    }

    fun setTextureRegion(texture: Texture?, region: Region) {
        this.texture = texture
        setRegion(region)
    }

    fun setTextureRegion(textureRegion: TextureRegion, x: Int, y: Int, width: Int, height: Int) {
        texture = textureRegion.texture
        setRegion(Region.Companion.calcRegionInRegion(textureRegion, calcFromRect(x, y, width, height, texture)))
    }

    fun setTextureRegion(textureRegion: TextureRegion, u1: Double, v1: Double, u2: Double, v2: Double) {
        texture = textureRegion.texture
        setRegion(Region.Companion.calcRegionInRegion(textureRegion, u1, v1, u2, v2))
    }

    fun setTextureRegion(textureRegion: TextureRegion, region: Region) {
        texture = textureRegion.texture
        setRegion(Region.Companion.calcRegionInRegion(textureRegion, region))
    }

    fun setRegion(x: Int, y: Int, width: Int, height: Int) {
        setRegion(calcFromRect(x, y, width, height, texture))
    }

    fun setRegion(u1: Double, v1: Double, u2: Double, v2: Double) {
        super.set(u1, v1, u2, v2)
    }

    fun setRegion(region: Region) {
        super.set(region)
    }

    val widthPx: Float
        get() = getWidthPx(texture)
    val heightPx: Float
        get() = getHeightPx(texture)

    override fun copy(): TextureRegion {
        return TextureRegion(this)
    }

    companion object {
        fun calcFromRect(x: Int, y: Int, width: Int, height: Int, texture: Texture?): Region {
            return Region(
                x.toDouble() / texture.getWidth(),
                y.toDouble() / texture.getHeight(),
                (x + width).toDouble() / texture.getWidth(),
                (y + height).toDouble() / texture.getHeight()
            )
        }
    }
}
