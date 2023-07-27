package pize.tests.voxelgame.main.net.packet

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.physic.Velocity3f
import pize.tests.voxelgame.server.player.ServerPlayer
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class CBPacketEntityMove() : IPacket<PacketHandler?>(PACKET_ID) {
    var uuid: UUID? = null
    var position: Vec3f? = null
    var rotation: EulerAngles? = null
    var velocity: Velocity3f? = null

    constructor(serverPlayer: ServerPlayer) : this() {
        uuid = serverPlayer.uuid
        position = serverPlayer.position
        rotation = serverPlayer.rotation
        velocity = serverPlayer.velocity
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUUID(uuid!!)
        stream.writeVec3f(position!!)
        stream.writeEulerAngles(rotation!!)
        stream.writeVec3f(velocity)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        uuid = stream!!.readUUID()
        position = stream.readVec3f()
        rotation = stream.readEulerAngles()
        velocity = Velocity3f(stream.readVec3f())
    }

    companion object {
        const val PACKET_ID = 9
    }
}