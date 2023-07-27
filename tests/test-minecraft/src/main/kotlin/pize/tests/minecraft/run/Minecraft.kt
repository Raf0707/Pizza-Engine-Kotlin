package pize.tests.minecraft.run

import pize.Pize.create
import pize.Pize.run
import pize.Pize.window
import pize.app.AppAdapter
import pize.graphics.gl.DepthFunc
import pize.graphics.gl.Gl.clearColorDepthBuffers
import pize.graphics.gl.Gl.depthFunc
import pize.graphics.gl.Gl.enable
import pize.graphics.gl.Target
import pize.tests.minecraft.game.Session
import pize.tests.minecraft.utils.log.Logger

class Minecraft : AppAdapter() {
    private var session: Session? = null
    override fun init() {
        enable(Target.DEPTH_TEST)
        depthFunc(DepthFunc.LEQUAL)
        Thread.currentThread().setName("Render Thread")
        session = Session()
    }

    override fun render() {
        clearColorDepthBuffers()
        session!!.render()
    }

    override fun resize(width: Int, height: Int) {
        session!!.resize(width, height)
    }

    override fun dispose() {
        session!!.dispose()
        Logger.Companion.instance().info("EXIT.")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Minecraft", 925, 640)
            window()!!.setIcon("icon.png")
            run(Minecraft())
        }
    }
}
