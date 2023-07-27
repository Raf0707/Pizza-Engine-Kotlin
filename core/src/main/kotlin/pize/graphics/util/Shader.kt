package pize.graphics.util

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32
import pize.files.Resource
import pize.graphics.gl.GlObject
import pize.graphics.texture.CubeMap
import pize.graphics.texture.Texture
import pize.graphics.texture.TextureArray
import pize.graphics.util.color.IColor
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec2f
import pize.math.vecmath.vector.Vec3f

open class Shader : GlObject {
    private val uniforms: HashMap<String, Int>
    private var num_sampler2D = 0
    private var num_samplerCube = 0
    private var num_sampler2DArray = 0

    constructor(vertexCode: String?, fragmentCode: String?) : super(GL20.glCreateProgram()) {
        val vertexShaderID = createShader(vertexCode, GL20.GL_VERTEX_SHADER)
        val fragmentShaderID = createShader(fragmentCode, GL20.GL_FRAGMENT_SHADER)
        GL20.glAttachShader(ID, vertexShaderID)
        GL20.glAttachShader(ID, fragmentShaderID)
        GL20.glLinkProgram(ID)
        if (GL20.glGetProgrami(
                ID,
                GL20.GL_LINK_STATUS
            ) == GL11.GL_FALSE
        ) throw RuntimeException("Linking shader error: " + GL20.glGetProgramInfoLog(ID))
        GL20.glValidateProgram(ID)
        if (GL20.glGetProgrami(
                ID,
                GL20.GL_VALIDATE_STATUS
            ) == GL11.GL_FALSE
        ) throw RuntimeException("Validating shader error: " + GL20.glGetProgramInfoLog(ID))
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        uniforms = HashMap()
        detectUniforms(vertexCode)
        detectUniforms(fragmentCode)
    }

    constructor(resVertex: Resource, resFragment: Resource) : this(resVertex.readString(), resFragment.readString())
    constructor(vertexCode: String?, fragmentCode: String?, geometryCode: String?) : super(GL20.glCreateProgram()) {
        val vertexShaderID = createShader(vertexCode, GL20.GL_VERTEX_SHADER)
        val fragmentShaderID = createShader(fragmentCode, GL20.GL_FRAGMENT_SHADER)
        val geometryShaderID = createShader(geometryCode, GL32.GL_GEOMETRY_SHADER)
        GL20.glAttachShader(ID, vertexShaderID)
        GL20.glAttachShader(ID, fragmentShaderID)
        GL20.glAttachShader(ID, geometryShaderID)
        GL20.glLinkProgram(ID)
        if (GL20.glGetProgrami(
                ID,
                GL20.GL_LINK_STATUS
            ) == GL11.GL_FALSE
        ) throw RuntimeException("Linking shader error: " + GL20.glGetProgramInfoLog(ID))
        GL20.glValidateProgram(ID)
        if (GL20.glGetProgrami(
                ID,
                GL20.GL_VALIDATE_STATUS
            ) == GL11.GL_FALSE
        ) throw RuntimeException("Validating shader error: " + GL20.glGetProgramInfoLog(ID))
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        GL20.glDeleteShader(geometryShaderID)
        uniforms = HashMap()
        detectUniforms(vertexCode)
        detectUniforms(fragmentCode)
        detectUniforms(geometryCode)
    }

    constructor(resVertex: Resource, resFragment: Resource, resGeometry: Resource) : this(
        resVertex.readString(),
        resFragment.readString(),
        resGeometry.readString()
    )

    private fun createShader(code: String?, shaderType: Int): Int {
        val shaderID = GL20.glCreateShader(shaderType)
        GL20.glShaderSource(shaderID, code)
        GL20.glCompileShader(shaderID)
        if (GL20.glGetShaderi(
                shaderID,
                GL20.GL_COMPILE_STATUS
            ) == GL11.GL_FALSE
        ) throw RuntimeException("Compiling shader error: " + GL20.glGetShaderInfoLog(shaderID))
        return shaderID
    }

    private fun detectUniforms(code: String?) {                       // '..\nuniform type name [16] ;\n..'
        val uniformSplit =
            code!!.split("uniform".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // ' type name [16] ;\n..'
        for (i in 1 until uniformSplit.size) {
            var name = uniformSplit[i].split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0] // ' type name [16] '
            if (name.contains("[")) name = name.substring(0, name.lastIndexOf("[")) // ' type name '
            name = name.strip() // 'type name'
            name = name.substring(name.lastIndexOf(" ") + 1) // 'name'
            uniforms[name] = GL20.glGetUniformLocation(ID, name)
        }
    }

    fun setUniform(uniformName: String, matrix4f: Matrix4f?) {
        GL20.glUniformMatrix4fv(uniforms[uniformName]!!, false, matrix4f!!.`val`)
    }

    fun setUniform(uniformName: String, v: Vec2f) {
        GL20.glUniform2f(uniforms[uniformName]!!, v.x, v.y)
    }

    fun setUniform(uniformName: String, v: Vec3f) {
        GL20.glUniform3f(uniforms[uniformName]!!, v.x, v.y, v.z)
    }

    fun setUniform(uniformName: String, x: Float) {
        GL20.glUniform1f(uniforms[uniformName]!!, x)
    }

    fun setUniform(uniformName: String, x: Float, y: Float) {
        GL20.glUniform2f(uniforms[uniformName]!!, x, y)
    }

    fun setUniform(uniformName: String, x: Float, y: Float, z: Float) {
        GL20.glUniform3f(uniforms[uniformName]!!, x, y, z)
    }

    fun setUniform(uniformName: String, x: Float, y: Float, z: Float, w: Float) {
        GL20.glUniform4f(uniforms[uniformName]!!, x, y, z, w)
    }

    fun setUniform(uniformName: String, array: FloatArray?) {
        GL20.glUniform1fv(uniforms[uniformName]!!, array)
    }

    fun setUniform(uniformName: String, value: Int) {
        GL20.glUniform1i(uniforms[uniformName]!!, value)
    }

    fun setUniform(uniformName: String, array: IntArray?) {
        GL20.glUniform1iv(uniforms[uniformName]!!, array)
    }

    fun setUniform(uniformName: String, color: IColor) {
        GL20.glUniform4f(uniforms[uniformName]!!, color.r(), color.g(), color.b(), color.a())
    }

    fun setUniform(uniformName: String, texture: Texture?) {
        texture!!.bind(num_sampler2D)
        GL20.glUniform1i(uniforms[uniformName]!!, num_sampler2D)
        num_sampler2D++
    }

    fun setUniform(uniformName: String, textureArray: TextureArray) {
        textureArray.bind(num_sampler2DArray)
        GL20.glUniform1i(uniforms[uniformName]!!, num_sampler2DArray)
        num_sampler2DArray++
    }

    fun setUniform(uniformName: String, cubeMap: CubeMap) {
        cubeMap.bind(num_samplerCube)
        GL20.glUniform1i(uniforms[uniformName]!!, num_samplerCube)
        num_samplerCube++
    }

    fun bindAttribute(index: Int, name: String?) {
        GL20.glBindAttribLocation(ID, index, name)
    }

    fun bind() {
        GL20.glUseProgram(ID)
        num_sampler2D = 0
        num_samplerCube = 0
        num_sampler2DArray = 0
    }

    override fun dispose() {
        GL20.glDeleteProgram(ID)
    }

    companion object {
        fun unbind() {
            GL20.glUseProgram(0)
        }
    }
}
