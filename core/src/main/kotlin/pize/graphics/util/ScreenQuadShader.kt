package pize.graphics.util

import pize.files.Resource
import pize.graphics.texture.Texture

class ScreenQuadShader {
    private val shader: Shader

    init {
        shader = Shader(Resource("shader/screen/screen.vert"), Resource("shader/screen/screen.frag"))
    }

    companion object {
        private var instance: ScreenQuadShader? = null
        fun use(texture: Texture?) {
            if (instance == null) instance = ScreenQuadShader()
            instance!!.shader.bind()
            instance!!.shader.setUniform("u_texture", texture)
        }

        private fun dispose() { // Invoked from Context
            if (instance != null) instance!!.shader.dispose()
        }
    }
}
