package pize.io.mouse

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage
import pize.app.Disposable
import pize.graphics.texture.*
import pize.graphics.texture.PixmapIO.load

class Cursor : Disposable {
    val ID: Long

    constructor(filepath: String?) {
        val cursorTextureData: Pixmap = load(filepath!!)
        val cursorBuffer = GLFWImage.malloc()
        cursorBuffer[cursorTextureData.width, cursorTextureData.height] = cursorTextureData.buffer
        ID = GLFW.glfwCreateCursor(cursorBuffer, 0, 0)
    }

    constructor(cursorTexture: Texture) {
        val cursorBuffer = GLFWImage.malloc()
        cursorBuffer[cursorTexture.width, cursorTexture.height] = cursorTexture.pixmap?.buffer!!
        ID = GLFW.glfwCreateCursor(cursorBuffer, 0, 0)
    }

    constructor(cursorTexture: Pixmap) {
        val cursorBuffer = GLFWImage.malloc()
        cursorBuffer[cursorTexture.width, cursorTexture.height] = cursorTexture.buffer
        ID = GLFW.glfwCreateCursor(cursorBuffer, 0, 0)
    }

    override fun dispose() {
        GLFW.glfwDestroyCursor(ID)
    }
}
