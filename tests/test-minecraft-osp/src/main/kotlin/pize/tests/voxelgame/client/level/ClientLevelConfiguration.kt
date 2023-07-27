package pize.tests.voxelgame.client.level

import pize.tests.voxelgame.main.level.LevelConfiguration

class ClientLevelConfiguration : LevelConfiguration() {
    override fun load(name: String?) {
        super.load(name)
    }

    override var name: String?
        get() = super.name
        set(name) {
            this.name = name
        }
}