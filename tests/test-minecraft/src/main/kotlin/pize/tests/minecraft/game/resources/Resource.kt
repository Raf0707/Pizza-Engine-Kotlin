package pize.tests.minecraft.game.resources

import pize.app.Disposable

abstract class Resource<T>(var location: String) : Disposable {

    abstract fun loadResource()
    abstract fun reloadResource()
    abstract val isLoaded: Boolean
    abstract val resource: T
}
