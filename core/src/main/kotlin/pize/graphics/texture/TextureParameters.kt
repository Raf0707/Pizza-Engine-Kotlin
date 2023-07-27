package pize.graphics.texture

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL46
import pize.graphics.gl.*
import pize.graphics.util.color.Color
import java.nio.ByteBuffer
import kotlin.math.min

class TextureParameters {
    var minFilter: Filter
        private set
    var magFilter: Filter
        private set
    var wrapS: Wrap
        private set
    var wrapT: Wrap
        private set
    var wrapR: Wrap
        private set
    var sizedFormat: SizedFormat
        private set
    var type: Type
        private set
    val borderColor: Color
    var mipmapLevels: Int
        private set
    var lodBias: Float
        private set
    var anisotropyLevels: Float
        private set

    init {
        minFilter = DEFAULT_MIN_FILTER
        magFilter = DEFAULT_MAG_FILTER
        wrapS = DEFAULT_WRAP_S
        wrapT = DEFAULT_WRAP_T
        wrapR = DEFAULT_WRAP_R
        sizedFormat = DEFAULT_FORMAT
        type = DEFAULT_TYPE
        borderColor = Color(0f, 0f, 0f, 0f)
        mipmapLevels = DEFAULT_MIPMAP_LEVELS
        lodBias = DEFAULT_LOD_BIAS
        anisotropyLevels = DEFAULT_ANISOTROPY_LEVELS
    }

    fun use(TARGET: Int) {
        GL11.glTexParameteri(TARGET, GL11.GL_TEXTURE_MIN_FILTER, minFilter.GL)
        GL11.glTexParameteri(TARGET, GL11.GL_TEXTURE_MAG_FILTER, magFilter.GL)
        GL11.glTexParameteri(TARGET, GL11.GL_TEXTURE_WRAP_S, wrapS.GL)
        GL11.glTexParameteri(TARGET, GL11.GL_TEXTURE_WRAP_T, wrapT.GL)
        GL11.glTexParameteri(TARGET, GL12.GL_TEXTURE_WRAP_R, wrapR.GL)
        GL11.glTexParameterfv(TARGET, GL11.GL_TEXTURE_BORDER_COLOR, borderColor.toArray())
        GL11.glTexParameteri(TARGET, GL12.GL_TEXTURE_MAX_LEVEL, mipmapLevels)
        GL11.glTexParameterf(
            TARGET,
            GL14.GL_TEXTURE_LOD_BIAS,
            min(lodBias.toDouble(), GL11.glGetInteger(GL14.GL_MAX_TEXTURE_LOD_BIAS).toDouble())
                .toFloat()
        )
        GL11.glTexParameterf(
            TARGET,
            GL46.GL_TEXTURE_MAX_ANISOTROPY,
            min(anisotropyLevels.toDouble(), GL11.glGetFloat(GL46.GL_MAX_TEXTURE_MAX_ANISOTROPY).toDouble())
                .toFloat()
        )
    }

    @JvmOverloads
    fun texImage2D(target: Int, buffer: ByteBuffer?, width: Int, height: Int, level: Int = 0) {
        GL11.glTexImage2D(
            target, level, sizedFormat.GL, width, height,
            0, sizedFormat.base.GL, type.GL, buffer
        )
    }

    fun texSubImage3D(target: Int, buffer: ByteBuffer?, width: Int, height: Int, z: Int) {
        GL12.glTexSubImage3D(
            target, 0, 0, 0, z, width, height, 1,
            sizedFormat.base.GL, type.GL, buffer
        )
    }

    fun setWrap(wrap: Wrap): TextureParameters {
        setWrap(wrap, wrap, wrap)
        return this
    }

    fun setWrap(s: Wrap, t: Wrap): TextureParameters {
        wrapS = s
        wrapT = t
        return this
    }

    fun setWrap(s: Wrap, t: Wrap, r: Wrap): TextureParameters {
        wrapS = s
        wrapT = t
        wrapR = r
        return this
    }

    fun setFilter(filter: Filter): TextureParameters {
        setFilter(filter, filter)
        return this
    }

    fun setFilter(min: Filter, mag: Filter): TextureParameters {
        minFilter = min
        magFilter = mag
        return this
    }

    fun getFormat(): Format? {
        return sizedFormat.base
    }

    fun setSizedFormat(sizedFormat: SizedFormat): TextureParameters {
        this.sizedFormat = sizedFormat
        return this
    }

    fun setType(type: Type): TextureParameters {
        this.type = type
        return this
    }

    fun setMipmapLevels(mipmapLevels: Int): TextureParameters {
        this.mipmapLevels = mipmapLevels
        return this
    }

    fun setAnisotropyLevels(anisotropyLevels: Float): TextureParameters {
        this.anisotropyLevels = anisotropyLevels
        return this
    }

    fun setLodBias(lodBias: Float): TextureParameters {
        this.lodBias = lodBias
        return this
    }

    companion object {
        var DEFAULT_MIN_FILTER = Filter.LINEAR_MIPMAP_LINEAR
        var DEFAULT_MAG_FILTER = Filter.NEAREST
        var DEFAULT_WRAP_S = Wrap.CLAMP_TO_EDGE
        var DEFAULT_WRAP_T = Wrap.CLAMP_TO_EDGE
        var DEFAULT_WRAP_R = Wrap.CLAMP_TO_EDGE
        var DEFAULT_FORMAT = SizedFormat.RGBA8
        var DEFAULT_TYPE = Type.UNSIGNED_BYTE
        var DEFAULT_MIPMAP_LEVELS = 0
        var DEFAULT_LOD_BIAS = -maxLodBias
        var DEFAULT_ANISOTROPY_LEVELS = 0f
        val maxLodBias: Float
            get() = GL11.glGetFloat(GL14.GL_MAX_TEXTURE_LOD_BIAS)
    }
}
