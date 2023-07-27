package pize.tests.voxelgame.main.entity

import pize.tests.voxelgame.main.level.Level

interface EntityFactory<T : Entity?> {
    fun create(level: Level?): T
}
