package pize.util.io

import pize.graphics.util.color.IColor
import pize.math.util.EulerAngles
import pize.math.vecmath.vector.*
import java.io.*
import java.util.*

class PizeOutputStream(out: OutputStream?) : DataOutputStream(out) {
    @Throws(IOException::class)
    fun writeShortArray(shortArray: ShortArray) {
        val bytes = ByteArray(shortArray.size * 2)
        for (i in shortArray.indices) {
            val j = shortArray[i]
            bytes[i * 2] = (j.toInt() shr 8 and 0xFF).toByte()
            bytes[i * 2 + 1] = (j.toInt() and 0xFF).toByte()
        }
        writeByteArray(bytes)
    }

    @Throws(IOException::class)
    fun writeByteArray(byteArray: ByteArray) {
        writeInt(byteArray.size)
        write(byteArray)
    }

    @Throws(IOException::class)
    fun writeVec2i(vector: Vec2i) {
        writeInt(vector.x)
        writeInt(vector.y)
    }

    @Throws(IOException::class)
    fun writeVec2f(vector: Vec2f) {
        writeFloat(vector.x)
        writeFloat(vector.y)
    }

    @Throws(IOException::class)
    fun writeVec2d(vector: Vec2d) {
        writeDouble(vector.x)
        writeDouble(vector.y)
    }

    @Throws(IOException::class)
    fun writeVec3i(vector: Vec3i) {
        writeInt(vector.x)
        writeInt(vector.y)
        writeInt(vector.z)
    }

    @Throws(IOException::class)
    fun writeVec3f(vector: Vec3f) {
        writeFloat(vector.x)
        writeFloat(vector.y)
        writeFloat(vector.z)
    }

    @Throws(IOException::class)
    fun writeVec3d(vector: Vec3d) {
        writeDouble(vector.x)
        writeDouble(vector.y)
        writeDouble(vector.z)
    }

    @Throws(IOException::class)
    fun writeEulerAngles(vector: EulerAngles) {
        writeFloat(vector.yaw)
        writeFloat(vector.pitch)
        writeFloat(vector.roll)
    }

    @Throws(IOException::class)
    fun writeUUID(uuid: UUID) {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
    }

    @Throws(IOException::class)
    fun writeColor(color: IColor) {
        writeFloat(color.r())
        writeFloat(color.g())
        writeFloat(color.b())
        writeFloat(color.a())
    }
}
