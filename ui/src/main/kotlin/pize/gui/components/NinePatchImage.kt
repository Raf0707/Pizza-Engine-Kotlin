package pize.gui.components

import pize.graphics.texture.Region
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.Batch
import pize.graphics.util.batch.TextureBatch
import pize.gui.UIComponent
import pize.math.Maths.floor
import pize.math.vecmath.vector.Vec2f
import kotlin.math.max

class NinePatchImage(textureRegion: TextureRegion, mesh: RegionMesh?) : UIComponent<TextureBatch>() {
    private val regions: Array<TextureRegion?>
    private var texture: TextureRegion? = null
    private var mesh: RegionMesh? = null
    var pixelSize = 0f
        private set
    var expandType: ExpandType? = null

    init {
        regions = arrayOfNulls(9)
        setTexture(textureRegion, mesh)
        expandType = ExpandType.DEFAULT
    }

    constructor(texture: Texture?, mesh: RegionMesh?) : this(TextureRegion(texture), mesh)

    fun setTexture(texture: TextureRegion, mesh: RegionMesh?) {
        if (this.texture == texture) return
        this.texture = texture
        this.mesh = mesh
        for (i in 0..8) {
            regions[i] = TextureRegion(
                texture,
                (
                        mesh!!.mesh[i % 3] / texture.widthPx).toDouble(),
                (
                        mesh.mesh[4 + i / 3] / texture.heightPx).toDouble(),
                (
                        mesh.mesh[i % 3 + 1] / texture.widthPx).toDouble(),
                (
                        mesh.mesh[4 + i / 3 + 1] / texture.heightPx
                        ).toDouble()
            )
        }
    }

    fun setTexture(texture: TextureRegion) {
        setTexture(texture, mesh)
    }

    fun setTexture(texture: Texture?, mesh: RegionMesh?) {
        setTexture(TextureRegion(texture), mesh)
    }

    fun setTexture(texture: Texture?) {
        setTexture(texture, mesh)
    }

    override fun render(batch: TextureBatch, x: Float, y: Float, width: Float, height: Float) {
        // Calc Render Info
        pixelSize =
            if (expandType == ExpandType.HORIZONTAL) height / texture!!.heightPx else if (expandType == ExpandType.VERTICAL) width / texture!!.widthPx else if (width > height * texture!!.aspect()) height / texture!!.heightPx else width / texture!!.widthPx
        val cornerLeftBottomSize = getElementSize(0, 2)
        val cornerRightUpSize = getElementSize(2, 0)
        val centerElementSize = getElementSize(1, 1)
        val elementCountXF = (width - cornerLeftBottomSize.x - cornerRightUpSize.x) / centerElementSize.x
        val elementCountYF = (height - cornerLeftBottomSize.y - cornerRightUpSize.y) / centerElementSize.y
        val elementCountX = if (expandType == ExpandType.VERTICAL) 1 else max(
            (if (expandType == ExpandType.HORIZONTAL) 0 else 1).toDouble(),
            floor(elementCountXF.toDouble()).toDouble()
        ).toInt()
        val elementCountY = if (expandType == ExpandType.HORIZONTAL) 1 else max(
            (if (expandType == ExpandType.VERTICAL) 0 else 1).toDouble(),
            floor(elementCountYF.toDouble()).toDouble()
        ).toInt()
        val countDifferenceX = elementCountXF - elementCountX
        val countDifferenceY = elementCountYF - elementCountY

        // Draw corners
        renderRegion(
            batch, 0, 2,
            x,
            y,
            1f, 1f
        )
        renderRegion(
            batch, 2, 2,
            x + width - cornerRightUpSize.x,
            y,
            1f, 1f
        )
        renderRegion(
            batch, 2, 0,
            x + width - cornerRightUpSize.x,
            y + height - cornerRightUpSize.y,
            1f, 1f
        )
        renderRegion(
            batch, 0, 0,
            x,
            y + height - cornerRightUpSize.y,
            1f, 1f
        )

        // Draw Borders X
        var borderDrawOffsetX = 0f
        for (i in 0 until elementCountX) {
            val elementSize = renderRegion(
                batch, 1, 2,
                x + borderDrawOffsetX + cornerLeftBottomSize.x,
                y,
                1f, 1f
            )
            renderRegion(
                batch, 1, 0,
                x + borderDrawOffsetX + cornerLeftBottomSize.x,
                y + height - cornerRightUpSize.y,
                1f, 1f
            )
            borderDrawOffsetX += elementSize.x
        }
        if (countDifferenceX > 0) {
            renderRegion(
                batch, 1, 2,
                x + borderDrawOffsetX + cornerLeftBottomSize.x,
                y,
                countDifferenceX, 1f
            )
            renderRegion(
                batch, 1, 0,
                x + borderDrawOffsetX + cornerLeftBottomSize.x,
                y + height - cornerRightUpSize.y,
                countDifferenceX, 1f
            )
        }

        // Draw Borders Y
        var borderDrawOffsetY = 0f
        for (j in 0 until elementCountY) {
            val elementSize = renderRegion(
                batch, 0, 1,
                x,
                y + borderDrawOffsetY + cornerLeftBottomSize.y,
                1f, 1f
            )
            renderRegion(
                batch, 2, 1,
                x + width - cornerRightUpSize.x,
                y + borderDrawOffsetY + cornerLeftBottomSize.y,
                1f, 1f
            )
            borderDrawOffsetY += elementSize.y
        }
        if (countDifferenceY > 0) {
            renderRegion(
                batch, 0, 1,
                x,
                y + borderDrawOffsetY + cornerLeftBottomSize.y,
                1f, countDifferenceY
            )
            renderRegion(
                batch, 2, 1,
                x + width - cornerRightUpSize.x,
                y + borderDrawOffsetY + cornerLeftBottomSize.y,
                1f, countDifferenceY
            )
        }

        // Draw center
        var centerDrawOffsetX = 0f
        var centerDrawOffsetY = 0f
        for (j in 0 until elementCountY) {
            centerDrawOffsetX = 0f
            var elementSize = Vec2f()
            for (i in 0 until elementCountX) {
                elementSize = renderRegion(
                    batch, 1, 1,
                    x + centerDrawOffsetX + cornerLeftBottomSize.x,
                    y + centerDrawOffsetY + cornerLeftBottomSize.y,
                    1f, 1f
                )
                centerDrawOffsetX += elementSize.x
            }
            centerDrawOffsetY += elementSize.y
        }
        if (countDifferenceX > 0) renderRegion(
            batch, 1, 1,
            x + centerDrawOffsetX + cornerLeftBottomSize.x,
            y + cornerLeftBottomSize.y,
            countDifferenceX, 1f
        )
        if (countDifferenceY > 0) renderRegion(
            batch, 1, 1,
            x + cornerLeftBottomSize.x,
            y + centerDrawOffsetY + cornerLeftBottomSize.y,
            1f, countDifferenceY
        )
    }

    // PRIVATE
    private fun renderRegion(batch: Batch, i: Int, j: Int, x: Float, y: Float, u: Float, v: Float): Vec2f {
        val size = getElementSize(i, j)
        batch.draw(getRegion(i, j)!!, x, y, size.x * u, size.y * v, Region(0f, 1 - v, u, 1f))
        return size
    }

    private fun getElementSize(i: Int, j: Int): Vec2f {
        val region = getRegion(i, j)
        return Vec2f(
            region!!.widthPx * pixelSize,
            region.heightPx * pixelSize
        )
    }

    private fun getRegion(i: Int, j: Int): TextureRegion? {
        return regions[j * 3 + i]
    }
}