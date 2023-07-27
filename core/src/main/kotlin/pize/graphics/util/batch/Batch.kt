package pize.graphics.util.batch

import pize.app.Disposable
import pize.graphics.texture.Region
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.color.Color
import pize.graphics.util.color.IColor
import pize.math.vecmath.vector.Vec2f

abstract class Batch : Disposable {
    protected val color: Color

    init {
        color = Color()
    }

    abstract fun draw(texture: Texture, x: Float, y: Float, width: Float, height: Float)
    abstract fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float)
    abstract fun draw(texture: Texture?, x: Float, y: Float, width: Float, height: Float, region: Region)
    abstract fun draw(texReg: TextureRegion, x: Float, y: Float, width: Float, height: Float, region: Region)
    fun resetColor() {
        color[1f, 1f, 1f] = 1f
    }

    fun setColor(color: IColor) {
        this.color.set(color)
    }

    fun setColor(r: Double, g: Double, b: Double, a: Double) {
        color[r, g, b] = a
    }

    fun getColor(): Color {
        return color
    }

    fun setAlpha(a: Double) {
        color.setA(a.toFloat())
    }

    abstract val transformOrigin: Vec2f
    abstract fun setTransformOrigin(x: Double, y: Double)
    abstract fun rotate(angle: Float)
    abstract fun shear(angleX: Float, angleY: Float)
    abstract fun scale(scale: Float)
    abstract fun scale(x: Float, y: Float)
    abstract fun flip(x: Boolean, y: Boolean)
    abstract fun size(): Int

    companion object {
        @JvmField
        var QUAD_INDICES = 6
        @JvmField
        var QUAD_VERTICES = 4
    }
}
