package pize.tests.voxelgame.server.level

import pize.tests.voxelgame.server.Server
import pize.tests.voxelgame.server.chunk.gen.ChunkGenerator

class LevelManager(val server: Server) {
    val loadedLevels: MutableMap<String?, ServerLevel>

    init {
        loadedLevels = HashMap()
    }

    fun getLoadedLevels(): Collection<ServerLevel> {
        return loadedLevels.values
    }

    fun getLevel(worldName: String?): ServerLevel? {
        return loadedLevels[worldName]
    }

    val defaultLevel: ServerLevel?
        get() = getLevel(server.configuration.defaultLevelName)

    fun loadLevel(levelName: String?) {
        if (levelName == null || isLevelExists(levelName) || isLevelLoaded(levelName)) return

        // final ServerLevel level = new ServerLevel(server);
        // level.getConfiguration().load(levelName, seed, generator);
        // loadedLevels.put(levelName, level);
        // level.getChunkManager().start();
        println("[Server]: Loaded level '$levelName'")
    }

    fun createLevel(levelName: String?, seed: Int, generator: ChunkGenerator?) {
        if (levelName == null || !isLevelExists(levelName) || isLevelLoaded(levelName)) return
        val level = ServerLevel(server)
        level.configuration.load(levelName, seed, generator)
        loadedLevels[levelName] = level
        level.chunkManager.start()
        println("[Server]: Created level '$levelName'")
    }

    fun isLevelLoaded(levelName: String?): Boolean {
        return loadedLevels.containsKey(levelName)
    }

    fun isLevelExists(levelName: String?): Boolean {
        return true //: SHO? SHIZA.
    }
}
