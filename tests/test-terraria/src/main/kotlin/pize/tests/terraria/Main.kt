package pize.tests.terraria

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.fPS
import pize.Pize.isTouched
import pize.Pize.mouse
import pize.Pize.run
import pize.Pize.window
import pize.Pize.x
import pize.Pize.y
import pize.app.AppAdapter
import pize.graphics.font.BitmapFont
import pize.graphics.font.FontLoader.loadTrueType
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key
import pize.math.Maths.abs
import pize.math.vecmath.vector.Vec2f
import pize.tests.terraria.entity.Player
import pize.tests.terraria.graphics.GameRenderer
import pize.tests.terraria.tile.TileType
import pize.tests.terraria.world.World

class Main : AppAdapter() {
    private var world: World? = null
    private var gameRenderer: GameRenderer? = null
    private var batch: TextureBatch? = null
    private var font: BitmapFont? = null
    private var player: Player? = null
    override fun init() {
        gameRenderer = GameRenderer()
        world = World(300, 100)
        player = Player()
        player!!.pos()[world.getTileMap().width / 2f - 1.5f] = world.getTileMap().height.toFloat()
        world.getEntities().add(player)
        batch = TextureBatch()
        font = loadTrueType("font/andy_bold.ttf", 32)
    }

    override fun render() {
        clearColorBuffer()
        clearColor(0.14, 0.43, 0.8)
        ctrl()
        batch!!.begin()
        gameRenderer!!.update()
        gameRenderer!!.renderMap(world.getTileMap())
        gameRenderer!!.renderEntities(world.getEntities())
        player!!.update(world)
        font!!.drawText(batch!!, "fps: " + fPS, 10f, 10f)
        batch!!.end()
        if (Key.ESCAPE.isDown) exit()
        if (Key.F11.isDown) window()!!.toggleFullscreen()
        if (Key.V.isDown) window()!!.toggleVsync()
    }

    private fun ctrl() {
        val scroll = mouse()!!.scroll
        gameRenderer.getRenderInfo().mulScale(
            if (scroll < 0) 1 / 1.1.pow(abs(scroll).toDouble()) else if (scroll > 0) 1.1.pow(abs(scroll).toDouble()) else 1
        )
        gameRenderer!!.camera.position.set(player!!.pos().copy().add(player!!.rect()!!.center))
        val touch = Vec2f(x.toFloat(), y.toFloat())
            .sub(gameRenderer!!.camera.width / 2f, gameRenderer!!.camera.height / 2f)
            .div(gameRenderer.getRenderInfo().cellSize * gameRenderer.getRenderInfo().scale)
            .add(gameRenderer!!.camera.position)
        val tile = world.getTileMap().getTile(touch.xf(), touch.yf())
        if (tile != null && isTouched) tile.type = if (mouse()!!.isLeftPressed) TileType.AIR else TileType.DIRT
        if (player!!.pos().y < -100) player!!.pos().y = world.getTileMap().height.toFloat()
    }

    override fun resize(width: Int, height: Int) {
        gameRenderer!!.camera.resize(width, height)
    }

    override fun dispose() {
        batch!!.dispose()
        font!!.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Terraria", 1280, 720)
            window()!!.setIcon("icon.png")
            run(Main())
        }
    }
}
