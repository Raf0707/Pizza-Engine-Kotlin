package pize.tests.net.packet

import pize.net.tcp.packet.IPacket
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class EncodePacket() : IPacket<Any?>(PACKET_TYPE_ID) {
    var key: ByteArray
        private set

    constructor(key: ByteArray) : this() {
        this.key = key
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeByteArray(key)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        key = stream!!.readByteArray()
    }

    companion object {
        const val PACKET_TYPE_ID = 83
    }
}
