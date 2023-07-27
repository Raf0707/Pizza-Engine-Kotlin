package pize.tests.minecraft.game.audio

enum class MusicSet private constructor(vararg set: String) {
    MAIN_MENU("Beginning 2", "Moog City 2", "Floating Trees", "Mutation");

    private val set: Array<String>

    init {
        this.set = set as Array<String>
    }

    operator fun get(index: Int): String {
        return set[index]
    }

    fun size(): Int {
        return set.size
    }
}
