package pize.tests.voxelgame.main.net.packet

import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.server.net.ServerLoginPacketHandler
import pize.util.io.PizeInputStream
import pize.util.io.PizeOutputStream
import java.io.IOException

class SBPacketLogin() : IPacket<ServerLoginPacketHandler?>(PACKET_ID) {
    var clientVersionID = 0
    var profileName: String? = null

    constructor(clientVersionID: Int, profileName: String?) : this() {
        this.clientVersionID = clientVersionID
        this.profileName = profileName
    }

    @Throws(IOException::class)
    override fun write(stream: PizeOutputStream?) {
        stream!!.writeInt(clientVersionID)
        stream.writeUTF(profileName)
    }

    @Throws(IOException::class)
    override fun read(stream: PizeInputStream?) {
        clientVersionID = stream!!.readInt()
        profileName = stream.readUTF()
    }

    override fun handle(packetHandler: ServerLoginPacketHandler) {
        packetHandler.handleLogin(this)
    }

    companion object {
        const val PACKET_ID = 6
    }
}
