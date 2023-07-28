package pize.graphics.camera

import pize.app.Resizable
import pize.graphics.texture.Sizable
import pize.math.vecmath.matrix.Matrix4f

abstract class Camera(width: Int, height: Int) : Sizable(width, height), Resizable {

    abstract fun update()
    abstract val view: Matrix4f?
    abstract val projection: Matrix4f?

}
