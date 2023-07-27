package pize.math.util

import pize.math.Mathc
import pize.math.Maths
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3d
import pize.math.vecmath.vector.Vec3f
import kotlin.math.sqrt

class EulerAngles {
    @JvmField
    var pitch = 0f
    @JvmField
    var yaw = 0f
    @JvmField
    var roll = 0f

    constructor()
    constructor(yaw: Float, pitch: Float) {
        set(yaw, pitch)
    }

    constructor(yaw: Float, pitch: Float, roll: Float) {
        set(yaw, pitch, roll)
    }

    constructor(eulerAngles: EulerAngles) {
        set(eulerAngles)
    }

    fun constrain() {
        if (yaw >= 360) yaw -= 360f else if (yaw <= -360) yaw += 360f
    }

    fun limitPitch90() {
        if (pitch > 90) pitch = 90f else if (pitch < -90) pitch = -90f
    }

    val direction: Vec3f
        get() {
            val cosPitch = Maths.cosDeg(pitch.toDouble())
            return Vec3f(
                cosPitch * Maths.cosDeg(yaw.toDouble()),
                Maths.sinDeg(pitch.toDouble()),
                cosPitch * Maths.sinDeg(yaw.toDouble())
            )
        }
    val directionHorizontal: Vec3f
        get() = Vec3f(Maths.cosDeg(yaw.toDouble()), 0f, Maths.sinDeg(yaw.toDouble()))

    fun setDirection(x: Double, y: Double, z: Double): EulerAngles {
        yaw = -Mathc.atan2(x, z) * Maths.ToDeg + 90
        pitch = Mathc.atan2(y, sqrt(x * x + z * z)) * Maths.ToDeg
        return this
    }

    fun setDirection(dir: Vec3d): EulerAngles {
        return setDirection(dir.x, dir.y, dir.z)
    }

    fun setDirection(dir: Vec3f): EulerAngles {
        return setDirection(dir.x.toDouble(), dir.y.toDouble(), dir.z.toDouble())
    }

    fun toMatrix(): Matrix4f? {
        return Matrix4f() //.toRotatedX(roll)
            //.mul(new Matrix4f()
            .toRotatedY(yaw.toDouble()) //)
            .mul(Matrix4f().toRotatedZ(pitch.toDouble()))
    }

    fun set(eulerAngles: EulerAngles): EulerAngles {
        yaw = eulerAngles.yaw
        pitch = eulerAngles.pitch
        roll = eulerAngles.roll
        return this
    }

    operator fun set(yaw: Float, pitch: Float, roll: Float): EulerAngles {
        this.yaw = yaw
        this.pitch = pitch
        this.roll = roll
        return this
    }

    operator fun set(yaw: Float, pitch: Float): EulerAngles {
        this.yaw = yaw
        this.pitch = pitch
        return this
    }

    fun add(yaw: Float, pitch: Float, roll: Float): EulerAngles {
        this.yaw += yaw
        this.pitch += pitch
        this.roll += roll
        return this
    }

    fun add(yaw: Float, pitch: Float): EulerAngles {
        this.yaw += yaw
        this.pitch += pitch
        return this
    }

    fun lerp(start: EulerAngles, end: EulerAngles, t: Float): EulerAngles {
        yaw = Maths.lerp(start.yaw, end.yaw, t)
        pitch = Maths.lerp(start.pitch, end.pitch, t)
        roll = Maths.lerp(start.roll, end.roll, t)
        return this
    }

    fun copy(): EulerAngles {
        return EulerAngles(this)
    }
}
