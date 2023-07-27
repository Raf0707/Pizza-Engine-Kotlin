package pize.graphics.font

import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBTTAlignedQuad
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype
import org.lwjgl.system.MemoryStack
import pize.files.Resource
import pize.graphics.texture.*
import java.io.IOException
import java.nio.*
import java.nio.file.Path
import java.util.*

object FontLoader {
    private var defaultFont: BitmapFont? = null
    @JvmStatic
    fun loadFnt(filepath: String): BitmapFont {
        val font = BitmapFont()
        val reader = Resource(filepath).reader
        while (reader!!.hasNext()) {
            val tokens = reader.nextLine().trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            when (tokens[0].lowercase(Locale.getDefault())) {
                "info" -> font.isItalic = getValue(tokens[4]).toInt() == 1
                "common" -> font.setLineHeight(getValue(tokens[1]).toInt())
                "page" -> {
                    val id = getValue(tokens[1]).toInt()
                    val relativeTexturePath = getValue(tokens[2]).replace("\"", "")
                    val path = Path.of(filepath)
                    font.addPage(
                        id, Texture(
                            Resource(
                                if (path.parent == null) relativeTexturePath else Path.of(path.parent.toString() + "/" + relativeTexturePath)
                                    .normalize().toString()
                            )
                        )
                    )
                }

                "char" -> {
                    val id = getValue(tokens[1]).toInt()
                    val page = getValue(tokens[9]).toInt()
                    val pageTexture = font.getPage(page)
                    val s0 = getValue(tokens[2]).toInt().toFloat() / pageTexture.width
                    val t0 = getValue(tokens[3]).toInt().toFloat() / pageTexture.height
                    val s1 = getValue(tokens[4]).toInt().toFloat() / pageTexture.width + s0
                    val t1 = getValue(tokens[5]).toInt().toFloat() / pageTexture.height + t0
                    val offsetX = getValue(tokens[6]).toInt()
                    val offsetY = getValue(tokens[7]).toInt()
                    val advanceX = getValue(tokens[8]).toInt()
                    val regionOnTexture = Region(s0, t0, s1, t1)
                    val glyphHeight = regionOnTexture.getHeightPx(font.getPage(page))
                    val glyphWidth = regionOnTexture.getWidthPx(font.getPage(page))
                    font.addGlyph(
                        Glyph(
                            font,
                            id,
                            offsetX.toFloat(),
                            font.lineHeight - offsetY - glyphHeight,
                            glyphWidth,
                            glyphHeight,
                            regionOnTexture,
                            advanceX.toFloat(),
                            page
                        )
                    )
                }
            }
        }
        return font
    }

    private fun getValue(token: String): String {
        return token.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    @JvmStatic
    fun loadTrueType(filepath: String, size: Int, charset: FontCharset): BitmapFont {
        val font = BitmapFont()
        font.setLineHeight(size)
        val width = size * charset.size()
        val height = size * 3
        val data: ByteBuffer
        try {
            Resource(filepath).inStream().use { inStream ->
                val bytes = inStream.readAllBytes()
                data = BufferUtils.createByteBuffer(bytes.size)
                data.put(bytes)
                data.flip()
            }
        } catch (e: IOException) {
            throw Error("Failed to load $filepath ($e)")
        }
        val bitmap = BufferUtils.createByteBuffer(width * height)
        val charData = STBTTBakedChar.malloc(charset.lastChar + 1)
        STBTruetype.stbtt_BakeFontBitmap(data, size.toFloat(), bitmap, width, height, charset.firstChar, charData)
        val buffer = BufferUtils.createByteBuffer(width * height * 4)
        var i = 0
        while (i < buffer.capacity()) {
            buffer.put(255.toByte())
            buffer.put(255.toByte())
            buffer.put(255.toByte())
            buffer.put(bitmap.get())
            i += 4
        }
        buffer.flip()
        val texture = Texture(Pixmap(buffer, width, height))
        font.addPage(0, texture)
        MemoryStack.stackPush().use { stack ->
            // Creating font
            val fontInfo = STBTTFontinfo.create()
            STBTruetype.stbtt_InitFont(fontInfo, data)

            // Getting descent
            val descentBuffer = stack.mallocInt(1)
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, null, descentBuffer, null)
            val descent = descentBuffer.get() * STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, size.toFloat())
            val quad = STBTTAlignedQuad.malloc(stack)
            for (i in 0 until charset.size()) {
                val id = charset.charAt(i).code

                // Getting advanceX
                val advanceXBuffer = stack.floats(0f)
                val advanceYBuffer = stack.floats(0f)
                STBTruetype.stbtt_GetBakedQuad(
                    charData,
                    width,
                    height,
                    id - charset.firstChar,
                    advanceXBuffer,
                    advanceYBuffer,
                    quad,
                    false
                )
                val advanceX = advanceXBuffer.get()

                // Calculating glyph Region on the texture & glyph Width and Height
                val regionOnTexture = Region(quad.s0(), quad.t0(), quad.s1(), quad.t1())
                val glyphHeight = quad.y1() - quad.y0()
                val glyphWidth = quad.x1() - quad.x0()

                // Adding Glyph to the font
                font.addGlyph(
                    Glyph(
                        font,
                        id,
                        quad.x0(),
                        -quad.y0() - glyphHeight - descent,
                        glyphWidth,
                        glyphHeight,
                        regionOnTexture,
                        advanceX,
                        0
                    )
                )
            }
        }
        return font
    }

    @JvmStatic
    fun loadTrueType(filePath: String, size: Int): BitmapFont {
        return loadTrueType(filePath, size, FontCharset.Companion.DEFAULT)
    }

    @JvmStatic
    val default: BitmapFont?
        get() {
            if (defaultFont == null) defaultFont = loadTrueType("font/OpenSans-Regular.ttf", 32)
            return defaultFont
        }
}
