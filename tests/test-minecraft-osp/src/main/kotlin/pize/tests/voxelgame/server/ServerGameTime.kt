package pize.tests.voxelgame.server

import pize.tests.voxelgame.main.net.packet.CBPacketTime
import pize.tests.voxelgame.main.time.GameTime

class ServerGameTime(val server: Server) : GameTime() {

    override var ticks: Long
        get() = super.ticks
        set(ticks) {
            super.setTicks(ticks)
            server.playerList.broadcastPacket(CBPacketTime(ticks))
        }
}
