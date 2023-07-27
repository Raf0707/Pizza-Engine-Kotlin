package pize.tests.terraria.indev

abstract class ITile(private val id: String?) {
    fun get(): String? {
        return id
    }
}
