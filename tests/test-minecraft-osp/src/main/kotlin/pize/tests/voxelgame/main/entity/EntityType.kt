package pize.tests.voxelgame.main.entity

import pize.physic.BoundingBox3
import pize.tests.voxelgame.main.level.Level

class EntityType<T : Entity?>(
    private val factory: EntityFactory<T>,
    val iD: Int,
    val boundingBox: BoundingBox3,
    val isSummonable: Boolean
) {

    init {
        entityMap[iD] = this
    }

    fun createEntity(level: Level?): T? {
        return if (!isSummonable) null else factory.create(level)
    }

    companion object {
        val entityMap: MutableMap<Int, EntityType<*>> = HashMap()
        fun fromEntityID(entityID: Int): EntityType<*> {
            return entityMap[entityID]!!
        }

        val PLAYER = EntityType(
            { level: Level? -> Player(level, "") }, 1, BoundingBox3(-0.3, 0.0, -0.3, 0.3, 1.8, 0.3), false
        )
    }
}
