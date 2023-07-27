package pize.tests.drift

import pize.app.Disposable
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.math.vecmath.vector.Vec2f

open class CarPart(private val texture: Texture, width: Float, height: Float) : Disposable {
    val position: Vec2f
    private val finalPosition: Vec2f
    private val size: Vec2f
    private val origin: Vec2f
    var rotation = 0f
        private set
    private var finalRotation = 0f
    private var parent: CarPart? = null

    init {
        size = Vec2f(width, height)
        position = Vec2f()
        origin = Vec2f(0.5)
        finalPosition = Vec2f()
    }

    open fun render(batch: TextureBatch) {
        if (parent != null) {
            finalRotation = rotation + parent!!.finalRotation
            finalPosition.set(position) // that position
                .rotDeg(parent!!.finalRotation.toDouble()).add(parent!!.finalPosition) // parent
        } else {
            finalRotation = rotation
            finalPosition.set(position) // that position
        }
        val renderPosition = finalPosition.copy().sub(size.copy().mul(origin)) // origin
        batch.setTransformOrigin(origin.x.toDouble(), origin.y.toDouble())
        batch.rotate(finalRotation)
        batch.draw(texture, renderPosition.x, renderPosition.y, size.x, size.y)
    }

    fun <P : CarPart?> setParent(parent: CarPart?): P {
        this.parent = parent
        return this as P
    }

    fun <P : CarPart?> setRotation(rotation: Float): P {
        this.rotation = rotation
        return this as P
    }

    fun <P : CarPart?> setOrigin(x: Double, y: Double): P {
        origin[x] = y
        return this as P
    }

    fun <P : CarPart?> setPosition(x: Double, y: Double): P {
        position[x] = y
        return this as P
    }

    fun rotate(angle: Float) {
        rotation += angle
    }

    override fun dispose() {
        texture.dispose()
    }
}
