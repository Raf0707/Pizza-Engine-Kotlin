package pize.tests.voxelgame.main.level

open class LevelConfiguration {
    var name: String? = null
        protected set

    open fun load(name: String?) {
        this.name = name
    }
}
