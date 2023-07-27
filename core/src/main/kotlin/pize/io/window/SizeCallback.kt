package pize.io.window

fun interface SizeCallback {
    operator fun invoke(width: Int, height: Int)
}
