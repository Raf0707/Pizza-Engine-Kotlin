package pize.tests.voxelgame.server.net

import pize.net.security.KeyAES
import pize.net.tcp.TcpConnection
import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.net.PlayerProfile
import pize.tests.voxelgame.main.net.packet.*
import pize.tests.voxelgame.server.Server

class ServerLoginPacketHandler(private val server: Server, private val connection: TcpConnection?) : PacketHandler {
    private var profileName: String? = null
    private var versionID = 0
    fun handleLogin(packet: SBPacketLogin) {
        profileName = packet.profileName
        versionID = packet.clientVersionID
        if (versionID != server.configuration.version.id) {
            CBPacketDisconnect("Server not available on your game version").write(connection)
            return
        }
        if (PlayerProfile.Companion.isNameInvalid(profileName)) {
            CBPacketDisconnect("Invalid player name").write(connection)
            return
        }
        if (server.playerList.isPlayerOnline(profileName)) {
            CBPacketDisconnect("Player with your nickname already plays on the server").write(connection)
            return
        }
        CBPacketEncryptStart(server.connectionManager.rsaKey.public).write(connection)
    }

    fun handleEncryptEnd(packet: SBPacketEncryptEnd) {
        val decryptedClientKey = KeyAES(server.connectionManager.rsaKey.decrypt(packet.encryptedClientKey))
        connection!!.encode(decryptedClientKey)
    }

    fun handleAuth(packet: SBPacketAuth) {
        if ("54_54-iWantPizza-54_54" != packet.accountSessionToken) {
            CBPacketDisconnect("Invalid session").write(connection)
            return
        }
        server.playerList.addNewPlayer(profileName, connection)
    }
}
