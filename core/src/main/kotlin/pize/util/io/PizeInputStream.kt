package pize.util.io

import pize.graphics.util.color.Color
import pize.graphics.util.color.ImmutableColor
import pize.math.util.EulerAngles
import pize.math.vecmath.vector.*
import java.io.*
import java.util.*

class PizeInputStream(`in`: InputStream?) : DataInputStream(`in`) {
    @Throws(IOException::class)
    fun readShortArray(): ShortArray {
        val bytes = readByteArray()
        val shortArray = ShortArray(bytes.size / 2)
        for (i in shortArray.indices) shortArray[i] =
            (bytes[i * 2].toInt() and (0xFF shl 8) or (bytes[i * 2 + 1].toInt() and 0xFF)).toShort()
        return shortArray
    }

    @Throws(IOException::class)
    fun readByteArray(): ByteArray {
        return readNBytes(readInt())
    }

    @Throws(IOException::class)
    fun readVec2i(): Vec2i {
        return Vec2i(
            readInt(),
            readInt()
        )
    }

    @Throws(IOException::class)
    fun readVec2f(): Vec2f {
        return Vec2f(
            readFloat(),
            readFloat()
        )
    }

    @Throws(IOException::class)
    fun readVec2d(): Vec2d {
        return Vec2d(
            readDouble(),
            readDouble()
        )
    }

    @Throws(IOException::class)
    fun readVec3i(): Vec3i {
        return Vec3i(
            readInt(),
            readInt(),
            readInt()
        )
    }

    @Throws(IOException::class)
    fun readVec3f(): Vec3f {
        return Vec3f(
            readFloat(),
            readFloat(),
            readFloat()
        )
    }

    @Throws(IOException::class)
    fun readVec3d(): Vec3d {
        return Vec3d(
            readDouble(),
            readDouble(),
            readDouble()
        )
    }

    @Throws(IOException::class)
    fun readEulerAngles(): EulerAngles {
        return EulerAngles(
            readFloat(),
            readFloat(),
            readFloat()
        )
    }

    @Throws(IOException::class)
    fun readUUID(): UUID {
        return UUID(readLong(), readLong())
    }

    @Throws(IOException::class)
    fun readColor(): Color {
        return Color(
            readFloat(),
            readFloat(),
            readFloat(),
            readFloat()
        )
    }

    @Throws(IOException::class)
    fun readImmutableColor(): ImmutableColor {
        return ImmutableColor(
            readFloat(),
            readFloat(),
            readFloat(),
            readFloat()
        )
    }
}
