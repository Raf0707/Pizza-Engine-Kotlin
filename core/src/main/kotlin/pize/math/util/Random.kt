package pize.math.util

import java.util.Random

class Random(seed: Long) {
    private val random: Random

    init {
        random = Random(seed)
    }

    fun random(): Float {
        return random.nextFloat()
    }

    fun random(start: Int, end: Int): Int {
        return start + random.nextInt(end - start + 1)
    }

    fun random(start: Float, end: Float): Float {
        return start + random.nextFloat() * (end - start)
    }

    fun random(start: Long, end: Long): Long {
        var start = start
        var end = end
        val rand = random.nextLong()
        if (end < start) {
            val t = end
            end = start
            start = t
        }
        val bound = end - start + 1L
        val randLow = rand and 0xFFFFFFFFL
        val boundLow = bound and 0xFFFFFFFFL
        val randHigh = rand ushr 32
        val boundHigh = bound ushr 32
        return start + (randHigh * boundLow ushr 32) + (randLow * boundHigh ushr 32) + randHigh * boundHigh
    }

    fun randomBoolean(): Boolean {
        return random.nextBoolean()
    }

    fun randomBoolean(chance: Float): Boolean {
        return random.nextFloat() < chance
    }

    fun randomBoolean(chance: Double): Boolean {
        return random.nextDouble() < chance
    }

    fun randomSign(): Int {
        return 1 or (random.nextInt() shr 31)
    }

    fun randomBytes(bytes: ByteArray?) {
        random.nextBytes(bytes)
    }
}
