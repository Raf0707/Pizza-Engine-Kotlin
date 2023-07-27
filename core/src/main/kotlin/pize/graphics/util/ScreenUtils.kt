package pize.graphics.util

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import pize.Pize
import pize.files.Resource
import pize.graphics.gl.Format
import pize.graphics.gl.Type
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.ByteOrder
import javax.imageio.ImageIO

object ScreenUtils {
    fun saveScreenshot(file: File?, format: String?) {
        val width = Pize?.width!!
        val height = Pize?.height!!
        val buffer = BufferUtils.createByteBuffer(width * height * 4).order(ByteOrder.LITTLE_ENDIAN)
        GL11.glReadPixels(0, 0, width, height, Format.BGRA.GL, Type.UNSIGNED_BYTE.GL, buffer)
        val pixels = IntArray(width * height)
        buffer.asIntBuffer()[pixels]
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until height) for (x in 0 until width) bufferedImage.setRGB(
            x,
            height - y - 1,
            pixels[y * width + x]
        )
        try {
            ImageIO.write(bufferedImage, format, file)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun saveScreenshot(filepath: String) {
        val resource = Resource(filepath)
        saveScreenshot(resource.file, resource?.extension!!)
    }
}
