package pize.graphics.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import pize.graphics.gl.*
import pize.graphics.texture.PixmapIO.load

class CubeMap(px: String?, nx: String?, py: String?, ny: String?, pz: String?, nz: String?) :
    GlTexture(GL13.GL_TEXTURE_CUBE_MAP) {
    init {
        bind()
        parameters.setFilter(Filter.LINEAR)
        val pixmapPx: Pixmap = load(px!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            pixmapPx.buffer,
            pixmapPx?.width!!,
            pixmapPx?.height!!
        )
        val pixmapNx: Pixmap = load(nx!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            pixmapNx.buffer,
            pixmapNx?.width!!,
            pixmapNx?.height!!
        )
        val pixmapPy: Pixmap = load(py!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            pixmapPy.buffer,
            pixmapPy?.width!!,
            pixmapPy?.height!!
        )
        val pixmapNy: Pixmap = load(ny!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            pixmapNy.buffer,
            pixmapNy?.width!!,
            pixmapNy?.height!!
        )
        val pixmapPz: Pixmap = load(pz!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            pixmapPz.buffer,
            pixmapPz?.width!!,
            pixmapPz?.height!!
        )
        val pixmapNz: Pixmap = load(nz!!)
        parameters.texImage2D(
            GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,
            pixmapNz.buffer,
            pixmapNz?.width!!,
            pixmapNz?.height!!
        )
        parameters.use(GL13.GL_TEXTURE_CUBE_MAP)
        genMipMap()
    }

    companion object {
        fun unbind() {
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0)
        }
    }
}
