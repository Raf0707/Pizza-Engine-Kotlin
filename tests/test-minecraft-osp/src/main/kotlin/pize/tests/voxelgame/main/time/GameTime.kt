package pize.tests.voxelgame.main.time

import pize.math.Maths.floor
import pize.math.Maths.frac
import pize.tests.voxelgame.main.Tickable

open class GameTime : Tickable {
    open var ticks: Long = 0
    override fun tick() {
        ticks++
    }

    val seconds: Float
        get() = ticks.toFloat() / TICKS_IN_SECOND
    val minutes: Float
        get() = ticks.toFloat() / TICKS_IN_MINUTE
    val days: Float
        get() = ticks.toFloat() / TICKS_IN_DAY
    val dayNumber: Int
        get() = floor(days.toDouble())
    val time: String
        get() = String.format("%02d", floor(dayMinutes.toDouble())) + ":" + String.format(
            "%02d", floor(
                (frac(
                    dayMinutes
                ) * SECONDS_IN_MINUTE).toDouble()
            )
        )

    fun setSeconds(seconds: Double) {
        ticks = Math.round(seconds * TICKS_IN_SECOND)
    }

    fun setMinutes(minutes: Double) {
        ticks = Math.round(minutes * TICKS_IN_MINUTE)
    }

    fun setDays(days: Double) {
        ticks = Math.round(days * TICKS_IN_DAY)
    }

    fun setTime(time: Float, unit: TimeUnit?) {
        when (unit) {
            TimeUnit.TICKS -> ticks = time.toLong()
            TimeUnit.SECONDS -> setSeconds(time.toDouble())
            TimeUnit.MINUTES -> setMinutes(time.toDouble())
            TimeUnit.DAYS -> setDays(time.toDouble())
        }
    }

    var dayTicks: Long
        get() = ticks % TICKS_IN_DAY
        set(ticks) {
            this.ticks = floorDayTicks + ticks
        }
    val daySeconds: Float
        get() = dayTicks.toFloat() / TICKS_IN_SECOND
    val dayMinutes: Float
        get() = dayTicks.toFloat() / TICKS_IN_MINUTE
    val floorDayTicks: Long
        get() = Math.floorDiv(ticks, TICKS_IN_DAY) * TICKS_IN_DAY

    fun setDaySeconds(seconds: Double) {
        dayTicks = Math.round(seconds * TICKS_IN_SECOND)
    }

    fun setDayMinutes(minutes: Double) {
        dayTicks = Math.round(minutes * TICKS_IN_MINUTE)
    }

    fun setDayTime(time: Float, unit: TimeUnit?) {
        when (unit) {
            TimeUnit.TICKS -> dayTicks = time.toLong()
            TimeUnit.SECONDS -> setDaySeconds(time.toDouble())
            TimeUnit.MINUTES -> setDayMinutes(time.toDouble())
        }
    }

    fun setDay() {
        dayTicks = TIME_DAY.toLong()
    }

    fun setMidnight() {
        dayTicks = TIME_MIDNIGHT.toLong()
    }

    fun setNight() {
        dayTicks = TIME_NIGHT.toLong()
    }

    fun setNoon() {
        dayTicks = TIME_NOON.toLong()
    }

    companion object {
        const val TICKS_PER_SECOND = 20
        const val TICKS_IN_SECOND = 20
        const val SECONDS_IN_MINUTE = 60
        const val TICKS_IN_MINUTE = TICKS_IN_SECOND * SECONDS_IN_MINUTE
        const val MINUTES_IN_DAY = 20
        const val TICKS_IN_DAY = TICKS_IN_MINUTE * MINUTES_IN_DAY
        const val TIME_DAY = 1000
        const val TIME_MIDNIGHT = 18000
        const val TIME_NIGHT = 13000
        const val TIME_NOON = 6000
    }
}
