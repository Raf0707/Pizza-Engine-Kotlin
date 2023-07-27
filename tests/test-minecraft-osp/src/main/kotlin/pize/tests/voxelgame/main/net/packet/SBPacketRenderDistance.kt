package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.PlayerGameConnection
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketRenderDistance() : IPacket<PlayerGameConnection?>(PACKET_ID) {
    var renderDistance = 0

    constructor(renderDistance: Int) : this() {
        this.renderDistance = renderDistance
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeInt(renderDistance)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        renderDistance = stream!!.readInt()
    }

    override fun handle(packetHandler: PlayerGameConnection) {
        packetHandler.handleRenderDistance(this)
    }

    companion object {
        const val PACKET_ID = 8
    }
}
