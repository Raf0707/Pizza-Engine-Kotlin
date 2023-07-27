package pize.tests.voxelgame.main.level

import pize.tests.voxelgame.client.block.BlockProperties
import pize.tests.voxelgame.main.Tickable
import pize.tests.voxelgame.main.block.BlockData
import pize.tests.voxelgame.main.chunk.LevelChunk
import pize.tests.voxelgame.main.entity.Entity
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class Level : Tickable {
    private val entityMap: MutableMap<UUID?, Entity>

    init {
        entityMap = ConcurrentHashMap()
    }

    override fun tick() {
        for (entity in entityMap.values) entity.tick()
    }

    val entities: Collection<Entity>
        get() = entityMap.values

    fun getEntity(uuid: UUID?): Entity? {
        return entityMap[uuid]
    }

    fun addEntity(entity: Entity) {
        entityMap[entity.uuid] = entity
    }

    fun removeEntity(entity: Entity) {
        removeEntity(entity.uuid)
    }

    fun removeEntity(entityUUID: UUID?) {
        entityMap.remove(entityUUID)
    }

    abstract fun getBlock(x: Int, y: Int, z: Int): Short
    fun getBlockProps(x: Int, y: Int, z: Int): BlockProperties? {
        return BlockData.getProps(getBlock(x, y, z))
    }

    abstract fun setBlock(x: Int, y: Int, z: Int, block: Short): Boolean
    abstract fun getHeight(x: Int, z: Int): Int
    abstract fun setLight(x: Int, y: Int, z: Int, level: Int)
    abstract fun getLight(x: Int, y: Int, z: Int): Byte
    abstract val configuration: LevelConfiguration
    abstract val chunkManager: ChunkManager
    abstract fun getBlockChunk(blockX: Int, blockZ: Int): LevelChunk?
}
