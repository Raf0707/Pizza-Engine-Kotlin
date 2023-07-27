package pize.tests.voxelgame.server.player

import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.TcpConnection
import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.main.net.packet.*
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.level.ServerLevel
import java.util.concurrent.ConcurrentHashMap

class PlayerList(val server: Server) {
    private val playerMap: MutableMap<String?, ServerPlayer>

    init {
        playerMap = ConcurrentHashMap()
    }

    val players: Collection<ServerPlayer>
        get() = playerMap.values

    fun isPlayerOnline(name: String?): Boolean {
        return playerMap.containsKey(name)
    }

    fun getPlayer(name: String?): ServerPlayer? {
        return playerMap[name]
    }

    fun addNewPlayer(name: String?, connection: TcpConnection?) {
        // Get level & Spawn position
        val level: ServerLevel?
        val spawnPosition: Vec3f?
        val offlinePlayer = getOfflinePlayer(name)
        if (offlinePlayer != null) {
            val levelName = offlinePlayer.levelName
            server.levelManager.loadLevel(levelName)
            level = server.levelManager.getLevel(levelName)
            spawnPosition = offlinePlayer.position
        } else {
            val levelName = server.configuration.defaultLevelName
            server.levelManager.loadLevel(levelName)
            level = server.levelManager.getLevel(levelName)
            spawnPosition = level.getSpawnPosition()
        }

        // Add ServerPlayer to list
        val serverPlayer = ServerPlayer(level, connection, name)
        server.connectionManager.setPacketHandler(connection, serverPlayer.connectionAdapter)
        serverPlayer.teleport(level, spawnPosition)
        playerMap[name] = serverPlayer

        // Send packets to player
        val connectionAdapter = serverPlayer.connectionAdapter
        CBPacketSpawnInfo(level.configuration.name, spawnPosition).write(connection) // spawn init info
        CBPacketAbilities(false).write(connection) // abilities
        for (anotherPlayer in playerMap.values) if (anotherPlayer !== serverPlayer) connectionAdapter!!.sendPacket(
            CBPacketSpawnPlayer(anotherPlayer)
        ) // all players info

        // Load chunks for player
        level.addEntity(serverPlayer)
        level.chunkManager.loadInitChunkForPlayer(serverPlayer)

        // Send to all player-connection-event packet
        broadcastPacketExcept(CBPacketSpawnPlayer(serverPlayer), serverPlayer)
        serverPlayer.sendToChat(Component().color(TextColor.YELLOW).text("Player $name joined the game"))
    }

    fun disconnectPlayer(player: ServerPlayer?) {
        broadcastPacketExcept(CBPacketRemoveEntity(player), player) // Remove player entity on client
        player.getLevel().removeEntity(player) // Remove entity on server
        PlayerIO.save(player) // Save
        playerMap.remove(player.getName())
    }

    fun getOfflinePlayer(name: String?): OfflinePlayer? { //: NICHE TAK, HARAM
        return null
    }

    fun broadcastPacket(packet: IPacket<*>) {
        for (player in playerMap.values) player.sendPacket(packet)
    }

    fun broadcastPacketExcept(packet: IPacket<*>, except: ServerPlayer?) {
        for (player in playerMap.values) if (player !== except) player.sendPacket(packet)
    }

    fun broadcastPacketLevel(level: ServerLevel, packet: IPacket<*>) {
        for (player in playerMap.values) if (player.level === level) player.sendPacket(packet)
    }

    fun broadcastPacketLevelExcept(level: ServerLevel?, packet: IPacket<*>, except: ServerPlayer) {
        for (player in playerMap.values) if (player.level === level && player !== except) player.sendPacket(packet)
    }

    fun broadcastServerMessage(message: Component?) {
        broadcastPacket(CBPacketChatMessage(message!!.toFlatList()))
        println(message)
    }

    fun broadcastPlayerMessage(player: ServerPlayer, message: Component?) {
        broadcastPacket(CBPacketChatMessage(player.name, message!!.toFlatList()))
        println(message)
    }
}
