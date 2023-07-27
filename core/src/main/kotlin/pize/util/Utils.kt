package pize.util

import org.lwjgl.PointerBuffer
import org.lwjgl.system.libc.LibCStdlib
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.*

object Utils {
    fun delay(millis: Long, nanos: Int) {
        try {
            Thread.sleep(millis, nanos)
        } catch (ignored: InterruptedException) {
        }
    }

    @JvmStatic
    fun delayMillis(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ignored: InterruptedException) {
        }
    }

    fun delayNanos(nanos: Int) {
        try {
            Thread.sleep(0, nanos)
        } catch (ignored: InterruptedException) {
        }
    }

    fun close(stream: InputStream) {
        try {
            stream.close()
        } catch (ignored: IOException) {
        }
    }

    fun close(stream: OutputStream) {
        try {
            stream.close()
        } catch (ignored: IOException) {
        }
    }

    fun close(socket: Socket) {
        try {
            socket.close()
        } catch (ignored: IOException) {
        }
    }

    fun close(socket: ServerSocket) {
        try {
            socket.close()
        } catch (ignored: IOException) {
        }
    }

    @JvmStatic
    fun delayElapsed(millis: Long) {
        val current = System.currentTimeMillis()
        while (System.currentTimeMillis() - current < millis);
    }

    fun free(buffer: ByteBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: ShortBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: IntBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: LongBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: FloatBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: DoubleBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun free(buffer: PointerBuffer?) {
        LibCStdlib.free(buffer)
    }

    fun invokeStatic(targetClass: Class<*>, name: String?, vararg args: Any?) {
        try {
            val method = targetClass.getDeclaredMethod(name)
            method.setAccessible(true)
            method.invoke(null, *args)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
