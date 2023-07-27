package pize.tests.voxelgame.server

import pize.tests.voxelgame.Minecraft

class IntegratedServer(val session: Minecraft) : Server() {

    init {
        configuration.loadDefaults() // Load server configuration
    }

    override fun run() {
        super.run()
        println("[Server]: Integrated server listening on " + configuration.address + ":" + configuration.port)
    }
}
