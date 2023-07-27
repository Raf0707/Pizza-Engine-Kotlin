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
import pize.graphics.gl.Primitive
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
import pize.physic.Ray3f

class TriangleIntersectionTest : AppAdapter() {
    var skyBox: SkyBox? = null
    var shader: BaseShader? = null
    var camera: PerspectiveCamera? = null
    var rotationController: Rotation3DController? = null
    var ray: Ray3f? = null
    var rayMesh: Mesh? = null
    var mesh: Mesh? = null
    override fun init() {
        // Camera
        camera = PerspectiveCamera(0.5, 500.0, 70.0)
        rotationController = Rotation3DController()
        ray = Ray3f()
        // Skybox
        skyBox = SkyBox()
        // Shader
        shader = getPos3UColor()
        // Mesh
        mesh = Mesh(VertexAttr(3, Type.FLOAT))
        mesh!!.setVertices(
            floatArrayOf(
                1f, 3f, 0f,  // 0
                0f, 0f, 0f,  // 1
                3f, 1f, 0f
            )
        )
        mesh!!.setIndices(
            intArrayOf(
                0, 1, 2
            )
        )

        // Ray Mesh
        rayMesh = Mesh(VertexAttr(3, Type.FLOAT))
        rayMesh!!.setRenderMode(Primitive.LINES)
        rayMesh!!.setIndices(
            intArrayOf(
                0, 1
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
        ray!![camera!!.position, camera!!.rotation.direction] = 100f
        if (ray!!.intersect(
                Vec3f(1, 3, 0),
                Vec3f(0, 0, 0),
                Vec3f(3, 1, 0)
            ) != -1f
        ) println(1)
        val position = camera!!.position
        val v2 = position.copy().add(camera!!.rotation.direction.mul(100))
        rayMesh!!.setVertices(
            floatArrayOf(
                0f, 0f, 0f,  // position.x, position.y, position.z,
                v2.x, v2.y, v2.z
            )
        )
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
        mesh!!.render()
        rayMesh!!.render()
    }
}
