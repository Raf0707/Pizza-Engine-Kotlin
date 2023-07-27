package pize.tests.drift

import pize.Pize.create
import pize.Pize.exit
import pize.Pize.run
import pize.app.AppAdapter
import pize.graphics.camera.CenteredOrthographicCamera
import pize.graphics.gl.Gl.clearColor
import pize.graphics.gl.Gl.clearColorBuffer
import pize.graphics.texture.Texture
import pize.graphics.util.batch.TextureBatch
import pize.io.glfw.Key

class Drift : AppAdapter() {
    private val car: Car
    private val batch: TextureBatch
    private val camera: CenteredOrthographicCamera
    private val roadTexture: Texture

    init {
        car = Car()
        batch = TextureBatch()
        camera = CenteredOrthographicCamera()
        roadTexture = Texture("road.png")
    }

    override fun update() {
        if (Key.ESCAPE.isDown) exit()
        car.update()
        camera.position.set(car.position) //.rotDeg(-car.getRotation()).add(0, 130);
        //camera.setRotation(-car.getRotation());
        camera.scale = 0.4f
        camera.update()
    }

    override fun render() {
        clearColorBuffer()
        clearColor(0.2, 0.2, 0.3, 1.0)
        batch.begin(camera)
        batch.draw(roadTexture, -200f, -2000f, 400f, 4000f)
        car.render(batch)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        camera.resize(width, height)
    }

    override fun dispose() {
        car.dispose()
        batch.dispose()
        roadTexture.dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            create("Drift", 2000, 1000)
            run(Drift())
        }
    }
}
