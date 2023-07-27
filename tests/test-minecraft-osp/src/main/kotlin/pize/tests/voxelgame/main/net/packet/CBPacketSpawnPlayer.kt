package pize.tests.voxelgame.main.net.packet

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.server.player.ServerPlayer
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class CBPacketSpawnPlayer() : IPacket<PacketHandler?>(PACKET_ID) {
    var uuid: UUID? = null
    var position: Vec3f? = null
    var rotation: EulerAngles? = null
    var playerName: String? = null

    constructor(player: ServerPlayer) : this() {
        uuid = player.uuid
        position = player.position
        rotation = player.rotation
        playerName = player.name
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUTF(uuid.toString())
        stream.writeVec3f(position!!)
        stream.writeEulerAngles(rotation!!)
        stream.writeUTF(playerName)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        uuid = UUID.fromString(stream!!.readUTF())
        position = stream.readVec3f()
        rotation = stream.readEulerAngles()
        playerName = stream.readUTF()
    }

    companion object {
        const val PACKET_ID = 15
    }
}
