package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.PlayerGameConnection
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketChunkRequest() : IPacket<PlayerGameConnection?>(PACKET_ID) {
    var chunkX = 0
    var chunkZ = 0

    constructor(chunkX: Int, chunkZ: Int) : this() {
        this.chunkX = chunkX
        this.chunkZ = chunkZ
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeInt(chunkX)
        stream.writeInt(chunkZ)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        chunkX = stream!!.readInt()
        chunkZ = stream.readInt()
    }

    override fun handle(packetHandler: PlayerGameConnection) {
        packetHandler.handleChunkRequest(this)
    }

    companion object {
        const val PACKET_ID = 12
    }
}
