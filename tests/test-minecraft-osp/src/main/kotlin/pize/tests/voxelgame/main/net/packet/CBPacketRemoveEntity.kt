package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.entity.Entity
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class CBPacketRemoveEntity() : IPacket<PacketHandler?>(PACKET_ID) {
    var uuid: UUID? = null

    constructor(entity: Entity?) : this() {
        uuid = entity.getUUID()
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(uuid.toString())
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        uuid = UUID.fromString(stream!!.readUTF())
    }

    companion object {
        const val PACKET_ID = 16
    }
}