package pize.graphics.postprocess.effects

import org.lwjgl.opengl.GL11
import pize.Pize
import pize.app.Disposable
import pize.files.Resource
import pize.graphics.gl.*
import pize.graphics.postprocess.FrameBufferObject
import pize.graphics.texture.Texture
import pize.graphics.util.Shader
import pize.math.vecmath.matrix.Matrix4f
import pize.math.vecmath.vector.Vec3f

class ShadowMapping(private val width: Int, private val height: Int, size: Vec3f, private val pos: Vec3f, dir: Vec3f) :
    Disposable {
    private val dir: Vec3f?
    private val fbo: FrameBufferObject
    private val shader: Shader
    private val projectionMatrix: Matrix4f?
    val lightSpace: Matrix4f
    private val lookAtMatrix: Matrix4f

    init {
        this.dir = dir.nor()

        // Framebuffer
        fbo = FrameBufferObject(width, height)
        fbo.setAttachment(Attachment.DEPTH_ATTACHMENT)
        fbo.setWrite(false)
        fbo.setRead(false)
        fbo.info
            ?.setSizedFormat(SizedFormat.DEPTH_COMPONENT32)
            ?.setWrap(Wrap.CLAMP_TO_BORDER)
            ?.setFilter(Filter.NEAREST)
            ?.setType(Type.FLOAT)
            ?.borderColor?.set(1f, 1f, 1f, 1f)
        fbo.create()

        // Shader
        shader = Shader(Resource("shader/shadowMapping/shadow.vert"), Resource("shader/shadowMapping/shadow.frag"))
        projectionMatrix = Matrix4f().toOrthographic(-size.x / 2, size.x / 2, size.y / 2, -size.y / 2, 1f, size.z + 1)
        lightSpace = Matrix4f()
        lookAtMatrix = Matrix4f()
    }

    fun begin() {
        Gl.viewport(width, height)
        fbo.bind()
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT)
        shader.bind()
        lightSpace.set(projectionMatrix!!.getMul(lookAtMatrix.toLookAt(pos, dir)))
        shader.setUniform("u_space", lightSpace)
    }

    fun end() {
        fbo.unbind()
        Gl.viewport(Pize?.width!!, Pize?.height!!)
    }

    val shadowMap: Texture?
        get() = fbo.texture

    fun setModelMatrix(model: Matrix4f?) {
        shader.setUniform("u_model", model)
    }

    fun pos(): Vec3f {
        return pos
    }

    fun dir(): Vec3f? {
        return dir
    }

    override fun dispose() {
        shader.dispose()
        fbo.dispose()
    }
}
