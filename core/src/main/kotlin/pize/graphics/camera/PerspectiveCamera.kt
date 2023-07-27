package pize.graphics.camera

import pize.Pize
import pize.app.Resizable
import pize.math.util.Frustum
import pize.math.vecmath.matrix.Matrix4f

open class PerspectiveCamera(width: Int, height: Int, near: Double, far: Double, fieldOfView: Double) :
    Camera3D(width, height), Resizable {
    private var fieldOfView: Float
    override var near: Float = 0.0f
    override var far: Float = 0.0f
    override val projection: Matrix4f?
    override val view: Matrix4f?
    @JvmField
    val imaginaryView: Matrix4f?
    @JvmField
    val frustum: Frustum
    private var dirtyProjection = false
    private var imaginaryX = false
    private var imaginaryY = false
    private var imaginaryZ = false

    constructor(near: Double, far: Double, fieldOfView: Double) : this(
        Pize?.width!!,
        Pize?.height!!,
        near,
        far,
        fieldOfView
    )

    init {
        this.near = near.toFloat()
        this.far = far.toFloat()
        this.fieldOfView = fieldOfView.toFloat()
        view =
            Matrix4f().toLookAt(position, rotation.direction).mul(Matrix4f().toRotatedZ(rotation.roll.toDouble()))
        projection = Matrix4f().toPerspective(width.toFloat(), height.toFloat(), this.near, this.far, this.fieldOfView)
        imaginaryView = Matrix4f().set(view)
        frustum = Frustum(view, projection)
    }

    override fun update() {
        if (dirtyProjection) {
            projection!!.toPerspective(width.toFloat(), height.toFloat(), near, far, fieldOfView)
            dirtyProjection = false
        }
        view!!.toLookAt(
            (if (imaginaryX) 0 else position.x) as Float,
            (if (imaginaryY) 0 else position.y) as Float,
            (if (imaginaryZ) 0 else position.z) as Float,
            rotation.direction
        )
        imaginaryView!!.toLookAt(position, rotation.direction).mul(Matrix4f().toRotatedZ(rotation.roll.toDouble()))
        frustum.setFrustum(
            if (!(imaginaryX || imaginaryY || imaginaryZ)) view else imaginaryView,
            projection
        )
    }

    override fun resize(width: Int, height: Int) {
        if (match(width, height)) return
        setSize(width, height)
        dirtyProjection = true
    }

    fun setImaginaryOrigins(x: Boolean, y: Boolean, z: Boolean) {
        imaginaryX = x
        imaginaryY = y
        imaginaryZ = z
    }

    override var fov: Float
        get() = fieldOfView
        set(fieldOfView) {
            if (this.fieldOfView == fieldOfView) return
            this.fieldOfView = fieldOfView
            dirtyProjection = true
        }

    fun getNear(): Float {
        return near
    }

    fun setNear(near: Float) {
        if (this.near == near) return
        this.near = near
        dirtyProjection = true
    }

    fun getFar(): Float {
        return far
    }

    fun setFar(far: Float) {
        if (this.far == far) return
        this.far = far
        dirtyProjection = true
    }
}
