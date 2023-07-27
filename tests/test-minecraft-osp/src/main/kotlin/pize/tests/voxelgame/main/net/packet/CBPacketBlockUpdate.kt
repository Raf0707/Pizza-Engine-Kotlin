package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketBlockUpdate() : IPacket<PacketHandler?>(PACKET_ID) {
    var x = 0
    var y = 0
    var z = 0
    var state: Short = 0

    constructor(x: Int, y: Int, z: Int, state: Short) : this() {
        this.x = x
        this.y = y
        this.z = z
        this.state = state
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeInt(x)
        stream.writeInt(y)
        stream.writeInt(z)
        stream.writeShort(state.toInt())
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        x = stream!!.readInt()
        y = stream.readInt()
        z = stream.readInt()
        state = stream.readShort()
    }

    companion object {
        const val PACKET_ID = 10
    }
}
