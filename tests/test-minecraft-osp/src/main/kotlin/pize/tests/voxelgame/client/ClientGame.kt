package pize.tests.voxelgame.client

import pize.Pize.execSync
import pize.math.vecmath.vector.Vec3f
import pize.math.vecmath.vector.Vec3f.equals
import pize.net.security.KeyAES
import pize.net.tcp.TcpClient
import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.Minecraft
import pize.tests.voxelgame.client.chat.Chat
import pize.tests.voxelgame.client.control.BlockRayCast
import pize.tests.voxelgame.client.control.camera.GameCamera
import pize.tests.voxelgame.client.entity.LocalPlayer
import pize.tests.voxelgame.client.level.ClientLevel
import pize.tests.voxelgame.client.net.ClientPacketHandler
import pize.tests.voxelgame.client.renderer.particle.Particle
import pize.tests.voxelgame.main.Tickable
import pize.tests.voxelgame.main.net.packet.SBPacketLogin
import pize.tests.voxelgame.main.net.packet.SBPacketMove
import pize.tests.voxelgame.main.time.GameTime

class ClientGame(val session: Minecraft) : Tickable {
    private val client: TcpClient
    val chat: Chat
    val encryptKey: KeyAES
    val blockRayCast: BlockRayCast
    val time: GameTime
    var level: ClientLevel? = null
        private set
    var player: LocalPlayer? = null
        private set
    var camera: GameCamera? = null
        private set

    init {
        client = TcpClient(ClientPacketHandler(this))
        encryptKey = KeyAES(256)
        blockRayCast = BlockRayCast(session, 2000f)
        chat = Chat(this)
        time = GameTime()
    }

    fun connect(address: String, port: Int) {
        println("[Client]: Connect to $address:$port")
        client.connect(address, port)
        client.connection!!.setTcpNoDelay(true)
        sendPacket(SBPacketLogin(session.version.id, session.profile.name))
    }

    override fun tick() {
        if (level == null || player == null) return
        time.tick()
        player!!.tick()
        level!!.tick()
        if (player!!.isPositionChanged || player!!.isRotationChanged) sendPacket(SBPacketMove(player!!))
        if (time.ticks % GameTime.Companion.TICKS_IN_SECOND == 0L) { //: HARAM
            tx = txCounter
            txCounter = 0
            ClientPacketHandler.Companion.rx = ClientPacketHandler.Companion.rxCounter
            ClientPacketHandler.Companion.rxCounter = 0
        }
    }

    fun update() {
        if (camera == null) return
        player!!.updateInterpolation()
        blockRayCast.update()
        camera!!.update()
    }

    fun createClientLevel(worldName: String?) {
        if (level != null) execSync {
            level.getConfiguration().name = worldName
            level.getChunkManager().reset()
        } else {
            level = ClientLevel(session, worldName)
            blockRayCast.setLevel(level)
        }
    }

    fun spawnPlayer(position: Vec3f?) {
        if (level == null) return
        player = LocalPlayer(level, session.profile.name)
        player!!.position.set(position!!)
        camera = GameCamera(this, 0.1, 5000.0, session.options.fieldOfView.toDouble())
        camera!!.setDistance(session.options.renderDistance)
        session.controller.playerController.setTargetPlayer(player)
    }

    fun sendPacket(packet: IPacket<*>) {
        packet.write(client.connection)
        txCounter++
    }

    fun disconnect() {
        client.disconnect()
        if (level != null) execSync {
            println(10000)
            level.getChunkManager().dispose()
        }
    }

    fun spawnParticle(particle: Particle?, position: Vec3f) {
        val particleBatch = session.renderer.worldRenderer.particleBatch
        particleBatch!!.spawnParticle(particle, position)
    }

    companion object {
        var tx = 0
        private var txCounter = 0
    }
}
