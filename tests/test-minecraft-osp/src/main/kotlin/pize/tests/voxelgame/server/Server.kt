package pize.tests.voxelgame.server

import pize.net.tcp.TcpServer
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.time.GameTime
import pize.tests.voxelgame.server.chunk.gen.DefaultGenerator
import pize.tests.voxelgame.server.command.CommandDispatcher
import pize.tests.voxelgame.server.level.LevelManager
import pize.tests.voxelgame.server.net.ServerConnectionManager
import pize.tests.voxelgame.server.player.PlayerList
import pize.util.time.*

abstract class Server : Tickable {
    val configuration: ServerConfiguration
    private val tcpServer: TcpServer
    val connectionManager: ServerConnectionManager
    val playerList: PlayerList
    val levelManager: LevelManager
    val commandDispatcher: CommandDispatcher
    val gameTime: ServerGameTime

    init {
        configuration = ServerConfiguration()
        connectionManager = ServerConnectionManager(this)
        tcpServer = TcpServer(connectionManager)
        playerList = PlayerList(this)
        levelManager = LevelManager(this)
        commandDispatcher = CommandDispatcher(this)
        gameTime = ServerGameTime(this)
    }

    open fun run() {
        levelManager.createLevel(
            configuration.defaultLevelName,
            43337,
            DefaultGenerator.Companion.getInstance()
        ) // Create default level
        tcpServer.run(configuration.address, configuration.port) // Run TCP server
        TickGenerator(GameTime.Companion.TICKS_PER_SECOND.toFloat()).startAsync(this)
    }

    fun stop() {
        val players = playerList.players
        for (player in players!!) {
            player!!.disconnect()
        }
    }

    override fun tick() {
        gameTime.tick()
        for (level in levelManager.getLoadedLevels()) level!!.tick()
    }

    fun broadcast(component: Component?) {
        playerList.broadcastServerMessage(component)
    }
}
