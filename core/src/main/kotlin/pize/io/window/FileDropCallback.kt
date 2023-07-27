package pize.io.window

fun interface FileDropCallback {
    operator fun invoke(count: Int, names: Array<String?>?)
}
