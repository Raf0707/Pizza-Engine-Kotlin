package pize.io.keyboard

fun interface CharCallback {
    operator fun invoke(character: Char)
}
