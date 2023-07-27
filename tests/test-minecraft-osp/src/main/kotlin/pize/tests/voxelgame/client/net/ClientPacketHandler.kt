package pize.tests.voxelgame.client.net

import pize.Pize.execSync
import pize.Pize.exit
import pize.graphics.texture.Region.set
import pize.graphics.util.color.Color.set
import pize.math.Maths
import pize.math.util.EulerAngles.set
import pize.math.vecmath.matrix.Matrix4f.set
import pize.math.vecmath.vector.Vec2f.set
import pize.math.vecmath.vector.Vec3f.set
import pize.math.vecmath.vector.Vec3i.set
import pize.net.tcp.TcpConnection
import pize.net.tcp.TcpListener
import pize.net.tcp.packet.PacketHandler
import pize.net.tcp.packet.Packets.getPacketInfo
import pize.tests.voxelgame.client.ClientGame
import pize.tests.voxelgame.client.entity.RemotePlayer
import pize.tests.voxelgame.main.chat.MessageSourceServer
import pize.tests.voxelgame.main.net.packet.*
import pize.tests.voxelgame.main.text.Component

class ClientPacketHandler(val game: ClientGame) : TcpListener, PacketHandler {

    override fun received(bytes: ByteArray?, sender: TcpConnection?) {
        val packetInfo = getPacketInfo(bytes!!) ?: return
        rxCounter++
        when (packetInfo.packetID) {
            CBPacketDisconnect.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketDisconnect())
                game.disconnect()
                println("[Client]: Connection closed: " + packet.reasonComponent)
            }

            CBPacketEncryptStart.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketEncryptStart())
                val encryptedClientKey = packet.publicServerKey!!.encrypt(game.encryptKey.key.getEncoded())
                SBPacketEncryptEnd(encryptedClientKey).write(sender)
                sender!!.encode(game.encryptKey) // * шифрование *
                SBPacketAuth(game.session.sessionToken).write(sender)
            }

            CBPacketSpawnInfo.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketSpawnInfo())
                game.createClientLevel(packet.levelName)
                game.spawnPlayer(packet.position)
                game.level.chunkManager.startLoadChunks()
                game.sendPacket(SBPacketRenderDistance(game.session.options.renderDistance))
            }

            CBPacketPlaySound.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketPlaySound())
                val camPosition = game.camera.position
                execSync {
                    game.session.audioPlayer.play(
                        packet.soundID,
                        packet.volume, packet.pitch,
                        packet.x - camPosition.x,
                        packet.y - camPosition.y,
                        packet.z - camPosition.z
                    )
                }
            }

            CBPacketTime.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketTime())
                game.time.ticks = packet.gameTimeTicks
            }

            CBPacketAbilities.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketAbilities())
                game.player.isFlyEnabled = packet.flyEnabled
            }

            CBPacketTeleportPlayer.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketTeleportPlayer())
                val localPlayer = game.player ?: break

                // Load another level
                if (packet.levelName != localPlayer.level.configuration.name) {
                    game.createClientLevel(packet.levelName)
                    game.level.chunkManager.startLoadChunks()
                    localPlayer.setLevel(game.level)
                }
                localPlayer.position.set(packet.position!!)
                localPlayer.rotation.set(packet.rotation)
            }

            CBPacketChatMessage.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketChatMessage())
                game.chat.putMessage(packet)
            }

            CBPacketPlayerSneaking.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketPlayerSneaking())
                val targetEntity = game.level.getEntity(packet.playerUUID)
                (targetEntity as? RemotePlayer)?.setSneaking(packet.sneaking)
            }

            CBPacketEntityMove.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketEntityMove())
                var targetEntity = game.level.getEntity(packet.uuid)
                if (targetEntity == null && game.player.uuid === packet.uuid) targetEntity = game.player
                if (targetEntity != null) {
                    targetEntity.position.set(packet.position!!)
                    targetEntity.rotation.set(packet.rotation)
                    targetEntity.velocity.set(packet.velocity)
                }
            }

            CBPacketSpawnEntity.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketSpawnEntity())
                val entity = packet.type!!.createEntity(game.level)
                entity!!.position.set(packet.position!!)
                entity.rotation.set(packet.rotation)
                entity.uuid = packet.uuid
                game.level.addEntity(entity)
            }

            CBPacketRemoveEntity.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketRemoveEntity())
                game.level.removeEntity(packet.uuid)
            }

            CBPacketSpawnPlayer.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketSpawnPlayer())
                val remotePlayer = RemotePlayer(game.level, packet.playerName)
                remotePlayer.position.set(packet.position!!)
                remotePlayer.rotation.set(packet.rotation)
                remotePlayer.uuid = packet.uuid
                game.level.addEntity(remotePlayer)
            }

            CBPacketBlockUpdate.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketBlockUpdate())
                game.level.setBlock(packet.x, packet.y, packet.z, packet.state)
            }

            CBPacketChunk.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketChunk())
                game.level.chunkManager.receivedChunk(packet)
            }

            CBPacketPong.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(CBPacketPong())
                val message = "Ping - " + String.format(
                    "%.5f",
                    (System.nanoTime() - packet.timeNanos) / Maths.NanosInSecond
                ) + " ms"
                game.chat.putMessage(MessageSourceServer(), Component().text(message))
                println("[Client]: $message")
            }
        }
    }

    override fun connected(connection: TcpConnection?) {}
    override fun disconnected(connection: TcpConnection?) {
        exit()
    }

    companion object {
        var rx = 0
        var rxCounter = 0
    }
}
