package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketAbilities() : IPacket<PacketHandler?>(PACKET_ID) {
    var flyEnabled = false

    constructor(flyEnabled: Boolean) : this() {
        this.flyEnabled = flyEnabled
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeBoolean(flyEnabled)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        flyEnabled = stream!!.readBoolean()
    }

    companion object {
        const val PACKET_ID = 21
    }
}
