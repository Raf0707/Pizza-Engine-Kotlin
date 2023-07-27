package pize.graphics.util

import pize.Pize
import pize.app.Disposable
import pize.graphics.camera.OrthographicCamera
import pize.graphics.gl.Gl
import pize.graphics.gl.Target
import pize.graphics.texture.Pixmap
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch

class Canvas @JvmOverloads constructor(width: Int = Pize.getWidth(), height: Int = Pize.getHeight()) :
    Pixmap(width, height), Disposable {
    private val batch: TextureBatch
    private val camera: OrthographicCamera
    private val frameTexture: Texture

    init {
        batch = TextureBatch()
        camera = OrthographicCamera()
        batch.flip(false, true)
        frameTexture = Texture(this)
    }

    fun render() {
        val cullFace = Gl.isEnabled(Target.CULL_FACE)
        if (cullFace) Gl.disable(Target.CULL_FACE)
        frameTexture.setPixmap(this)
        camera.update()
        batch.begin(camera)
        batch.draw(frameTexture, 0f, 0f, Pize.getWidth().toFloat(), Pize.getHeight().toFloat())
        batch.end()
        if (cullFace) Gl.enable(Target.CULL_FACE)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        frameTexture.resize(width, height)
        camera.resize(width, height)
    }

    override fun dispose() {
        batch.dispose()
        frameTexture.dispose()
    }
}
