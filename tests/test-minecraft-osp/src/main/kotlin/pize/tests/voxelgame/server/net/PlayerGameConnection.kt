package pize.tests.voxelgame.server.net

import pize.graphics.texture.Region.set
import pize.graphics.util.color.Color.set
import pize.math.util.EulerAngles.set
import pize.math.vecmath.matrix.Matrix4f.set
import pize.math.vecmath.vector.Vec2f.set
import pize.math.vecmath.vector.Vec3f.set
import pize.math.vecmath.vector.Vec3i.set
import pize.net.tcp.TcpConnection
import pize.net.tcp.packet.IPacket
import pize.tests.voxelgame.main.audio.BlockSoundPack
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.block.BlockSetType
import pize.tests.voxelgame.main.chunk.storage.ChunkPos
import pize.tests.voxelgame.main.command.source.CommandSourcePlayer
import pize.tests.voxelgame.main.net.packet.*
import pize.tests.voxelgame.main.text.Component
import pize.tests.voxelgame.main.text.TextColor
import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.player.ServerPlayer

class PlayerGameConnection(val player: ServerPlayer, val connection: TcpConnection?) : ServerPlayerPacketHandler {
    private val server: Server?
    private val commandSource: CommandSourcePlayer

    init {
        server = player.server
        commandSource = CommandSourcePlayer(player)
    }

    fun sendPacket(packet: IPacket<*>) {
        packet.write(connection)
    }

    override fun handleChunkRequest(packet: SBPacketChunkRequest) {
        val level = player.level
        level.chunkManager.requestedChunk(
            player,
            ChunkPos(packet.chunkX, packet.chunkZ)
        )
    }

    override fun handlePlayerBlockSet(packet: SBPacketPlayerBlockSet) {
        val oldBlock = player.level.getBlockProps(packet.x, packet.y, packet.z)

        // Set Block on the Server //
        val result = player.level.setBlock(packet.x, packet.y, packet.z, packet.state)
        if (!result) return
        val block = BlockData.getProps(packet.state)
        val setType: BlockSetType = BlockSetType.Companion.from(oldBlock.isEmpty, block.isEmpty)

        // Send Set Block packet //
        server.getPlayerList()
            .broadcastPacketExcept(CBPacketBlockUpdate(packet.x, packet.y, packet.z, packet.state), player)

        // Sound //
        val soundPack: BlockSoundPack?
        soundPack = if (setType.ordinal > 0) block.soundPack else oldBlock.soundPack
        if (soundPack != null) player.level.playSound(
            soundPack.randomDestroySound(),
            1f,
            1f,
            packet.x + 0.5f,
            packet.y + 0.5f,
            packet.z + 0.5f
        )
    }

    override fun handleMove(packet: SBPacketMove) {
        player.position.set(packet.position!!)
        player.rotation.set(packet.rotation)
        player.velocity.set(packet.velocity)
        server.getPlayerList().broadcastPacketLevelExcept(player.level, CBPacketEntityMove(player), player)
    }

    override fun handleRenderDistance(packet: SBPacketRenderDistance) {
        player.renderDistance = packet.renderDistance
    }

    override fun handleSneaking(packet: SBPacketPlayerSneaking) {
        player.setSneaking(packet.sneaking)
        server.getPlayerList().broadcastPacketExcept(CBPacketPlayerSneaking(player), player)
    }

    override fun handleChatMessage(packet: SBPacketChatMessage) {
        val message = packet.message
        if (message!!.startsWith("/")) server.getCommandDispatcher()
            .executeCommand(message.substring(1), commandSource) else player.sendToChat(
            Component().color(TextColor.DARK_GREEN).text("<" + player.name + "> ").reset().text(packet.message)
        )
    }
}
