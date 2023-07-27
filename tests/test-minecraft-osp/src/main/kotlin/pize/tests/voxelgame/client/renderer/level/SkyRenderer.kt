package pize.tests.voxelgame.client.renderer.level

import pize.app.Disposable
import pize.files.Resource
import pize.graphics.gl.Face
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.cullFace
import pize.graphics.gl.Gl.depthMask
import pize.graphics.gl.Primitive
import pize.graphics.gl.Type
import pize.graphics.util.BufferBuilder
import pize.graphics.util.Shader
import pize.graphics.util.SkyBox
import pize.graphics.util.color.Color
import pize.graphics.vertex.VertexArray
import pize.graphics.vertex.VertexAttr
import pize.graphics.vertex.VertexBuffer
import pize.math.Mathc.cos
import pize.math.Mathc.pow
import pize.math.Mathc.sin
import pize.math.Maths
import pize.math.Maths.frac
import pize.math.Maths.map
import pize.math.Maths.sinDeg
import pize.math.vecmath.matrix.Matrix4f
import pize.tests.voxelgame.client.control.camera.GameCamera

class SkyRenderer(private val levelRenderer: LevelRenderer) : Disposable {
    private val skyBox: SkyBox
    private val skyViewMatrix: Matrix4f
    private val sunriseShader: Shader
    private val skydiscShader: Shader
    private val sunriseVbo: VertexBuffer
    private val lightSkyVbo: VertexBuffer
    private val darkSkyVbo: VertexBuffer
    private val sunriseVao: VertexArray
    private val lightSkyVao: VertexArray
    private val darkSkyVao: VertexArray

    init {
        skyBox = SkyBox(
            "texture/skybox/1/skybox_positive_x.png",
            "texture/skybox/1/skybox_negative_x.png",
            "texture/skybox/1/skybox_positive_y.png",
            "texture/skybox/1/skybox_negative_y.png",
            "texture/skybox/1/skybox_positive_z.png",
            "texture/skybox/1/skybox_negative_z.png"
        )
        skyViewMatrix = Matrix4f()
        sunriseShader = Shader(Resource("shader/level/sky/sunrise.vert"), Resource("shader/level/sky/sunrise.frag"))
        skydiscShader = Shader(Resource("shader/level/sky/skydisc.vert"), Resource("shader/level/sky/skydisc.frag"))
        sunriseVao = VertexArray()
        sunriseVbo = VertexBuffer()
        sunriseVbo.enableAttributes(VertexAttr(3, Type.FLOAT), VertexAttr(4, Type.FLOAT)) // pos3, col4
        lightSkyVao = VertexArray()
        lightSkyVbo = VertexBuffer()
        lightSkyVbo.enableAttributes(VertexAttr(3, Type.FLOAT)) // pos3
        darkSkyVao = VertexArray()
        darkSkyVbo = VertexBuffer()
        darkSkyVbo.enableAttributes(VertexAttr(3, Type.FLOAT)) // pos3
        buildSkyDisc(16f).end(lightSkyVbo)
        buildSkyDisc(-16f).end(darkSkyVbo)


        // final BufferBuilder builder = new BufferBuilder();
        // builder.vertex( 448, 24, -384);
        //
        // builder.vertex(-384, 24,  448);
        // builder.vertex(-384, 24, -384);
        //
        // builder.vertex( 448, 24,  448);
        // builder.end(lightSkyVbo);
    }

    private fun buildSkyDisc(radius: Float): BufferBuilder {
        val builder = BufferBuilder()
        builder.vertex(0.0, radius.toDouble(), 0.0)
        var i = 0
        while (i <= 360) {
            val angle = i * Maths.ToRad
            builder.vertex(
                radius * kotlin.math.cos(angle.toDouble()),
                0.0,
                radius * kotlin.math.sin(angle.toDouble())
            )
            i += 120
        }
        return builder
    }

    fun render(camera: GameCamera) {
        // Skybox
        skyViewMatrix.set(camera.view)
        skyViewMatrix.cullPosition()
        // skyBox.render(camera.getProjection(), skyViewMatrix);
        val time = levelRenderer.gameRenderer.session.game.time
        val skyColor = skyColor
        val fogColor = fogColor
        depthMask(false)
        clearColor(fogColor)
        cullFace(Face.FRONT)
        skydiscShader.bind()
        skydiscShader.setUniform("u_projection", camera.projection)
        skydiscShader.setUniform("u_view", skyViewMatrix)
        skydiscShader.setUniform("u_skyColor", skyColor)
        lightSkyVao.drawArrays(lightSkyVbo.verticesNum, Primitive.TRIANGLE_FAN)
        cullFace(Face.BACK)
        depthMask(true)
        if (true) return
        depthMask(false)
        cullFace(Face.FRONT)

        // Render light sky
        skydiscShader.bind()
        skydiscShader.setUniform("u_projection", camera.projection)
        skydiscShader.setUniform("u_view", skyViewMatrix)
        skydiscShader.setUniform("u_skyColor", skyColor)
        lightSkyVao.drawArrays(lightSkyVbo.verticesNum, Primitive.TRIANGLE_FAN)

        // Render sunrise
        val sunriseColor = getSunriseColor(frac(time.days).toFloat())
        if (sunriseColor != null) {
            val bufferbuilder = BufferBuilder()
            bufferbuilder.vertex(0.0, 100.0, 0.0)
                .color(sunriseColor[0].toDouble(), sunriseColor[1].toDouble(), sunriseColor[2].toDouble())
            val i = 16
            for (j in 0..i) {
                val angle = 2 * Maths.PI * j / i
                val sin = sin(angle.toDouble())
                val cos = cos(angle.toDouble())
                bufferbuilder.vertex(
                    (sin * 120.0f).toDouble(),
                    (cos * 120.0f).toDouble(),
                    (-cos * 40.0f * sunriseColor[3]).toDouble()
                ).color(
                    sunriseColor[0].toDouble(), sunriseColor[1].toDouble(), sunriseColor[2].toDouble(), 0.0
                )
            }
            bufferbuilder.end(sunriseVbo)
            sunriseShader.bind()
            sunriseShader.setUniform("u_projection", camera.projection)
            sunriseShader.setUniform("u_view", skyViewMatrix)
            sunriseVao.drawArrays(sunriseVbo.verticesNum, Primitive.TRIANGLE_FAN)
            println(sunriseVbo.verticesNum)
        }

        // Render dark sky
        if (camera.y < 0) {
            skydiscShader.bind()
            skydiscShader.setUniform("u_projection", camera.projection)
            skydiscShader.setUniform("u_view", skyViewMatrix)
            skydiscShader.setUniform("u_skyColor", Color(0, 0, 0, 1))
            darkSkyVao.drawArrays(darkSkyVbo.verticesNum, Primitive.TRIANGLE_FAN)
        }
        depthMask(true)
        cullFace(Face.BACK)
    }

    val fogColor: Color
        get() {
            val time = levelRenderer.gameRenderer.session.game.time
            return Color(0.6, 0.75, 0.9, 0.95)
                .mul(
                    map(
                        pow(sinDeg((time.days * 360).toDouble()).toDouble(), 0.5),
                        -Maths.Sqrt2,
                        Maths.Sqrt2,
                        0f,
                        1f
                    ).toDouble()
                )
        }
    val skyColor: Color
        get() {
            val time = levelRenderer.gameRenderer.session.game.time
            return Color(0.35, 0.6, 1.0, 1.0)
                .mul(
                    map(
                        pow(sinDeg((time.days * 360).toDouble()).toDouble(), 0.5),
                        -Maths.Sqrt2,
                        Maths.Sqrt2,
                        0f,
                        1f
                    ).toDouble()
                )
        }
    val fogStart: Float
        get() = 0.6f

    fun getSunriseColor(dayTime: Float): FloatArray? {
        val sunriseCol = FloatArray(4)
        val cos = cos(dayTime * Math.PI * 2)
        return if (cos >= -0.4f && cos <= 0.4f) {
            val a = cos / 0.4f * 0.5f + 0.5f
            var b = 1 - (1 - sin((a * Math.PI.toFloat()).toDouble())) * 0.99f
            b *= b
            sunriseCol[0] = a * 0.3f + 0.7f
            sunriseCol[1] = a * a * 0.7f + 0.2f
            sunriseCol[2] = a * a * 0 + 0.2f
            sunriseCol[3] = b
            sunriseCol
        } else null
    }

    override fun dispose() {
        skyBox.dispose()
        sunriseShader.dispose()
        sunriseVbo.dispose()
        sunriseVao.dispose()
        skydiscShader.dispose()
        lightSkyVbo.dispose()
        lightSkyVao.dispose()
    }
}
