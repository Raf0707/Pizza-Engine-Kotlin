package pize.tests.voxelgame.client.control

import pize.Pize.clipboardString
import pize.Pize.exit
import pize.Pize.mouse
import pize.Pize.window
import pize.graphics.gl.Face
import pize.graphics.gl.Gl.polygonMode
import pize.graphics.gl.PolygonMode
import pize.io.glfw.Key
import pize.math.Maths.random
import pize.math.vecmath.vector.Vec3f
import pize.physic.BoxBody
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.block.Blocks
import pize.tests.voxelgame.client.options.KeyMapping
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.net.packet.SBPacketPing
import pize.tests.voxelgame.main.net.packet.SBPacketPlayerBlockSet

class GameController(val session: Minecraft) {
    val playerController: PlayerController
    private var f3Plus = false

    init {
        playerController = PlayerController(session)
    }

    fun update() {
        val options = session.options
        val camera = session.game.camera
        val blockRayCast = session.game.blockRayCast
        val level = session.game.level
        /** Window  */

        // Fullscreen
        if (Key.F11.isDown) {
            playerController.rotationController.lockNextFrame()
            window()!!.toggleFullscreen()
        }

        // VSync
        if (Key.V.isDown) {
            window()!!.toggleVsync()
        }
        /** Chat  */
        val chat = session.game.chat
        if (chat!!.isOpened) {
            if (Key.ENTER.isDown) {
                chat.enter()
                chat.close()
            }
            if (Key.UP.isDown) chat.historyUp()
            if (Key.DOWN.isDown) chat.historyDown()
            if (Key.LEFT_CONTROL.isPressed) {
                if (Key.C.isDown) clipboardString = chat.enteringText
                if (Key.V.isDown) chat.textProcessor.insertLine(clipboardString)
            }
            if (Key.ESCAPE.isDown) chat.close()
            return  // Abort subsequent control
        } else if (options!!.getKey(KeyMapping.CHAT)!!.isDown) chat.open() else if (options.getKey(KeyMapping.COMMAND)!!.isDown) {
            chat.openAsCommandLine()
        }
        /** Game  */

        // Player
        playerController.update()

        // Place/Destroy/Copy block
        if (blockRayCast!!.isSelected) {
            val player = session.game.player
            if (mouse()!!.isLeftDown || Key.U.isPressed) {
                val blockPos = blockRayCast.selectedBlockPosition
                level!!.setBlock(blockPos!!.x, blockPos.y, blockPos.z, Blocks.AIR.defaultData)
                session.game.sendPacket(
                    SBPacketPlayerBlockSet(
                        blockPos.x,
                        blockPos.y,
                        blockPos.z,
                        Blocks.AIR.defaultData
                    )
                )
                for (i in 0..99) {
                    session.game.spawnParticle(
                        session.BREAK_PARTICLE, Vec3f(
                            blockPos.x + random(1f),
                            blockPos.y + random(1f),
                            blockPos.z + random(1f)
                        )
                    )
                }
            } else if (mouse()!!.isRightDown || Key.J.isPressed) {
                placeBlock()
            } else if (mouse()!!.isMiddleDown) {
                val blockPos = blockRayCast.selectedBlockPosition
                player!!.holdBlock = level!!.getBlockProps(blockPos!!.x, blockPos.y, blockPos.z)
            }
        }

        // Show mouse
        if (Key.M.isDown) playerController.rotationController.toggleShowMouse()

        // F3 + ...
        if (Key.F3.isPressed) {
            // G - Chunk border
            if (Key.G.isDown) {
                session.renderer.worldRenderer.chunkBorderRenderer.toggleShow()
                f3Plus = true
            }

            // R - Reload chunks
            if (Key.H.isDown) {
                session.game.level.chunkManager.reload()
                f3Plus = true
            }
        }

        // Info Panel
        if (Key.F3.isReleased) {
            if (!f3Plus) session.renderer.infoRenderer.toggleOpen()
            f3Plus = false
        }

        // Camera zoom
        if (options!!.getKey(KeyMapping.ZOOM)!!.isDown) camera!!.zoom =
            10f else if (options.getKey(KeyMapping.ZOOM)!!.isPressed) {
            camera!!.zoom = camera.zoom + mouse()!!.scroll * (camera.zoom / 8)
        } else if (options.getKey(KeyMapping.ZOOM)!!.isReleased) camera!!.zoom = 1f

        // Ping server
        if (Key.P.isDown) session.game.sendPacket(SBPacketPing(System.nanoTime()))

        // Polygon Mode
        if (Key.F9.isDown) polygonMode(Face.FRONT_AND_BACK, PolygonMode.LINE)
        if (Key.F8.isDown) polygonMode(Face.FRONT_AND_BACK, PolygonMode.FILL)

        // Exit
        if (Key.ESCAPE.isDown) exit()
    }

    private fun placeBlock() {
        val blockRayCast = session.game.blockRayCast
        val level = session.game.level
        val blockPos = blockRayCast.imaginaryBlockPosition
        val player = session.game.player
        val block = player!!.holdBlock
        val blockStates = block.states.size
        var blockState: Byte = 0
        if (blockStates > 1) blockState = random(0, blockStates - 1).toByte()
        val blockData = BlockData.getData(block, blockState)
        val collideShape = block!!.getState(blockState.toInt()).collide
        if (collideShape != null) {
            val blockBoxes = collideShape.boxes
            val blockBox = BoxBody(Vec3f(blockPos!!))
            val entities = session.game.level.entities

            // Check intersect with player & entity
            for (box in blockBoxes!!) {
                blockBox.boundingBox!!.resize(box!!)
                if (player.intersects(blockBox)) return
                for (entity in entities!!) if (entity!!.intersects(blockBox)) return
            }
        }
        level!!.setBlock(blockPos!!.x, blockPos.y, blockPos.z, blockData)
        session.game.sendPacket(SBPacketPlayerBlockSet(blockPos.x, blockPos.y, blockPos.z, blockData))
    }
}
