package pize.tests.voxelgame.server.level

import pize.math.vecmath.vector.Vec2f
import pize.tests.voxelgame.main.level.LevelConfiguration
import pize.tests.voxelgame.server.chunk.gen.ChunkGenerator

class ServerLevelConfiguration : LevelConfiguration() {
    var generator: ChunkGenerator? = null
        private set
    var worldSpawn: Vec2f? = null
        private set
    var seed = 0
        private set

    fun load(name: String?, seed: Int, generator: ChunkGenerator?) {
        super.load(name)
        this.seed = seed
        this.generator = generator
        worldSpawn = Vec2f(-717f, -952f)
    }

    fun setWorldSpawn(x: Double, z: Double) {
        worldSpawn!![x] = z
    }
}
