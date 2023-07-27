package pize.devtests

import pize.Pize.height
import pize.Pize.width
import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.atlas.TextureAtlas
import pize.graphics.util.batch.TextureBatch

class AtlasTest : AppAdapter() {
    var batch: TextureBatch? = null
    var atlas: TextureAtlas<Int>? = null
    override fun init() {
        batch = TextureBatch()
        atlas = TextureAtlas()
        for (i in 1..25) atlas!!.put(i, "texture$i.png")
        atlas!!.generate(128, 128, 1)
    }

    override fun render() {
        clearColorBuffer()
        clearColor(0.4, 0.5, 0.7)
        batch!!.begin()
        batch!!.draw(atlas!!.texture!!, 0f, 0f, width.toFloat(), height.toFloat())
        batch!!.end()
    }
}
