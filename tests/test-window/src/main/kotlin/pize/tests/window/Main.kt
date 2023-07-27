package pize.tests.window

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.height
import pize.Pize.run
import pize.Pize.width
import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key

class Main : AppAdapter() {
    private var batch: TextureBatch? = null
    private var texture: Texture? = null
    override fun init() {
        batch = TextureBatch()
        texture = Texture("wallpaper-19.jpg")
    }

    override fun render() {
        clearColorBuffer()
        batch!!.begin()
        batch!!.draw(texture!!, 0f, 0f, width.toFloat(), height.toFloat())
        batch!!.end()
        if (Key.ESCAPE.isPressed) exit()
    }

    override fun dispose() {
        batch!!.dispose()
        texture!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Hello, Window!", 1080, 640)
            run(Main())
        }
    }
}
