package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.ServerLoginPacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketEncryptEnd() : IPacket<ServerLoginPacketHandler?>(PACKET_ID) {
    var encryptedClientKey: ByteArray

    constructor(encryptedClientKey: ByteArray) : this() {
        this.encryptedClientKey = encryptedClientKey
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeByteArray(encryptedClientKey)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        encryptedClientKey = stream!!.readByteArray()
    }

    override fun handle(packetHandler: ServerLoginPacketHandler) {
        packetHandler.handleEncryptEnd(this)
    }

    companion object {
        const val PACKET_ID = 4
    }
}
