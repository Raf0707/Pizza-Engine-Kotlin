package pize.graphics.util

import pize.files.Resource
import pize.graphics.camera.Camera
import pize.graphics.util.color.IColor
import pize.math.vecmath.matrix.Matrix4f

class BaseShader private constructor(path: String) : Shader(
    Resource("shader/base/$path.vert"), Resource(
        "shader/base/$path.frag"
    )
) {
    fun setMatrices(projection: Matrix4f?, view: Matrix4f?) {
        setUniform("u_projection", projection)
        setUniform("u_view", view)
    }

    fun setMatrices(camera: Camera) {
        setMatrices(camera.getProjection(), camera.getView())
    }

    fun setColor(color: IColor?) {
        setUniform("u_color", color!!)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        setUniform("u_color", r, g, b, a)
    }

    fun setColor(r: Float, g: Float, b: Float) {
        setColor(r, g, b, 1f)
    }

    companion object {
        private var pos2Color: BaseShader? = null

        /** Attributes: vec2 POSITION, vec4 COLOR  */
        fun getPos2Color(): BaseShader? {
            if (pos2Color == null) pos2Color = BaseShader("pos2-color")
            return pos2Color
        }

        private var pos3Color: BaseShader? = null

        /** Attributes: vec3 POSITION, vec4 COLOR  */
        fun getPos3Color(): BaseShader? {
            if (pos3Color == null) pos3Color = BaseShader("pos3-color")
            return pos3Color
        }

        private var pos3UColor: BaseShader? = null

        /** Attributes: vec3 POSITION; Uniforms: vec4 COLOR  */
        @JvmStatic
        fun getPos3UColor(): BaseShader? {
            if (pos3UColor == null) pos3UColor = BaseShader("pos3-ucolor")
            return pos3UColor
        }

        private fun disposeShaders() { // Invoked from Context
            if (pos2Color != null) pos2Color!!.dispose()
            if (pos3Color != null) pos3Color!!.dispose()
            if (pos3UColor != null) pos3UColor!!.dispose()
        }
    }
}
