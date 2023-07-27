package pize.tests.net.packet

import pize.net.tcp.packet.IPacket
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class MessagePacket() : IPacket<Any?>(PACKET_TYPE_ID) {
    var message: String? = null
        private set

    constructor(message: String?) : this() {
        this.message = message
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(message)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        message = stream!!.readUTF()
    }

    companion object {
        const val PACKET_TYPE_ID = 54
    }
}
