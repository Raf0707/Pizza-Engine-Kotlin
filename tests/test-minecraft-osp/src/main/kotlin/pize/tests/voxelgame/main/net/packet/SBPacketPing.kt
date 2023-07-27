package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketPing() : IPacket<PacketHandler?>(PACKET_ID) {
    var timeNanos: Long = 0

    constructor(timeNanos: Long) : this() {
        this.timeNanos = timeNanos
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeLong(timeNanos)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        timeNanos = stream!!.readLong()
    }

    companion object {
        const val PACKET_ID = 7
    }
}
