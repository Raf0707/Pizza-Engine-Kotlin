package pize.tests.voxelgame.main.net.packet

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.physic.Velocity3f
import pize.tests.voxelgame.client.entity.LocalPlayer
import pize.tests.voxelgame.server.net.PlayerGameConnection
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketMove() : IPacket<PlayerGameConnection?>(PACKET_ID) {
    var position: Vec3f? = null
    var rotation: EulerAngles? = null
    var velocity: Velocity3f? = null

    constructor(localPlayer: LocalPlayer) : this() {
        position = localPlayer.position
        rotation = localPlayer.rotation
        velocity = localPlayer.velocity
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeVec3f(position!!)
        stream.writeEulerAngles(rotation!!)
        stream.writeVec3f(velocity)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        position = stream!!.readVec3f()
        rotation = stream.readEulerAngles()
        velocity = Velocity3f(stream.readVec3f())
    }

    override fun handle(packetHandler: PlayerGameConnection) {
        packetHandler.handleMove(this)
    }

    companion object {
        const val PACKET_ID = 9
    }
}
