package pize.tests.physic

import pize.Pize.create
import pize.Pize.cursorPos
import pize.Pize.exit
import pize.Pize.fPS
import pize.Pize.isTouched
import pize.Pize.run
import pize.Pize.window
import pize.app.AppAdapter
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.math.vecmath.vector.Vec2f.add
import pize.physic.BoundingBox2
import pize.physic.Collider2f.getCollidedMotion

class Main : AppAdapter() {
    private var batch: TextureBatch? = null
    private var rect1: DynamicRect? = null
    private var rect2: DynamicRect? = null
    override fun init() {
        batch = TextureBatch()
        rect1 = DynamicRect(BoundingBox2(-25.0, -25.0, 25.0, 25.0))
        rect1!!.motion().setMax(50f)
        rect2 = DynamicRect(BoundingBox2(-100.0, -100.0, 100.0, 100.0))
        rect2!!.pos().add(600f, 400f)
    }

    override fun render() {
        window()!!.title = "Physics (fps: " + fPS + ")"
        if (Key.ESCAPE.isDown) exit()
        if (Key.F11.isDown) window()!!.toggleFullscreen()
        if (Key.V.isDown) window()!!.toggleVsync()
        if (isTouched) rect1!!.motion().add(cursorPos.sub(rect1!!.pos()).nor())
        val collidedMotion = getCollidedMotion(rect1, rect1!!.motion(), rect2!!)
        rect1!!.pos().add(collidedMotion)
        rect1!!.motion().clampToMax().reduce(0.3).collidedAxesToZero(collidedMotion)
        clearColorBuffer()
        clearColor(0.2, 0.1, 0.3)
        batch!!.begin()
        batch!!.drawQuad(
            1.0,
            1.0,
            0.0,
            1.0,
            rect1!!.min.x,
            rect1!!.min.y,
            rect1!!.rect()!!.width,
            rect1!!.rect()!!.height
        )
        batch!!.drawQuad(
            0.5,
            0.2,
            0.2,
            1.0,
            rect2!!.min.x,
            rect2!!.min.y,
            rect2!!.rect()!!.width,
            rect2!!.rect()!!.height
        )
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Physics", 1280, 720)
            run(Main())
        }
    }
}