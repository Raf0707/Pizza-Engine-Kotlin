package pize.tests.voxelgame.server.net

import pize.net.tcp.packet.PacketHandler
import pize.tests.voxelgame.main.net.packet.*

interface ServerPlayerPacketHandler : PacketHandler {
    fun handleChunkRequest(packet: SBPacketChunkRequest)
    fun handlePlayerBlockSet(packet: SBPacketPlayerBlockSet)
    fun handleMove(packet: SBPacketMove)
    fun handleRenderDistance(packet: SBPacketRenderDistance)
    fun handleSneaking(packet: SBPacketPlayerSneaking)
    fun handleChatMessage(packet: SBPacketChatMessage)
}
