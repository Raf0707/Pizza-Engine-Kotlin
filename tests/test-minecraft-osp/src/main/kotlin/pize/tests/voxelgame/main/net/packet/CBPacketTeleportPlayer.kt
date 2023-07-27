package pize.tests.voxelgame.main.net.packet

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class CBPacketTeleportPlayer() : IPacket<PacketHandler?>(PACKET_ID) {
    var position: Vec3f? = null
    var rotation: EulerAngles? = null
    var levelName: String? = null

    constructor(levelName: String?, position: Vec3f?, rotation: EulerAngles?) : this() {
        this.levelName = levelName
        this.position = position
        this.rotation = rotation
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(levelName)
        stream.writeVec3f(position!!)
        stream.writeEulerAngles(rotation!!)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        levelName = stream!!.readUTF()
        position = stream.readVec3f()
        rotation = stream.readEulerAngles()
    }

    companion object {
        const val PACKET_ID = 20
    }
}