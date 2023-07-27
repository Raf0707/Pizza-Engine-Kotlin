package pize.audio

import org.lwjgl.openal.AL10
import pize.math.vecmath.vector.Vec3d
import pize.math.vecmath.vector.Vec3f

object AudioListener {
    fun setSpeed(speed: Vec3f) {
        AL10.alListener3f(AL10.AL_VELOCITY, speed.x, speed.y, speed.z)
    }

    fun setSpeed(speed: Vec3d) {
        AL10.alListener3f(AL10.AL_VELOCITY, speed.x.toFloat(), speed.y.toFloat(), speed.z.toFloat())
    }

    fun setPosition(position: Vec3f) {
        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z)
    }

    fun setPosition(position: Vec3d) {
        AL10.alListener3f(AL10.AL_POSITION, position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
    }

    fun setOrientation(at: Vec3f, up: Vec3f) {
        val orientation = FloatArray(6)
        orientation[0] = at.x
        orientation[1] = at.y
        orientation[2] = at.z
        orientation[3] = up.x
        orientation[4] = up.y
        orientation[5] = up.z
        AL10.alListenerfv(AL10.AL_ORIENTATION, orientation)
    }
}
