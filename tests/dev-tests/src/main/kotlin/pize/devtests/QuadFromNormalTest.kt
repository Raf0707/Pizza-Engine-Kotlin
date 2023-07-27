package pize.devtests

import pize.Pize.dt
import pize.Pize.exit
import pize.Pize.window
import pize.app.AppAdapter
import pize.graphics.camera.PerspectiveCamera
import pize.graphics.camera.controller.Rotation3DController
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.gl.Gl.disable
import pize.graphics.gl.Target
import pize.graphics.gl.Type
import pize.graphics.util.BaseShader
import pize.graphics.util.BaseShader.Companion.getPos3UColor
import pize.graphics.util.SkyBox
import pize.graphics.vertex.Mesh
import pize.graphics.vertex.VertexAttr
import pize.io.glfw.Key
import pize.math.Maths.cosDeg
import pize.math.Maths.sinDeg
import pize.math.vecmath.vector.Vec3f
import pize.math.vecmath.vector.Vec3i

class QuadFromNormalTest : AppAdapter() {
    var skyBox: SkyBox? = null
    var shader: BaseShader? = null
    var camera: PerspectiveCamera? = null
    var rotationController: Rotation3DController? = null
    var quadMesh: Mesh? = null
    override fun init() {
        // Camera
        camera = PerspectiveCamera(0.5, 500.0, 70.0)
        camera!!.position.y += 3f
        rotationController = Rotation3DController()
        // Skybox
        skyBox = SkyBox()
        // Shader
        shader = getPos3UColor()
        // Mesh
        quadMesh = Mesh(VertexAttr(3, Type.FLOAT))
        generateFromBlockFace(Vec3i(0, 1, 0))
        quadMesh!!.setIndices(
            intArrayOf(
                0, 1, 2,
                2, 3, 0
            )
        )

        // Disable cull face
        disable(Target.CULL_FACE)
    }

    override fun update() {
        // Exit & Fullscreen
        if (Key.ESCAPE.isDown) exit()
        if (Key.F11.isDown) window()!!.toggleFullscreen()

        // Camera control
        val cameraMotion = Vec3f()
        val yawSin = sinDeg(camera!!.rotation.yaw.toDouble())
        val yawCos = cosDeg(camera!!.rotation.yaw.toDouble())
        if (Key.W.isPressed) cameraMotion.add(yawCos, 0f, yawSin)
        if (Key.S.isPressed) cameraMotion.add(-yawCos, 0f, -yawSin)
        if (Key.A.isPressed) cameraMotion.add(-yawSin, 0f, yawCos)
        if (Key.D.isPressed) cameraMotion.add(yawSin, 0f, -yawCos)
        if (Key.SPACE.isPressed) cameraMotion.y++
        if (Key.LEFT_SHIFT.isPressed) cameraMotion.y--

        // Camera update
        camera!!.position.add(cameraMotion.mul(dt * 2))
        rotationController!!.update()
        camera!!.rotation.set(rotationController!!.rotation)
        camera!!.update()
    }

    override fun render() {
        // Clear color
        clearColorBuffer()
        clearColor(0.2, 0.2, 0.22)

        // Render skybox
        skyBox!!.render(camera!!)

        // Render mesh & ray
        shader!!.bind()
        shader!!.setMatrices(camera!!)
        shader!!.setColor(1f, 0f, 0f)
        quadMesh!!.render()
    }

    fun generateFromBlockFace(normal: Vec3i) {
        // Calculate positions (N - normal, p(1-4) - vertex position)

        //   p1 ------- p4
        //   |           |
        //   |     N     |
        //   |           |
        //   p2 ------- p3

        // Calc
        val p4 = Vec3f(1).sub(normal) // calc p4
        val p2 = p4.copy().mul(-1) // inv p4
        val p1 = Vec3f(normal).crs(p4) // crs n*p4
        val p3 = Vec3f(normal).crs(p2) // crs n*p2
        println(Vec3f(1, 0, 1).crs(Vec3f(0, 1, 0)))
        println(p1)
        println(p2)
        println(p3)
        println(p4)

        // Scale, shift
        normal.mul(0.5).add(0.5)
        p1.mul(0.5).add(normal)
        p2.mul(0.5).add(normal)
        p3.mul(0.5).add(normal)
        p4.mul(0.5).add(normal)

        // Set vertices
        quadMesh!!.setVertices(
            floatArrayOf(
                p1.x, p1.y, p1.z,  // 0
                p2.x, p2.y, p2.z,  // 1
                p3.x, p3.y, p3.z,  // 2
                p4.x, p4.y, p4.z
            )
        )
    }
}
