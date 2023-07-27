package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.ServerLoginPacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketAuth() : IPacket<ServerLoginPacketHandler?>(PACKET_ID) {
    var accountSessionToken: String? = null

    constructor(accountSessionToken: String?) : this() {
        this.accountSessionToken = accountSessionToken
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(accountSessionToken)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        accountSessionToken = stream!!.readUTF()
    }

    override fun handle(packetListener: ServerLoginPacketHandler) {
        packetListener.handleAuth(this)
    }

    companion object {
        const val PACKET_ID = 1
    }
}
