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
import pize.io.monitor.Monitor
import pize.io.monitor.MonitorManager
import pize.io.mouse.Cursor
import java.io.IOException
import java.nio.*
import javax.imageio.ImageIO

class Window @JvmOverloads constructor(
    title: String?,
    val width: Int,
    val height: Int,
    private var resizable: Boolean = true,
    private var vsync: Boolean = true,
    samples: Int = 1
) : Disposable, Resizable {
    val iD: Long
    val x = 0
    val y = 0
    private var windowedLastWidth = 0
    private var windowedLastHeight = 0
    private var windowedLastX = 0
    private var windowedLastY = 0
    private var fullscreen = false
    var isFocused = false
    var title: String? = null
        set(title) {
            field = title
            GLFW.glfwSetWindowTitle(iD, title)
        }
    private val sizeCallbackList: MutableList<SizeCallback>

    init {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, if (resizable) 1 else 0)
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, samples)
        iD = GLFW.glfwCreateWindow(width, height, title, 0, 0)
        if (iD == 0L) throw Error("Failed to create the GLFW Window")
        val monitor: Monitor = MonitorManager.Companion.getPrimary()
        GLFW.glfwSetWindowPos(iD, monitor.width / 2 - width / 2, monitor.height / 2 - height / 2)
        GLFW.glfwMakeContextCurrent(iD)
        GL.createCapabilities()
        GLFW.glfwSwapInterval(if (vsync) 1 else 0)
        GLFW.glfwSetWindowFocusCallback(iD) { id: Long, flag: Boolean -> isFocused = flag }
        GLFW.glfwSetWindowPosCallback(iD) { id: Long, x: Int, y: Int ->
            this.x = x
            this.y = y
        }
        sizeCallbackList = ArrayList()
        GLFW.glfwSetWindowSizeCallback(iD) { id: Long, w: Int, h: Int ->
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
        val monitor: Monitor = MonitorManager.Companion.getPrimary()
        if (flag) {
            windowedLastX = x
            windowedLastY = y
            windowedLastWidth = width
            windowedLastHeight = height
            GLFW.glfwSetWindowMonitor(iD, monitor.id, 0, 0, monitor.width, monitor.height, monitor.refreshRate)
        } else GLFW.glfwSetWindowMonitor(
            iD,
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
        GLFW.glfwSetWindowSize(iD, width, height)
    }

    fun setPos(x: Int, y: Int) {
        GLFW.glfwSetWindowPos(iD, x, y)
    }

    fun isResizable(): Boolean {
        return resizable
    }

    fun setResizable(resizable: Boolean) {
        this.resizable = resizable
        GLFW.glfwSetWindowAttrib(iD, GLFW.GLFW_RESIZABLE, if (resizable) 1 else 0)
    }

    fun toggleResizable() {
        setResizable(!resizable)
    }

    fun setIcon(filePath: String) {
        try {
            val bufferedImage = ImageIO.read(Resource(filePath).inStream())
            val pixmap: Pixmap = load(bufferedImage)
            val image = GLFWImage.malloc()
            val iconBuffer = GLFWImage.malloc(1)
            image[pixmap.width, pixmap.height] = pixmap.buffer
            iconBuffer.put(0, image)
            GLFW.glfwSetWindowIcon(iD, iconBuffer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun setCursor(cursor: Cursor?) {
        GLFW.glfwSetCursor(iD, cursor?.id ?: 0)
    }

    fun swapBuffers() {
        GLFW.glfwSwapBuffers(iD)
    }

    fun show() {
        GLFW.glfwShowWindow(iD)
    }

    fun hide() {
        GLFW.glfwHideWindow(iD)
    }

    fun closeRequest(): Boolean {
        return GLFW.glfwWindowShouldClose(iD)
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
        GLFW.glfwSetDropCallback(iD) { windowID: Long, count: Int, names: Long ->
            val nameBuffer = MemoryUtil.memPointerBuffer(names, count)
            val files = arrayOfNulls<String>(count)
            for (i in 0 until count) files[i] = MemoryUtil.memUTF8(MemoryUtil.memByteBufferNT1(nameBuffer[i]))
            callback.invoke(count, files)
        }
    }

    var clipboardString: String?
        get() = GLFW.glfwGetClipboardString(iD)
        set(charSequence) {
            GLFW.glfwSetClipboardString(iD, charSequence)
        }

    fun setClipboardString(buffer: String?) {
        GLFW.glfwSetClipboardString(iD, buffer)
    }

    override fun dispose() {
        Callbacks.glfwFreeCallbacks(iD)
        GLFW.glfwDestroyWindow(iD)
    }
}
