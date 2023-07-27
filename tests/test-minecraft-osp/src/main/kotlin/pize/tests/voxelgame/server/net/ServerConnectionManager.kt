package pize.tests.voxelgame.server.net

import pize.net.security.KeyRSA
import pize.net.tcp.TcpConnection
import pize.net.tcp.TcpListener
import pize.net.tcp.packet.PacketHandler
import pize.net.tcp.packet.Packets.getPacketInfo
import pize.tests.voxelgame.main.net.packet.*
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.player.ServerPlayer

class ServerConnectionManager(val server: Server) : TcpListener {
    private val packetHandlerMap: MutableMap<TcpConnection?, PacketHandler?>
    val rsaKey: KeyRSA

    init {
        packetHandlerMap = HashMap()
        rsaKey = KeyRSA(1024)
    }

    fun setPacketHandler(connection: TcpConnection?, packetHandler: PacketHandler?) {
        packetHandlerMap[connection] = packetHandler
    }

    @Synchronized
    override fun received(bytes: ByteArray?, sender: TcpConnection?) {
        val packetInfo = getPacketInfo(bytes!!) ?: return
        val packetHandler = packetHandlerMap[sender]
        when (packetInfo.packetID) {
            SBPacketAuth.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketAuth())
                .handle(packetHandler as ServerLoginPacketHandler?)

            SBPacketEncryptEnd.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketEncryptEnd())
                .handle(packetHandler as ServerLoginPacketHandler?)

            SBPacketLogin.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketLogin())
                .handle(packetHandler as ServerLoginPacketHandler?)

            SBPacketChunkRequest.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketChunkRequest())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketPlayerBlockSet.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketPlayerBlockSet())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketMove.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketMove())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketRenderDistance.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketRenderDistance())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketPlayerSneaking.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketPlayerSneaking())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketChatMessage.Companion.PACKET_ID -> packetInfo.readPacket(SBPacketChatMessage())
                .handle(packetHandler as PlayerGameConnection?)

            SBPacketPing.Companion.PACKET_ID -> {
                val packet = packetInfo.readPacket(SBPacketPing())
                CBPacketPong(packet.timeNanos).write(sender)
            }
        }
    }

    override fun connected(connection: TcpConnection?) {
        setPacketHandler(connection, ServerLoginPacketHandler(server, connection))
    }

    override fun disconnected(connection: TcpConnection?) {
        val packetHandler = packetHandlerMap[connection]
        if (packetHandler is PlayerGameConnection) {
            val player: ServerPlayer = packetHandler.player
            server.playerList.disconnectPlayer(player)
            player.sendToChat(Component().color(TextColor.YELLOW).text("Player " + player.name + " leave the game"))
        }
        packetHandlerMap.remove(connection)
    }
}
