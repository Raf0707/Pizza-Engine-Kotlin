package pize.app

abstract class AppAdapter : Resizable, Disposable {
    open fun init() {}
    open fun render() {}
    open fun update() {}
    open fun fixedUpdate() {}
    override fun resize(width: Int, height: Int) {}
    override fun dispose() {}
}
