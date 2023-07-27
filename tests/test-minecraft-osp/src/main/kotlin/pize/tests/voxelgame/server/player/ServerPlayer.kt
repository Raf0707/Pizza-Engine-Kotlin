package pize.tests.voxelgame.server.player

import pize.math.util.EulerAngles
import pize.math.vecmath.vector.Vec3f
import pize.net.tcp.TcpConnection
import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.main.audio.Sound
import pize.tests.voxelgame.main.entity.Entity
import pize.tests.voxelgame.main.entity.Player
import pize.tests.voxelgame.main.level.Level
import pize.tests.voxelgame.main.net.packet.CBPacketAbilities
import pize.tests.voxelgame.main.net.packet.CBPacketChatMessage
import pize.tests.voxelgame.main.net.packet.CBPacketPlaySound
import pize.tests.voxelgame.main.net.packet.CBPacketTeleportPlayer
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.level.ServerLevel
import pize.tests.voxelgame.server.net.PlayerGameConnection

class ServerPlayer(level: ServerLevel?, connection: TcpConnection?, name: String?) : Player(level, name) {
    val server: Server?
    val connectionAdapter: PlayerGameConnection
    var renderDistance: Int

    init {
        server = level.getServer()
        connectionAdapter = PlayerGameConnection(this, connection)
        renderDistance = server.getConfiguration().maxRenderDistance //: 0
    }

    override var level: Level?
        get() = super.getLevel() as ServerLevel
        set(level) {
            super.level = level
        }

    fun playSound(sound: Sound, volume: Float, pitch: Float) {
        sendPacket(CBPacketPlaySound(sound, volume, pitch, position))
    }

    @JvmOverloads
    fun teleport(level: Level?, position: Vec3f?, rotation: EulerAngles? = getRotation()) {
        sendPacket(CBPacketTeleportPlayer(level.getConfiguration().name, position, rotation))
        val oldLevel = this.level
        if (level !== oldLevel) {
            setLevel(level)
            oldLevel!!.removeEntity(this)
            level!!.addEntity(this)
        }
        position!!.set(position)
        getRotation().set(rotation)
    }

    fun teleport(entity: Entity?) {
        teleport(entity.getLevel(), entity!!.position, entity.rotation)
    }

    fun teleport(position: Vec3f?, rotation: EulerAngles?) {
        teleport(this.level, position, rotation)
    }

    fun teleport(position: Vec3f?) {
        teleport(this.level, position, rotation)
    }

    override fun setFlyEnabled(flyEnabled: Boolean) {
        sendPacket(CBPacketAbilities(flyEnabled))
        super.setFlyEnabled(flyEnabled)
    }

    fun disconnect() {
        connectionAdapter.connection.close()
    }

    fun sendToChat(message: Component?) {
        server.getPlayerList().broadcastPlayerMessage(this, message)
    }

    fun sendMessage(message: Component?) {
        sendPacket(CBPacketChatMessage(message!!.toFlatList()))
    }

    fun sendPacket(packet: IPacket<*>) {
        connectionAdapter.sendPacket(packet)
    }
}
