package pize.tests.net.packet

import pize.net.tcp.packet.IPacket
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class PingPacket() : IPacket<Any?>(PACKET_TYPE_ID) {
    var time: Long = 0
        private set

    constructor(time: Long) : this() {
        this.time = time
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeLong(time)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        time = stream!!.readLong()
    }

    companion object {
        const val PACKET_TYPE_ID = 18
    }
}
