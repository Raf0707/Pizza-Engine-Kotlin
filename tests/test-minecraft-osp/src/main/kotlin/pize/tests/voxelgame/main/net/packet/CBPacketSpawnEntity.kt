package pize.tests.voxelgame.main.net.packet

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.packet.IPacket
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.entity.Entity
import pize.tests.voxelgame.main.entity.EntityType
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class CBPacketSpawnEntity() : IPacket<PacketHandler?>(PACKET_ID) {
    var type: EntityType<*>? = null
    var uuid: UUID? = null
    var position: Vec3f? = null
    var rotation: EulerAngles? = null

    constructor(entity: Entity) : this() {
        type = entity.entityType
        uuid = entity.uuid
        position = entity.position
        rotation = entity.rotation
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeInt(type.getID())
        stream.writeUTF(uuid.toString())
        stream.writeVec3f(position!!)
        stream.writeEulerAngles(rotation!!)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        type = EntityType.Companion.fromEntityID(stream!!.readInt())
        uuid = UUID.fromString(stream.readUTF())
        position = stream.readVec3f()
        rotation = stream.readEulerAngles()
    }

    companion object {
        const val PACKET_ID = 14
    }
}