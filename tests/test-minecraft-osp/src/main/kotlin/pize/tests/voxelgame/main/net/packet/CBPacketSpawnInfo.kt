package pize.tests.voxelgame.main.net.packet

import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketSpawnInfo() : IPacket<PacketHandler?>(PACKET_ID) {
    var levelName: String? = null
    var position: Vec3f? = null

    constructor(levelName: String?, position: Vec3f?) : this() {
        this.levelName = levelName
        this.position = position
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(levelName)
        stream.writeVec3f(position!!)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        levelName = stream!!.readUTF()
        position = stream.readVec3f()
    }

    companion object {
        const val PACKET_ID = 13
    }
}
