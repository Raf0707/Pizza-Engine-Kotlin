package pize.graphics.texture

import pize.files.Resource
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO

object PixmapIO {
    @JvmOverloads
    fun load(bufferedImage: BufferedImage, invX: Boolean = false, invY: Boolean = false): Pixmap {
        val width = bufferedImage.width
        val height = bufferedImage.height
        val pixmap = Pixmap(width, height)
        val buffer = pixmap.buffer
        val pixels = IntArray(width * height)
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixelIndex = (if (!invY) y else height - 1 - y) * width + if (!invX) x else width - 1 - x
                val pixel = pixels[pixelIndex]
                buffer!!.put((pixel shr 16 and 0xFF).toByte())
                buffer.put((pixel shr 8 and 0xFF).toByte())
                buffer.put((pixel and 0xFF).toByte())
                buffer.put((pixel shr 24 and 0xFF).toByte())
            }
        }
        buffer!!.flip()
        return pixmap
    }

    @JvmOverloads
    fun load(res: Resource, invX: Boolean = false, invY: Boolean = false): Pixmap {
        return try {
            load(ImageIO.read(res.inStream()), invX, invY)
        } catch (e: IOException) {
            throw RuntimeException("Pixmap " + res.inStream().toString() + " does not exists")
        }
    }

    @JvmOverloads
    fun load(filepath: String, invX: Boolean = false, invY: Boolean = false): Pixmap {
        return load(Resource(filepath), invX, invY)
    }

    fun save(pixmap: Pixmap, output: OutputStream?) {
        val bufferedImage = BufferedImage(pixmap.width, pixmap.height, BufferedImage.TYPE_INT_ARGB)
        for (x in 0 until pixmap.width) for (y in 0 until pixmap.height) bufferedImage.setRGB(
            x,
            y,
            pixmap.getPixelBGRA(x, y)
        )
        try {
            ImageIO.write(bufferedImage, "PNG", output)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun save(pixmap: Pixmap, output: File?) {
        try {
            save(pixmap, FileOutputStream(output))
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
    }

    fun save(pixmap: Pixmap, path: String) {
        val resource = Resource(path, true)
        resource.mkParentDirs()
        resource.create()
        save(pixmap, resource.file)
    }
}
