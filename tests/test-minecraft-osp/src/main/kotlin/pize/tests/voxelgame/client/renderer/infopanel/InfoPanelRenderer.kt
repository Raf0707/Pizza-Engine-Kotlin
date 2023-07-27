package pize.tests.voxelgame.client.renderer.infopanel

import pize.Pize.dt
import pize.Pize.fPS
import pize.Pize.height
import pize.Pize.width
import pize.app.Disposable
import pize.graphics.util.batch.TextureBatch
import pize.math.Maths.frac
import pize.math.vecmath.vector.Vec2f.Companion.len
import pize.math.vecmath.vector.Vec3f.len
import pize.tests.voxelgame.client.ClientGame
import pize.tests.voxelgame.client.net.ClientPacketHandler
import pize.tests.voxelgame.client.options.KeyMapping
import pize.tests.voxelgame.client.renderer.GameRenderer
import pize.tests.voxelgame.client.renderer.text.TextComponentBatch
import pize.tests.voxelgame.main.chunk.ChunkUtils
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.main.time.GameTime
import pize.util.Utils.delayElapsed
import kotlin.math.max

class InfoPanelRenderer(private val gameRenderer: GameRenderer) : Disposable {
    private var infoLineNum = 0
    private var hintLineNum = 0
    private val batch: TextureBatch
    private val textBatch: TextComponentBatch?
    private var opened = false
    private var animationEnded: Boolean
    private var animationTimeLine = 0f
    private var panelOffsetX = 0f // For open animation

    init {
        textBatch = gameRenderer.textComponentBatch
        batch = TextureBatch(200)
        animationEnded = true

        // Open
        Thread {
            delayElapsed(500)
            setOpened(true)
        }.start()
    }

    private fun animate() {
        val width = width / 2
        if (!animationEnded) {
            val animationSpeed = (max(0.03, (1 - animationTimeLine * animationTimeLine).toDouble()) * dt * 5).toFloat()
            if (opened) { // open
                animationTimeLine += animationSpeed
                if (animationTimeLine >= 1) {
                    animationTimeLine = 1f
                    animationEnded = true
                }
            } else { // close
                animationTimeLine -= animationSpeed
                if (animationTimeLine <= 0) {
                    animationTimeLine = 0f
                    animationEnded = true
                }
            }
            panelOffsetX = (1 - animationTimeLine) * -width
        }
    }

    fun render() {
        animate()
        if (!opened && animationEnded) return
        val session = gameRenderer.session
        val game = session.game
        val camera = game.camera ?: return
        val options = session.options
        val level = game.level
        val chunkBuilder = level.chunkManager.chunkBuilders[0]
        val time = game.time
        val player = game.player
        val playerPos = player.lerpPosition
        val blockRayCast = game.blockRayCast
        val blockPos = blockRayCast.selectedBlockPosition
        infoLineNum = 0
        hintLineNum = 0
        batch.begin()
        textBatch!!.setBackgroundColor(0.0, 0.0, 0.0, 0.2)
        /** -------- INFO --------  */

        // Game Version
        val loadedMods = session.modLoader.loadedMods
        val modLoaderState = if (loadedMods!!.size == 0) "Vanilla" else loadedMods.size.toString() + " Mod(s) loaded"
        info(
            Component().color(TextColor.DARK_GRAY)
                .text("VoxelGame")
                .text(" " + session.version.name + " (" + modLoaderState + ")")
        )

        // FPS
        info(Component().color(TextColor.YELLOW).text(fPS.toString() + " fps"))

        // Packets
        nextLine()
        info(TextColor.GRAY, "Packets sent", TextColor.YELLOW, ClientGame.Companion.tx)
        info(TextColor.GRAY, "Packets received", TextColor.YELLOW, ClientPacketHandler.Companion.rx)

        // Position
        nextLine()
        info(
            Component()
                .color(TextColor.RED).text("X")
                .color(TextColor.GREEN).text("Y")
                .color(TextColor.AQUA).text("Z")
                .reset().text(": ")
                .color(TextColor.RED).text(String.format("%.3f", playerPos!!.x) + "  ")
                .color(TextColor.GREEN).text(String.format("%.3f", playerPos.y) + "  ")
                .color(TextColor.AQUA).text(String.format("%.3f", playerPos.z))
        )

        // Block
        info(
            Component()
                .color(TextColor.RED).text("Block").reset().text(": ")
                .color(TextColor.RED).text(blockPos!!.x.toString() + " ")
                .color(TextColor.GREEN).text(blockPos.y.toString() + " ")
                .color(TextColor.AQUA).text(blockPos.z)
        )

        // Chunk Relative
        info(
            Component()
                .color(TextColor.RED).text("Local").reset().text(": ")
                .color(TextColor.RED).text(ChunkUtils.getLocalCoord(playerPos.xf()).toString() + " ")
                .color(TextColor.GREEN).text(ChunkUtils.getLocalCoord(playerPos.yf()).toString() + " ")
                .color(TextColor.AQUA).text(ChunkUtils.getLocalCoord(playerPos.zf()))
        )

        // Chunk Coordinates
        info(
            Component()
                .color(TextColor.RED).text("Chunk").reset().text(": ")
                .color(TextColor.RED).text(ChunkUtils.getChunkPos(playerPos.xf()).toString() + " ")
                .color(TextColor.GREEN).text(ChunkUtils.getSectionIndex(playerPos.yf()).toString() + " ")
                .color(TextColor.AQUA).text(ChunkUtils.getChunkPos(playerPos.zf()))
        )

        // Level
        nextLine()
        info(TextColor.DARK_GREEN, "Level", TextColor.AQUA, level.configuration.name)

        // Rotation
        info(
            TextColor.DARK_GREEN, "Rotation", TextColor.AQUA,
            "Yaw: " + String.format("%.1f", frac(camera.rotation.yaw.toDouble(), -180.0, 180.0)) +
                    " Pitch: " + String.format("%.1f", camera.rotation.pitch)
        )

        // Speed
        info(
            TextColor.DARK_GREEN,
            "Move Speed: ",
            TextColor.AQUA,
            String.format("%.2f", player.velocity.len() * GameTime.Companion.TICKS_IN_SECOND) + " m/s"
        )

        // Render Distance
        nextLine()
        info(TextColor.BLUE, "Render Distance", TextColor.YELLOW, options!!.renderDistance)

        // Chunks Rendered
        info(
            Component()
                .color(TextColor.BLUE).text("Chunks (")
                .color(TextColor.YELLOW).text("rendered").color(TextColor.BLUE).text("/").color(TextColor.ORANGE)
                .text("total").color(TextColor.BLUE).text(")").reset().text(": ")
                .color(TextColor.YELLOW).text(gameRenderer.worldRenderer.chunkRenderer.renderedChunks)
                .color(TextColor.BLUE).text("/")
                .color(TextColor.ORANGE).text(level.chunkManager.allChunks.size)
        )

        // Chunk Build Time
        info(
            Component()
                .color(TextColor.BLUE).text("Chunk Build Time (")
                .color(TextColor.YELLOW).text("time").color(TextColor.BLUE).text("/").color(TextColor.ORANGE)
                .text("vertices").color(TextColor.BLUE).text(")").reset().text(": ")
                .color(TextColor.YELLOW).text(chunkBuilder!!.buildTime.toString() + " ms")
                .color(TextColor.BLUE).text("/")
                .color(TextColor.ORANGE).text(chunkBuilder.verticesNum)
        )

        // Time
        nextLine()
        info(TextColor.YELLOW, "Day: ", TextColor.AQUA, time.dayNumber)
        info(TextColor.YELLOW, "Time: ", TextColor.AQUA, time.time)

        // info("Threads:");
        // if(serverWorld != null) info("chunk find tps: " + serverWorld.getChunkManager().findTps.get());
        // if(serverWorld != null) info("chunk load tps: " + serverWorld.getChunkManager().loadTps.get());
        // info("chunk build tps: " + level.getChunkManager().buildTps.get());
        // info("chunk check tps: " + level.getChunkManager().checkTps.get());
        // info("Light time (I/D): " + WorldLight.increaseTime + " ms, " + WorldLight.decreaseTime + " ms");
        // Vec3i imaginaryPos = rayCast.getImaginaryBlockPosition();
        // Vec3i selectedPos = rayCast.getSelectedBlockPosition();
        // info("Selected light level (F/B): " + level.getLight(imaginaryPos.x, imaginaryPos.y, imaginaryPos.z) + ", " + level.getLight(selectedPos.x, selectedPos.y, selectedPos.z));

        // HINTS
        hint(Component().color(TextColor.ORANGE).text("F3 + G").color(TextColor.GRAY).text(" - Show chunk border"))
        hint(Component().color(TextColor.ORANGE).text("ESCAPE").color(TextColor.GRAY).text(" - Exit"))
        hint(
            Component().color(TextColor.ORANGE).text(options.getKey(KeyMapping.ZOOM)).color(TextColor.GRAY)
                .text(" - Mouse Wheel - zoom")
        )
        hint(
            Component().color(TextColor.ORANGE).text(options.getKey(KeyMapping.CHAT)).color(TextColor.GRAY)
                .text(" - Chat")
        )
        hint(
            Component().color(TextColor.ORANGE).text(options.getKey(KeyMapping.TOGGLE_PERSPECTIVE))
                .color(TextColor.GRAY).text(" - Toggle perspective")
        )
        hint(Component().color(TextColor.ORANGE).text("M").color(TextColor.GRAY).text(" - Show mouse"))
        batch.end()
    }

    private fun nextLine() {
        infoLineNum++
    }

    private fun info(keyColor: TextColor, key: String, valueColor: TextColor, value: String?) {
        info(Component().color(keyColor).text(key).reset().text(": ").color(valueColor).text(value))
    }

    private fun info(keyColor: TextColor, key: String, valueColor: TextColor, value: Any) {
        info(keyColor, key, valueColor, value.toString())
    }

    private fun info(text: Component?) {
        infoLineNum++
        val x = 5 + panelOffsetX
        val y = height - 5 - textBatch.getFont().lineAdvanceScaled * infoLineNum
        textBatch!!.drawComponent(text, x, y)
    }

    private fun hint(text: Component?) {
        hintLineNum++
        val x = width - 5 - textBatch.getFont().getLineWidth(text.toString()) - panelOffsetX
        val y = height - 5 - textBatch.getFont().lineAdvanceScaled * hintLineNum
        textBatch!!.drawComponent(text, x, y)
    }

    fun setOpened(opened: Boolean) {
        if (!animationEnded) return
        this.opened = opened
        animationEnded = false
    }

    fun toggleOpen() {
        setOpened(!opened)
    }

    override fun dispose() {
        batch.dispose()
    }
}
