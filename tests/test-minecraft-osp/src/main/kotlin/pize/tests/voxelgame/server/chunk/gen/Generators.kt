package pize.tests.voxelgame.server.chunk.gen

object Generators {
    private val fromID: MutableMap<String?, ChunkGenerator?> = HashMap()
    fun register(generator: ChunkGenerator?) {
        fromID[generator.getID()] = generator
    }

    fun fromID(generatorID: String?): ChunkGenerator? {
        if (fromID.size == 0) {
            register(DefaultGenerator.Companion.getInstance())
            register(FlatGenerator.Companion.getInstance())
            register(IslandsGenerator.Companion.getInstance())
        }
        return fromID[generatorID]
    }
}
