package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketDisconnect() : IPacket<PacketHandler?>(PACKET_ID) {
    var reasonComponent: String? = null

    constructor(reasonComponent: String?) : this() {
        this.reasonComponent = reasonComponent
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(reasonComponent)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        reasonComponent = stream!!.readUTF()
    }

    companion object {
        const val PACKET_ID = 3
    }
}
