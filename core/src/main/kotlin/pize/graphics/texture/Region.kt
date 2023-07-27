package pize.graphics.texture

open class Region {
    protected var u1 = 0f
    protected var v1 = 0f
    protected var u2 = 0f
    protected var v2 = 0f
    var width = 0f
        private set
    var height = 0f
        private set

    constructor(region: Region) {
        set(region)
    }

    @JvmOverloads
    constructor(u1: Float = 0f, v1: Float = 0f, u2: Float = 1f, v2: Float = 1f) {
        set(u1, v1, u2, v2)
    }

    constructor(u1: Double, v1: Double, u2: Double, v2: Double) {
        set(u1, v1, u2, v2)
    }

    constructor(texture: Texture, x: Double, y: Double, width: Double, height: Double) {
        set(texture, x, y, width, height)
    }

    operator fun set(u1: Float, v1: Float, u2: Float, v2: Float) {
        this.u1 = u1
        this.v1 = v1
        this.u2 = u2
        this.v2 = v2
        width = u2 - u1
        height = v2 - v1
    }

    fun set(region: Region) {
        set(region.u1, region.v1, region.u2, region.v2)
    }

    operator fun set(u1: Double, v1: Double, u2: Double, v2: Double) {
        set(u1.toFloat(), v1.toFloat(), u2.toFloat(), v2.toFloat())
    }

    operator fun set(texture: Texture, x: Double, y: Double, width: Double, height: Double) {
        set(
            x / texture.width,
            y / texture.height,
            (x + width) / texture.width,
            (y + height) / texture.height
        )
    }

    fun u1(): Float {
        return u1
    }

    fun v1(): Float {
        return v1
    }

    fun u2(): Float {
        return u2
    }

    fun v2(): Float {
        return v2
    }

    fun aspect(): Float {
        return width / height
    }

    fun getWidthPx(texture: Texture?): Float {
        return width * texture?.width!!
    }

    fun getHeightPx(texture: Texture?): Float {
        return height * texture?.height!!
    }

    open fun copy(): Region {
        return Region(this)
    }

    companion object {
        fun calcRegionInRegion(region: Region, u1: Double, v1: Double, u2: Double, v2: Double): Region {
            val regionWidth = region.width
            val regionHeight = region.height
            return Region(
                region.u1 + u1 * regionWidth,
                region.v1 + v1 * regionHeight,
                region.u1 + u2 * regionWidth,
                region.v1 + v2 * regionHeight
            )
        }

        fun calcRegionInRegion(region: Region, regionOfRegion: Region): Region {
            return calcRegionInRegion(
                region,
                regionOfRegion.u1.toDouble(),
                regionOfRegion.v1.toDouble(),
                regionOfRegion.u2.toDouble(),
                regionOfRegion.v2.toDouble()
            )
        }
    }
}
