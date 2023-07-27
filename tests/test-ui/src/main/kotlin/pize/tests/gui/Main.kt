package pize.tests.gui

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.run
import pize.Pize.window
import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureRegion
import pize.graphics.util.batch.TextureBatch
import pize.gui.Align
import pize.gui.LayoutType
import pize.gui.UIComponent
import pize.gui.components.ExpandType
import pize.gui.components.Layout
import pize.gui.components.NinePatchImage
import pize.gui.components.RegionMesh
import pize.gui.constraint.Constraint.Companion.relative
import pize.io.glfw.Key

class Main : AppAdapter() {
    private var batch: TextureBatch? = null
    private var texture: Texture? = null
    private var layout: Layout? = null
    override fun init() {
        batch = TextureBatch()
        texture = Texture("widgets.png")
        val buttonTextureRegion = TextureRegion(texture, 0, 66, 200, 20)
        val regionMesh = RegionMesh(0f, 0f, 2f, 2f, 198f, 17f, 200f, 20f)

        // UI
        layout = Layout()
        layout!!.setLayoutType(LayoutType.HORIZONTAL)
        layout!!.alignItems(Align.CENTER)
        val button = NinePatchImage(buttonTextureRegion, regionMesh)
        button.expandType = ExpandType.HORIZONTAL
        button.setSize(relative(0.333), relative(0.333))
        layout!!.put("button", button as UIComponent<*>)
    }

    override fun render() {
        if (Key.ESCAPE.isDown) exit()
        clearColorBuffer()
        clearColor(0.08, 0.11, 0.15, 1.0)
        batch!!.begin()
        layout!!.render(batch) // render layout
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        texture!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Test - UI", 480, 360)
            window()!!.setIcon("icon.png")
            run(Main())
        }
    }
}