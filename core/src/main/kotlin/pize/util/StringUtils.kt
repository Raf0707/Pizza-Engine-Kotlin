package pize.util

object StringUtils {
    @JvmStatic
    fun isBlank(string: String?): Boolean {
        var length: Int = string?.length!!
        if (string == null || length == 0) return true
        for (i in 0 until length) if (!Character.isWhitespace(string[i])) return false
        return true
    }

    fun count(string: String, pattern: String): Int {
        var count = 0
        var i = 0
        while (i < string.length - pattern.length + 1) {
            if (string.startsWith(pattern, i)) count++
            i += pattern.length
        }
        return count
    }
}
