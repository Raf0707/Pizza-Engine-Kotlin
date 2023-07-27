package pize.tests.voxelgame.server

import pize.tests.voxelgame.main.Version

class ServerConfiguration {
    var address: String? = null
        private set
    var port = 0
        private set
    var version: Version? = null
        private set
    var defaultLevelName: String? = null
        private set
    var maxRenderDistance = 0
        private set

    fun loadDefaults() {
        port = 22854
        address = "0.0.0.0"
        version = Version()
        defaultLevelName = "world1"
        maxRenderDistance = 12
    }
}
