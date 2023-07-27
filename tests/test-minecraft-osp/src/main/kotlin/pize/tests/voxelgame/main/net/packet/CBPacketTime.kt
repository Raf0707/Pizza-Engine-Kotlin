package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketTime() : IPacket<PacketHandler?>(PACKET_ID) {
    var gameTimeTicks: Long = 0

    constructor(gameTimeTicks: Long) : this() {
        this.gameTimeTicks = gameTimeTicks
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeLong(gameTimeTicks)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        gameTimeTicks = stream!!.readLong()
    }

    companion object {
        const val PACKET_ID = 22
    }
}