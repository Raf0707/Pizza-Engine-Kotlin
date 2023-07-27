package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.PlayerGameConnection
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketChatMessage() : IPacket<PlayerGameConnection?>(PACKET_ID) {
    var message: String? = null

    constructor(message: String?) : this() {
        this.message = message
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(message)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        message = stream!!.readUTF()
    }

    override fun handle(packetHandler: PlayerGameConnection) {
        packetHandler.handleChatMessage(this)
    }

    companion object {
        const val PACKET_ID = 19
    }
}