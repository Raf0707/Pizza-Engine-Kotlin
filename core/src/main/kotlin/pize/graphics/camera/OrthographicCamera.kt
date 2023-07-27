package pize.graphics.camera

import pize.Pize
import pize.app.Resizable
import pize.math.vecmath.matrix.Matrix4f

class OrthographicCamera @JvmOverloads constructor(width: Int = Pize?.width!!, height: Int = Pize?.height!!) :
    Camera2D(width, height), Resizable {
    override var scale = 1f
    override var rotation = 0f
        private set
    override val projection: Matrix4f?
    override val view: Matrix4f
    private val scalingMatrix: Matrix4f
    private val translationMatrix: Matrix4f
    private val rotationMatrix: Matrix4f
    private var dirtyProjection = false
    private var imaginaryX = false
    private var imaginaryY = false

    init {
        view = Matrix4f()
        projection = Matrix4f().toOrthographic(0f, 0f, width.toFloat(), height.toFloat())
        scalingMatrix = Matrix4f()
        translationMatrix = Matrix4f()
        rotationMatrix = Matrix4f()
    }

    override fun update() {
        if (dirtyProjection) {
            projection!!.identity().toOrthographic(0f, 0f, width.toFloat(), height.toFloat())
            dirtyProjection = false
        }
        translationMatrix.toTranslated(
            (if (imaginaryX) 0 else -position.x) as Float,
            (if (imaginaryY) 0 else -position.y) as Float,
            0f
        )
        scalingMatrix.toScaled(scale, scale, 1f)
        rotationMatrix.toRotatedZ(rotation.toDouble())
        view.set(scalingMatrix.mul(translationMatrix).mul(rotationMatrix))
    }

    override fun resize(width: Int, height: Int) {
        if (match(width, height)) return
        setSize(width, height)
        dirtyProjection = true
    }

    fun setImaginaryOrigins(x: Boolean, y: Boolean) {
        imaginaryX = x
        imaginaryY = y
    }

    fun scale(scale: Float) {
        this.scale *= scale
    }

    fun rotate(deg: Float) {
        rotation += deg
    }

    fun setRot(deg: Float) {
        rotation = deg
    }

    fun get(): Matrix4f? {
        return view
    }
}
