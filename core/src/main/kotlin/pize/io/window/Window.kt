package pize.io.window

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil
import pize.app.Disposable
import pize.app.Resizable
import pize.files.Resource
import pize.graphics.texture.Pixmap
import pize.graphics.texture.PixmapIO.load
import pize.io.monitor.Monitor
import pize.io.monitor.MonitorManager
import pize.io.mouse.Cursor
import java.io.IOException
import java.nio.*
import javax.imageio.ImageIO

class Window @JvmOverloads constructor(
    title: String?,
    var width: Int,
    var height: Int,
    private var resizable: Boolean = true,
    private var vsync: Boolean = true,
    samples: Int = 1
) : Disposable, Resizable {
    val ID: Long
    var x = 0
    var y = 0
    private var windowedLastWidth = 0
    private var windowedLastHeight = 0
    private var windowedLastX = 0
    private var windowedLastY = 0
    private var fullscreen = false
    var isFocused = false
    var title: String? = null
        set(title) {
            field = title
            GLFW.glfwSetWindowTitle(ID, title)
        }
    private val sizeCallbackList: MutableList<SizeCallback>

    init {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, if (resizable) 1 else 0)
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, samples)
        ID = GLFW.glfwCreateWindow(width, height, title, 0, 0)
        if (ID == 0L) throw Error("Failed to create the GLFW Window")
        val monitor: Monitor = MonitorManager.Companion?.primary!!
        GLFW.glfwSetWindowPos(ID, monitor.width / 2 - width / 2, monitor.height / 2 - height / 2)
        GLFW.glfwMakeContextCurrent(ID)
        GL.createCapabilities()
        GLFW.glfwSwapInterval(if (vsync) 1 else 0)
        GLFW.glfwSetWindowFocusCallback(ID) { id: Long, flag: Boolean -> isFocused = flag }
        GLFW.glfwSetWindowPosCallback(ID) { id: Long, x: Int, y: Int ->
            this.x = x
            this.y = y
        }
        sizeCallbackList = ArrayList()
        GLFW.glfwSetWindowSizeCallback(ID) { id: Long, w: Int, h: Int ->
            width = w
            height = h
            for (sizeCallback in sizeCallbackList) sizeCallback.invoke(w, h)
        }
    }

    fun isFullscreen(): Boolean {
        return fullscreen
    }

    fun setFullscreen(flag: Boolean) {
        if (flag == fullscreen) return
        fullscreen = flag
        val monitor: Monitor = MonitorManager.Companion?.primary!!
        if (flag) {
            windowedLastX = x
            windowedLastY = y
            windowedLastWidth = width
            windowedLastHeight = height
            GLFW.glfwSetWindowMonitor(ID, monitor.ID, 0, 0, monitor.width, monitor.height, monitor.refreshRate)
        } else GLFW.glfwSetWindowMonitor(
            ID,
            0,
            windowedLastX,
            windowedLastY,
            windowedLastWidth,
            windowedLastHeight,
            monitor.refreshRate
        )
    }

    fun toggleFullscreen() {
        setFullscreen(!fullscreen)
    }

    fun isVsync(): Boolean {
        return vsync
    }

    fun setVsync(vsync: Boolean) {
        if (vsync == this.vsync) return
        this.vsync = vsync
        GLFW.glfwSwapInterval(if (vsync) 1 else 0)
    }

    fun toggleVsync() {
        setVsync(!vsync)
    }

    override fun resize(width: Int, height: Int) {
        GLFW.glfwSetWindowSize(ID, width, height)
    }

    fun setPos(x: Int, y: Int) {
        GLFW.glfwSetWindowPos(ID, x, y)
    }

    fun isResizable(): Boolean {
        return resizable
    }

    fun setResizable(resizable: Boolean) {
        this.resizable = resizable
        GLFW.glfwSetWindowAttrib(ID, GLFW.GLFW_RESIZABLE, if (resizable) 1 else 0)
    }

    fun toggleResizable() {
        setResizable(!resizable)
    }

    fun setIcon(filePath: String) {
        try {
            val bufferedImage = ImageIO.read(Resource(filePath).inStream())
            val pixmap: Pixmap = load(bufferedImage!!)
            val image = GLFWImage.malloc()
            val iconBuffer = GLFWImage.malloc(1)
            image[pixmap.width, pixmap.height] = pixmap.buffer
            iconBuffer.put(0, image)
            GLFW.glfwSetWindowIcon(ID, iconBuffer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun setCursor(cursor: Cursor?) {
        GLFW.glfwSetCursor(ID, cursor?.ID ?: 0)
    }

    fun swapBuffers() {
        GLFW.glfwSwapBuffers(ID)
    }

    fun show() {
        GLFW.glfwShowWindow(ID)
    }

    fun hide() {
        GLFW.glfwHideWindow(ID)
    }

    fun closeRequest(): Boolean {
        return GLFW.glfwWindowShouldClose(ID)
    }

    fun aspect(): Float {
        return width.toFloat() / height
    }

    fun addSizeCallback(sizeCallback: SizeCallback) {
        sizeCallbackList.add(sizeCallback)
    }

    fun removeSizeCallback(sizeCallback: SizeCallback) {
        sizeCallbackList.remove(sizeCallback)
    }

    fun setFileDropCallback(callback: FileDropCallback) {
        GLFW.glfwSetDropCallback(ID) { windowID: Long, count: Int, names: Long ->
            val nameBuffer = MemoryUtil.memPointerBuffer(names, count)
            val files = arrayOfNulls<String>(count)
            for (i in 0 until count) files[i] = MemoryUtil.memUTF8(MemoryUtil.memByteBufferNT1(nameBuffer[i]))
            callback.invoke(count, files)
        }
    }

    var clipboardString: String?
        get() = GLFW.glfwGetClipboardString(ID)
        set(charSequence) {
            GLFW.glfwSetClipboardString(ID, charSequence)
        }

    fun setClipboardString(buffer: String?) {
        GLFW.glfwSetClipboardString(ID, buffer)
    }

    override fun dispose() {
        Callbacks.glfwFreeCallbacks(ID)
        GLFW.glfwDestroyWindow(ID)
    }
}
