package pize.tests.voxelgame.main.net.packet

import pize.net.security.PublicRSA
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketEncryptStart() : IPacket<PacketHandler?>(PACKET_ID) {
    var publicServerKey: PublicRSA? = null

    constructor(publicServerKey: PublicRSA?) : this() {
        this.publicServerKey = publicServerKey
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.write(publicServerKey!!.key!!.encoded)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        publicServerKey = PublicRSA(stream!!.readAllBytes())
    }

    companion object {
        const val PACKET_ID = 5
    }
}
