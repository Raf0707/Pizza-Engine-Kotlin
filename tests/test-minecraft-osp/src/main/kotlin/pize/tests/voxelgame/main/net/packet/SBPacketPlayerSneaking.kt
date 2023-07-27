package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.main.entity.Player
import pize.tests.voxelgame.server.net.PlayerGameConnection
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException
import java.util.*

class SBPacketPlayerSneaking() : IPacket<PlayerGameConnection?>(PACKET_ID) {
    var playerUUID: UUID? = null
    var sneaking = false

    constructor(player: Player) : this() {
        playerUUID = player.uuid
        sneaking = player.isSneaking
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeUUID(playerUUID!!)
        stream.writeBoolean(sneaking)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        playerUUID = stream!!.readUUID()
        sneaking = stream.readBoolean()
    }

    override fun handle(packetHandler: PlayerGameConnection) {
        packetHandler.handleSneaking(this)
    }

    companion object {
        const val PACKET_ID = 16
    }
}